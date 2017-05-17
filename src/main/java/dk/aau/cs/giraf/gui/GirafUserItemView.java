package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import dk.aau.cs.giraf.models.core.Department;
import dk.aau.cs.giraf.models.core.User;

/**
 * Created on 08/05/2017.
 */

//Note: As of 2017, we no longer use ImageViews. Instead this class now uses Pictogram objects,
// which themselves contain pictures.
public class GirafUserItemView extends LinearLayout implements Checkable {

    // The inflated view (See constructors)
    private View inflatedView;

    private RelativeLayout pictogramIconContainer;
    private ImageView iconImageView;
    private TextView titleContainer;

    private AsyncTask<Void, Void, Bitmap> loadPictogramImage;
    private Runnable updateSizeAndSetVisible;

    //Alternative to "fallback" as drawableToBitmap can't be called inside an async task.
    // (How did the last student even do that in the first place?)
    private Bitmap alternativeFallback = drawableToBitmap(this.getResources().getDrawable(R.drawable.no_profile_pic));

    /**
     * Used to implement edit triangle
     */
    private boolean isEditable = false;
    private Paint editableIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Point point1_draw = new Point();
    private Point point2_draw = new Point();
    private Point point3_draw = new Point();
    private Path path = new Path();

    // For the global top left position of @param view
    final int[] viewLocation = new int[2];

    // For the global top left position of this GirafUserItemView
    final int[] thisLocation = new int[2];

    // For the conversion to relative bottom right position
    final int[] bottomRightLocation = new int[2];

    // Used to indicate if the image should be gray scaled (As of 2017, this is no longer used)
    private boolean useGrayScale = false;

    /**
     * Do not use this constructor in code. It should only be used to inflate it from xml!
     */
    public GirafUserItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            initialize(null, null, null, attrs);
            return;
        }

        User sample = new User(new Department("sample"), "sampleName", "samplePassword");
        Bitmap sampleIcon = drawableToBitmap(context.getResources().getDrawable(R.drawable.no_profile_pic));
        sample.setIcon(sampleIcon);
        initialize(sample, null, "Sample", attrs);
    }

    //<editor-fold desc="constructors">

    /**
     * Constructor for GirafUserItemView
     * @param user the imageEntity to base the view upon
     */
    public GirafUserItemView(final Context context, final User user) {
        this(context, user, null, null);
    }

    /**
     * Constructor for GirafUserItemView
     * @param user the imageEntity to base the view upon
     * @param title the title to display below the view
     */
    public GirafUserItemView(final Context context, final User user, final String title) {
        this(context, user, null, title);
    }

    /**
     * Constructor for GirafUserItemView
     * @param user the imageEntity to base the view upon
     * @param fallback a fallback drawable in case that the provided ImageEntity does not contain an image
     */
    public GirafUserItemView(final Context context, final User user, final Drawable fallback) {
        this(context, user, fallback, null);
    }

    /**
     * Constructor for GirafUserItemView
     * @param user the imageEntity to base the view upon
     * @param fallback a fallback drawable in case that the provided ImageEntity does not contain an image
     * @param title the title to display below the view
     */
    public GirafUserItemView(final Context context, final User user, final Drawable fallback, final String title) {
        super(context);

        initialize(user, fallback, title, null);
    }

    /**
     * Constructor for GirafUserItemView
     * @param user the imageEntity to base the view upon
     * @param useGrayScale indicate if the image should be displayed as gray scaled. True if so, false if not. Defaults to false
     */
    public GirafUserItemView(final Context context, final User user, final boolean useGrayScale) {
        this(context, user, null, null);

        this.useGrayScale = useGrayScale;
    }

    /**
     * Constructor for GirafUserItemView
     * @param user the imageEntity to base the view upon
     * @param title the title to display below the view
     * @param useGrayScale indicate if the image should be displayed as gray scaled. True if so, false if not. Defaults to false
     */
    public GirafUserItemView(final Context context, final User user, final String title, final boolean useGrayScale) {
        this(context, user, null, title);

        this.useGrayScale = useGrayScale;
    }

    /**
     * Constructor for GirafUserItemView
     * @param user the the imageEntity to base the view upon
     * @param fallback a fallback drawable in case that the provided ImageEntity does not contain an image
     * @param useGrayScale indicate if the image should be displayed as gray scaled. True if so, false if not. Defaults to false
     */
    public GirafUserItemView(final Context context, final User user, final Drawable fallback, final boolean useGrayScale) {
        this(context, user, fallback, null);

        this.useGrayScale = useGrayScale;
    }

    /**
     * Constructor for GirafUserItemView
     * @param user the imageEntity to base the view upon
     * @param fallback a fallback drawable in case that the provided ImageEntity does not contain an image
     * @param title the title to display below the view
     * @param useGrayScale indicate if the image should be displayed as gray scaled. True if so, false if not. Defaults to false
     */
    public GirafUserItemView(final Context context, final User user, final Drawable fallback, final String title, final boolean useGrayScale) {
        this(context, user, fallback, title);

        this.useGrayScale = useGrayScale;
    }
    //</editor-fold>

    /**
     * Initialized the different components
     */
    private void initialize(final User user, final Drawable fallback, final String title, final AttributeSet attrs) {

        // Disable layout optimization in order to enable this views onDraw method to be called by its parent
        // NOTICE: This is require to draw the edit-triangle
        setWillNotDraw(false);

        // Find the XML for the imageEntity and load it into the view
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflatedView = inflater.inflate(R.layout.giraf_pictogram, this);

        // Find views that will be used throughout the class
        pictogramIconContainer = (RelativeLayout) inflatedView.findViewById(R.id.pictogram_icon_container);
        iconImageView = (ImageView) pictogramIconContainer.findViewById(R.id.pictogram_icon);

        // Hide the layout until it is loaded correctly
        inflatedView.setVisibility(INVISIBLE);

        // Runnable that will be used to update the size of the box (width = height)
        updateSizeAndSetVisible = new Runnable() {
            @Override
            public void run() {
                // Generate new layout params
                LinearLayout.LayoutParams newParams = (LinearLayout.LayoutParams) pictogramIconContainer.getLayoutParams();
                newParams.height = pictogramIconContainer.getMeasuredWidth();

                // Update the container with new params
                pictogramIconContainer.setLayoutParams(newParams);

                // Now that the height is correct, update the visibility of the component
                inflatedView.setVisibility(VISIBLE);
            }
        };

        // Set the imageEntity (image) for the view (Will be done as an ASyncTask)
        setImageModel(user, fallback);

        // Set the name of pictogram
        titleContainer = (TextView) inflatedView.findViewById(R.id.pictogram_title);
        setTitle(title);

        // Check if any 'custom' attributes are set by the user (for instance in xml)
        if (attrs != null) {
            final TypedArray GirafUserItemViewAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.GirafPictogramItemView);
            isEditable = GirafUserItemViewAttributes.getBoolean(R.styleable.GirafPictogramItemView_editable, false);

            final int drawableId = GirafUserItemViewAttributes.getInt(R.styleable.GirafPictogramItemView_indicatorOverlayDrawable, -1);

            if (drawableId != -1) {
                setIndicatorOverlayDrawable(getContext().getResources().getDrawable(drawableId));
            }

            GirafUserItemViewAttributes.recycle();
        }

        // Code to handle the edit-triangle
        editableIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        editableIndicatorPaint.setStrokeWidth(2);
        editableIndicatorPaint.setColor(Color.LTGRAY);
        editableIndicatorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        editableIndicatorPaint.setAntiAlias(true);
    }

    /**
     * Reset the view (Checked state and imageEntity image)
     */
    public synchronized void resetPictogramView() {
        // Hide the layout until it is loaded correctly
        inflatedView.setVisibility(INVISIBLE);

        // Cancel any currently loading imageEntity tasks
        if (loadPictogramImage != null) {
            loadPictogramImage.cancel(true);
        }

        iconImageView.setImageDrawable(null);
        indicatorOverlayDrawable = null;


        setChecked(false);
    }

    /**
     * Will update the view with the provided imageEntity
     *
     * @param user the imageEntity to update based upon
     */
    public synchronized void setImageModel(final User user) {
        setImageModel(user, null);
    }

    public synchronized void setImageModel(final User user, final boolean useGrayScale) {
        this.useGrayScale = useGrayScale;

        setImageModel(user);
    }

    /**
     * Will update the view with the provided imageEntity. Uses the provided fallback if no image could be loaded
     *
     * @param user the imageEntity to update based upon
     * @param fallback    fallback drawable to use if no image could be loaded
     */
    public synchronized void setImageModel(final User user, final Drawable fallback) {
        // If provided with null, do not update!
        if (user == null) {
            return;
        }

        // Cancel any currently loading imageEntity tasks (This will ensure that we do not try and load two different pictograms)
        if (loadPictogramImage != null) {
            loadPictogramImage.cancel(true);
        }

        // This class will be used to load the imageEntity (image) from the database and "insert" it into the view
        loadPictogramImage = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                final Bitmap image = user.getIcon();
                // Find the imageEntity to show
                // Notice that we create a copy to avoid memory leak (See implementation of getImage on imageEntity)

                // We cant use drawableToBitmap inside a non-UI thread, so instead of "fallback" we now use "alternativeFallback" and hope nothing breaks.
                return image != null ? image : alternativeFallback;
            }
            @Override
            protected void onPostExecute(final Bitmap pictogramImage) {
                iconImageView.setImageBitmap(pictogramImage);
                // Register the runnable and invalidate (so that it will be updated)
                inflatedView.post(updateSizeAndSetVisible);
            }
        };

        // Start loading the image of the imageEntity
        loadPictogramImage.execute();
    }

    public synchronized void setImageModel(final User user, final Drawable fallback, final boolean useGrayScale) {
        this.useGrayScale = useGrayScale;

        setImageModel(user, fallback);
    }




    /**
     * Will convert any drawable into a bitmap
     *
     * @param drawable the drawable to convert
     * @return a bitmap representing the given drawable
     */
    private Bitmap drawableToBitmap(final Drawable drawable) {
        // Check if the input if null. If this is the case we cannot do anything with it! Return the null back to the caller
        if (drawable == null) {
            return null;
        }

        // Check if the input is an instance of BitmapDrawable - this will make the conversion way easier
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // There is no easy way to convert this drawable - Let's do it the hard way
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * Will update the view with the provided title
     *
     * @param title the title to set
     */
    public synchronized void setTitle(final String title) {
        if (title == null) {
            hideTitle();
        } else {
            showTitle();
        }

        titleContainer.setText(title);
    }

    /**
     * Will hide the title of the imageEntity
     */
    public void hideTitle() {
        titleContainer.setVisibility(GONE);
    }

    /**
     * Will show the title of the imageEntity
     */
    public void showTitle() {
        titleContainer.setVisibility(VISIBLE);
    }

    /*
     * Methods and variables used to implement the interface Checkable below:
     */

    // Temp variable which will be used in the methods below
    private boolean checked = false;

    /**
     * Will set the checked state of the imageEntity. This will only change the appearance of the view.
     * True for a selected style, false for normal view.
     *
     * @param checked if true, the view will be updated to look selected/checked
     */
    @Override
    public void setChecked(final boolean checked) {
        // Update the local variable
        this.checked = checked;

        // Update the view
        if (checked) { // The view should appear as selected/checked
            inflatedView.setBackgroundColor(getResources().getColor(R.color.giraf_pictogram_view_background_checked));
        } else { // The view should look regular (no selection)
            inflatedView.setBackgroundColor(getResources().getColor(R.color.giraf_pictogram_view_background_regular));
        }
    }

    /**
     * Returns the checked state of the imageEntity
     *
     * @return true if checked (selected), false is not
     */
    @Override
    public boolean isChecked() {
        return checked;
    }

    /**
     * Will toggle the selected/checked state of the imageEntity
     */
    @Override
    public void toggle() {
        setChecked(!checked);
    }

    /**
     * Set if this GirafUserItemView should draw a small triangle to indicate that it is editable
     *
     * @param editable true if the view should appear as editable (small triangle)
     */
    public void setEditable(final boolean editable) {

        if (editable && this.indicatorOverlayDrawable != null) {
            throw new UnsupportedOperationException("A GirafUserItemView cannot be Editable and have an indicatorOverlayDrawable at the same time");
        }

        this.isEditable = editable;
        this.indicatorOverlayDrawable = null;

        // Make the view redraw on the GUI thread
        postInvalidate();
    }

    private Drawable indicatorOverlayDrawable;

    public void setIndicatorOverlayDrawable(Drawable indicatorOverlayDrawable) {

        if (isEditable && indicatorOverlayDrawable != null) {
            throw new UnsupportedOperationException("A GirafUserItemView cannot have an indicatorOverlayDrawable and be Editable at the same time");
        }

        this.indicatorOverlayDrawable = indicatorOverlayDrawable;
        isEditable = false;

        // Make the view redraw on the GUI thread
        postInvalidate();
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);

        // Get the relative right and bottom coordinate of iconImageView from this GirafUserItemView
        final int[] relativeRightAndBottom = getRelativeRightAndBottom(iconImageView);

        // Use the relativeRightAndBottom as xEnd and yEnd
        final int xEnd = relativeRightAndBottom[0];
        final int yEnd = relativeRightAndBottom[1];

        // Calculate xStart and yStart from end points minus 1/4 of the ImageView width and height
        final int xStart = xEnd - (int) Math.ceil(iconImageView.getMeasuredWidth() / 4.0d);
        final int yStart = yEnd - (int) Math.ceil(iconImageView.getMeasuredHeight() / 4.0d);

        /*
         * Only draw editable-triangle if the view is set to be editable in either xml
         * using the attribute "editable" or using the method setEditable
         */

        if (isEditable) {

            // Set 3 points in a triangle
            point1_draw.set(xEnd, yEnd);
            point2_draw.set(xEnd, yStart);
            point3_draw.set(xStart, yEnd);

            // Configure triangle path
            path.reset();
            path.setFillType(Path.FillType.EVEN_ODD);
            path.moveTo(point1_draw.x, point1_draw.y);
            path.lineTo(point2_draw.x, point2_draw.y);
            path.lineTo(point3_draw.x, point3_draw.y);
            path.lineTo(point1_draw.x, point1_draw.y);
            path.close();

            // Draw triangle
            canvas.drawPath(path, editableIndicatorPaint);

        } else if (indicatorOverlayDrawable != null) {

            // int left, int top, int right, int bottom
            indicatorOverlayDrawable.setBounds(xStart, yStart, xEnd, yEnd);
            indicatorOverlayDrawable.draw(canvas);
        }


    }

    /**
     * Gets the relative bottom right position of @param view relative to this GirafUserItemView
     *
     * @param view
     * @return the relative bottom right position of @param view
     */
    public int[] getRelativeRightAndBottom(final View view) {

        // Get the global top left position of @param view
        view.getLocationInWindow(viewLocation);

        // Get the global top left position of this GirafUserItemView
        this.getLocationInWindow(thisLocation);

        // convert to relative bottom right position and return
        bottomRightLocation[0] = viewLocation[0] - thisLocation[0] + view.getMeasuredWidth();
        bottomRightLocation[1] = viewLocation[1] - thisLocation[1] + view.getMeasuredHeight();
        return bottomRightLocation;
    }
}
