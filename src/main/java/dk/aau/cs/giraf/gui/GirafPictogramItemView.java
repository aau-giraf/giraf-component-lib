package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.BasicImageModel;
import dk.aau.cs.giraf.oasis.lib.models.Pictogram;

import static java.lang.Math.max;

/**
 * Created on 14/04/2015.
 */
public class GirafPictogramItemView extends LinearLayout implements Checkable {

    // The imageModel to base the view upon
    private BasicImageModel imageModel;

    // The inflated view (See constructors)
    private View inflatedView;

    private ImageView iconContainer;
    private TextView titleContainer;

    private AsyncTask<Void, Void, Bitmap> loadPictogramImage;

    /**
     * Do not use this constructor. It is only available for creating the imageModel in xml!
     */
    public GirafPictogramItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            initialize(null, null);
            return;
        }

        Pictogram sample = new Pictogram();
        sample.setName("Sample imageModel");
        sample.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_copy));
        initialize(sample, sample.getName());
    }

    //<editor-fold desc="constructors">
    public GirafPictogramItemView(Context context, BasicImageModel imageModel) {
        this(context, imageModel, null);
    }

    public GirafPictogramItemView(Context context, AttributeSet attrs, BasicImageModel imageModel) {
        this(context, attrs, imageModel, null);
    }

    public GirafPictogramItemView(Context context, AttributeSet attrs, int defStyle, BasicImageModel imageModel) {
        this(context, attrs, defStyle, imageModel, null);
    }

    public GirafPictogramItemView(Context context, BasicImageModel imageModel, String title) {
        super(context);

        initialize(imageModel, title);
    }

    public GirafPictogramItemView(Context context, AttributeSet attrs, BasicImageModel imageModel, String title) {
        super(context, attrs);

        initialize(imageModel, title);
    }

    public GirafPictogramItemView(Context context, AttributeSet attrs, int defStyle, BasicImageModel imageModel, String title) {
        super(context, attrs, defStyle);

        initialize(imageModel, title);
    }
    //</editor-fold>

    /**
     * Initialized the different components
     */
    private void initialize(BasicImageModel imageModel, String title) {
        // Find the XML for the imageModel and load it into the view
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflatedView = inflater.inflate(R.layout.giraf_pictogram, this);

        // Set the imageModel (image) for the view (Will be done as an ASyncTask)
        iconContainer = (ImageView) inflatedView.findViewById(R.id.pictogram_icon);
        setImageModel(imageModel);

        // Set the name of pictogram
        titleContainer = (TextView) inflatedView.findViewById(R.id.pictogram_title);
        setTitle(title);

        // Force the container to be square (height = width)
        final LinearLayout container = (LinearLayout) findViewById(R.id.pictogram_icon_container);
        container.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams newParams;
                newParams = (LinearLayout.LayoutParams) container.getLayoutParams();
                newParams.height = container.getWidth();
                container.setLayoutParams(newParams);
                container.postInvalidate();
            }
        });
        container.postInvalidate();
    }

    /**
     * Reset the view (Checked state and imageModel image)
     */
    public synchronized void resetPictogramView() {
        iconContainer.setImageBitmap(null);
        setChecked(false);
    }

    /**
     * Will update the view with the provided imageModel
     *
     * @param imageModel the imageModel to update based upon
     */
    public synchronized void setImageModel(final BasicImageModel imageModel) {

        // If provided with null, do not update!
        if (imageModel == null) {
            return;
        }

        this.imageModel = imageModel;

        // Cancel any currently loading imageModel tasks (This will ensure that we do not try and load two different pictograms)
        if (loadPictogramImage != null) {
            loadPictogramImage.cancel(true);
            loadPictogramImage = null;
        }

        // This class will be used to load the imageModel (image) from the database and "insert" it into the view
        loadPictogramImage = new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                final Helper helper = new Helper(getContext());

                /**
                 * We create a temporary reference to a imageModel because the current model objects saves a permanent reference to
                 * their bitmaps once they are loaded once.
                 * This creates memory overflows because we keep a list of all imageModel objects in memory
                 */
                BasicImageModel b = (BasicImageModel) clone(imageModel);

                // Check if the temp could not be found (This means that no bitmap could be found)
                if (b == null) {
                    return null;
                }

                // Find the imageModel to show
                // Notice that we create a copy to avoid memory leak (See implementation of getImage on imageModel)
                return b.getImage();
            }

            @Override
            protected void onPostExecute(final Bitmap pictogramImage) {
                iconContainer.setImageBitmap(pictogramImage);
            }

            // This method will be used to clone the the BasicImageModel to avoid memory leak. See doInBackground
            public Object clone(Object o) {
                Object clone = null;

                try {
                    clone = o.getClass().newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                // Walk up the superclass hierarchy
                for (Class obj = o.getClass();
                     !obj.equals(Object.class);
                     obj = obj.getSuperclass()) {
                    Field[] fields = obj.getDeclaredFields();
                    for (int i = 0; i < fields.length; i++) {
                        fields[i].setAccessible(true);
                        try {
                            // for each class/superclass, copy all fields
                            // from this object to the clone
                            fields[i].set(clone, fields[i].get(o));
                        } catch (IllegalArgumentException e) {
                        } catch (IllegalAccessException e) {
                        }
                    }
                }
                return clone;
            }
        };

        // Start loading the image of the imageModel
        loadPictogramImage.execute();
    }

    /**
     * Will update the view with the provided title
     *
     * @param title the title to set
     */
    public synchronized void setTitle(final String title) {
        if(title == null) {
            hideTitle();
        }
        else {
            showTitle();
        }

        titleContainer.setText(title);
    }

    /**
     * Will hide the title of the imageModel
     */
    public void hideTitle() {
        titleContainer.setVisibility(GONE);
    }

    /**
     * Will show the title of the imageModel
     */
    public void showTitle() {
        titleContainer.setVisibility(VISIBLE);
    }

    /*
     * Methods and variables used to implement the interface Checkable below:
     */

    private boolean checked = false;

    /**
     * Will set the checked state of the imageModel. This will only change the appearance of the view.
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
     * Returns the checked state of the imageModel
     *
     * @return true if checked (selected), false is not
     */
    @Override
    public boolean isChecked() {
        return checked;
    }

    /**
     * Will toggle the selected/checked state of the imageModel
     */
    @Override
    public void toggle() {
        setChecked(!checked);
    }
}
