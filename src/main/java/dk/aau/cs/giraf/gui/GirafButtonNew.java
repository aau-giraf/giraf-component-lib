package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dk.aau.cs.giraf.utilities.GirafScalingUtilities;

/**
 * Created on 11/05/2015.
 */
public class GirafButtonNew extends LinearLayout implements Checkable {

    private final int BUTTON_PADDING = (int) GirafScalingUtilities.convertDpToPixel(this.getContext(), 10);
    private final int SUBVIEW_SPACING = (int) GirafScalingUtilities.convertDpToPixel(this.getContext(), 10);

    // Views used in the layout
    private TextView textView;
    private ImageView iconView;
    private View inflatedView;

    // The content of the button itself
    private Drawable buttonIcon;
    private String buttonText;
    private boolean buttonIsEnabled;
    private boolean buttonScaleText = true;

    // Distances, padding and margins
    private float buttonPadding;

    // Used in the checkable methods to indicate the checked status of the button
    private boolean isChecked = false;

    // Event listeners that will be used
    private OnClickListener onDisabledClickCallBack;

    // Boolean to check if it is the first time onLayout is called
    private boolean firstTimeLayout = true;

    //private LinearLayout.LayoutParams textViewParams;

    /*
     * Constructors to be used from XML
     */

    public GirafButtonNew(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        // If attributes given in xml use them if the one given in the constructor is not set
        if (attrs != null) {
            // TODO: Add comment
            TypedArray girafButtonAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.GirafButton);

            // Find the icon and text attributes
            buttonIcon = girafButtonAttributes.getDrawable(R.styleable.GirafButton_icon);
            buttonText = girafButtonAttributes.getString(R.styleable.GirafButton_text);
            buttonIsEnabled = girafButtonAttributes.getBoolean(R.styleable.GirafButton_enabled, true);
            setButtonScaleText(girafButtonAttributes.getBoolean(R.styleable.GirafButton_scale_text, true));

            // Recycle the attributes
            girafButtonAttributes.recycle();
        } else {
            throw new IllegalArgumentException("Please provide properties for your button");
        }

        if (this.isInEditMode() && (buttonText == null || TextUtils.isEmpty(buttonText))) {
            buttonText = "Placeholder text";
        }

        // Initialize the button
        initialize(context, buttonIcon, buttonText);
    }

    /*
     * Constructors to be used in java code
     */

    public GirafButtonNew(final Context context, final Drawable icon) {
        this(context, icon, null);
    }

    public GirafButtonNew(final Context context, final String text) {
        this(context, text, null);
    }

    public GirafButtonNew(final Context context, final String text, final Drawable icon) {
        this(context, icon, text);
    }

    public GirafButtonNew(final Context context, final Drawable icon, final String text) {
        super(context);

        // Set the local properties
        this.buttonText = text;
        this.buttonIcon = icon;

        // Initialize the button
        initialize(context, icon, text);
    }

    private void initialize(final Context context, final Drawable icon, final String text) {
        // Either icon or text must be specified
        if (icon == null && (text == null || text.equals(""))) {
            throw new IllegalArgumentException("You must specify either icon or text on a GirafButton");
        }

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflatedView = inflater.inflate(R.layout.giraf_button, this);
        //this.addView(inflatedView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Find views used in the layout
        textView = (TextView) this.findViewById(R.id.text_view);
        iconView = (ImageView) this.findViewById(R.id.icon_view);

        // Set the text and icon in the view
        setText(text);
        setIcon(icon);
        setEnabled(buttonIsEnabled);

        // Find the dimens used
        buttonPadding = context.getResources().getDimension(R.dimen.giraf_button_padding);

        updateMargin();
    }

    //<editor-fold desc="hide and show text and icon methods">

    public void setText(String text) {
        if (text == null || text.equals("")) {
            hideText();
        } else {
            showText();
        }

        textView.setText(text);

        textView.layout(10, 10, 10, 10);
    }

    public void hideText() {
        if (textView.getVisibility() == GONE) return;

        textView.setVisibility(GONE);
        updateMargin();
    }

    public void showText() {
        if (textView.getVisibility() == VISIBLE) return;

        textView.setVisibility(VISIBLE);
        updateMargin();
    }

    public void setIcon(Drawable icon) {
        if (icon == null) {
            hideIcon();
        } else {
            showIcon();
        }

        iconView.setImageDrawable(icon);
    }

    public void hideIcon() {
        if (iconView.getVisibility() == GONE) return;

        iconView.setVisibility(GONE);
        updateMargin();
    }

    public void showIcon() {
        if (iconView.getVisibility() == VISIBLE) return;

        iconView.setVisibility(VISIBLE);
        updateMargin();
    }

    //</editor-fold>

    public synchronized boolean getButtonScaleText() {
        return buttonScaleText;
    }

    public synchronized void setButtonScaleText(final boolean buttonScaleText) {
        this.buttonScaleText = buttonScaleText;

        if (!buttonScaleText) {
            textView.setIncludeFontPadding(true);
        }

        // Invalidate view (redraw text)
        postInvalidate();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        //firstTimeLayout = true;
    }

    @Override
    protected synchronized void onLayout(final boolean changed, final int l, final int t, final int r, final int b) {
        super.onLayout(changed, l, t, r, b);

        // Check if it is the first time you call onLayout
        if (firstTimeLayout) {

            if (buttonText != null) { // If the button has text

                // If there is an icon set margin accordingly
                if (buttonIcon != null) {
                    // Set the margin on the textView
                    // TODO: FIX ///textViewParams.setMargins(SUBVIEW_SPACING, 0, 0, 0);
                }

                // Remove the padding on the font
                textView.setIncludeFontPadding(false);

                // Set the textSize of the textView dynamically of the height button
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.getMeasuredHeight() - (int) (2.5 * BUTTON_PADDING));
                Paint paint = new Paint();
                paint.setTextSize(this.getMeasuredHeight() - (int) (2.5 * BUTTON_PADDING));

                //(int) paint.measureText(textView.getText().toString())
            }
            textView.setLayoutParams(new LayoutParams( 10000, 10000));
            // It is no longer the first time you call onLayout
            firstTimeLayout = !firstTimeLayout;
            textView.forceLayout();
            textView.invalidate();
            textView.setBackgroundColor(Color.RED);
            requestLayout();
        }
        else
        {
            textView.setBackgroundColor(Color.BLUE);
        }
    }

    private void updateMargin() {
        if (iconView.getVisibility() == VISIBLE && textView.getVisibility() == VISIBLE) {
            // Reset margin on text
            LayoutParams textViewParams = (LayoutParams) textView.getLayoutParams();
            textViewParams.setMargins((int) buttonPadding, 0, 0, 0);
            textView.setLayoutParams(textViewParams);
        } else {
            // Set margin on text
            LayoutParams textViewParams = (LayoutParams) textView.getLayoutParams();
            textViewParams.setMargins(0, 0, 0, 0);
            textView.setLayoutParams(textViewParams);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (!enabled) {
            // This is 0x59 in hex (35% opaque)
            iconView.setAlpha(0.65F);
            textView.setAlpha(0.65F);
            this.setBackgroundResource(R.drawable.giraf_button_background_disabled);
        } else {
            // Fully opaque
            iconView.setAlpha(1F);
            textView.setAlpha(1F);
            this.setBackgroundResource(R.drawable.giraf_button_background);
        }
    }

    @Override
    public final boolean onTouchEvent(final MotionEvent event) {
        // If the button is disabled catch MotionEvent.ACTION_DOWN and invoke onDisabledClickCallBack
        if (!this.isEnabled() && onDisabledClickCallBack != null && event.getAction() == MotionEvent.ACTION_DOWN) {
            onDisabledClickCallBack.onClick(this);
            return true;
        }

        return super.onTouchEvent(event);
    }

    /**
     * Set a listener which is invoked when the button is disabled and clicked on
     *
     * @param listener
     */
    public void setOnDisabledClickListener(final OnClickListener listener) {
        this.onDisabledClickCallBack = listener;
    }

    /**
     * Set if the button should be checked or not
     *
     * @param checked is the newly checked state of the button
     */
    public void setChecked(boolean checked) {
        // Check if it is already in the correct checked-states
        if (checked == isChecked) {
            return;
        }

        // Set the background to look "pressed" if checked is true otherwise set the default background
        if (checked) {
            this.setBackgroundResource(R.drawable.giraf_button_background_checked);
        } else {
            this.setBackgroundResource(R.drawable.giraf_button_background);
        }

        // Set the internal boolean
        isChecked = checked;
    }

    /**
     * Get the checked-states
     *
     * @return true of it is checked else false
     */
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * Toggles if the button is checked
     */
    public void toggle() {
        setChecked(!isChecked);
    }
}
