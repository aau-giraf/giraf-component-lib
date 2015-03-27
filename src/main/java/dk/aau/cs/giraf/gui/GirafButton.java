package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dk.aau.cs.giraf.utilities.GirafScalingUtilities;

/**
 * Created on 24/02/15.
 * <p>To use this component in xml remember to set the type of the button:</p>
 * <p><b>Example of an button for camera:</b></p>
 * <pre>{@code
 * <LinearLayout xmlns:app="http://schemas.android.com/apk/res-auto">
 *
 *  <dk.aau.cs.giraf.gui.GirafButton
 *      android:id="@+id/giraf_button"
 *      android:layout_width="wrap_content"
 *      android:layout_height="wrap_content"
 *      app:text="Camera"
 *      app:icon="@drawable/icon_camera" />
 *
 * </LinearLayout>}</pre>
 */
public class GirafButton extends LinearLayout {

    private TextView textView;
    private LinearLayout.LayoutParams textViewParams;

    // The content of the inner view of the GirafButton
    private Drawable icon;
    private String buttonText;

    // Max width and height of the icon
    private final int ICON_MAX_WIDTH = (int) GirafScalingUtilities.convertDpToPixel(this.getContext(), 45);
    private final int ICON_MAX_HEIGHT = (int) GirafScalingUtilities.convertDpToPixel(this.getContext(), 45);

    // The spacings
    private final int SUBVIEW_SPACING = (int) GirafScalingUtilities.convertDpToPixel(this.getContext(), 10);
    private final int BUTTON_PADDING = (int) GirafScalingUtilities.convertDpToPixel(this.getContext(), 10);

    /**
     * Constructs a {@link dk.aau.cs.giraf.gui.GirafButton} without xml only icon
     *
     * @param context the context
     * @param icon    the drawable icon of the {@link dk.aau.cs.giraf.gui.GirafButton}
     */
    public GirafButton(Context context, Drawable icon) {
        this(context, icon, null);
    }

    /**
     * Constructs a {@link dk.aau.cs.giraf.gui.GirafButton} without xml only text
     * @param context the context
     * @param buttonText the text of the button
     */
    public GirafButton(Context context, String buttonText) {
        this(context, null, buttonText);
    }

    /**
     * Constructs a {@link dk.aau.cs.giraf.gui.GirafButton} without xml and text
     *
     * @param context    the context
     * @param icon       the drawable icon of the {@link dk.aau.cs.giraf.gui.GirafButton}
     * @param buttonText the text of the {@link dk.aau.cs.giraf.gui.GirafButton}
     */
    public GirafButton(Context context, Drawable icon, String buttonText) {
        super(context);
        this.icon = icon; // Set the icon to the property
        this.buttonText = buttonText; // Set the text to the property
        initializeButton(null); // Use the dynamic icon from code
    }

    /**
     * Construct a {@link dk.aau.cs.giraf.gui.GirafButton} from xml
     *
     * @param context the context
     * @param attrs   the attributes from the xml-declaration
     */
    public GirafButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeButton(attrs);
    }

    /**
     * Constructs a {@link dk.aau.cs.giraf.gui.GirafButton} from xml
     *
     * @param context  the context
     * @param attrs    the attributes from the xml-declaration
     * @param defStyle the style
     */
    public GirafButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeButton(attrs);
    }

    // Boolean to check if it is the first time onLayout is called
    private boolean firstTimeLayout = true;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        // Check if it is the first time you call onLayout
        if (firstTimeLayout) {

            if (buttonText != null) { // If the button has text

                // If there is an icon set margin accordingly
                if(icon != null) {
                    // Set the margin on the textView
                    textViewParams.setMargins(SUBVIEW_SPACING, 0, 0, 0);
                }

                // Set the textSize of the textView dynamically dependent on the height of the GirafButton
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.getHeight() - (int)(2.5 * BUTTON_PADDING));

                // Remove the padding on the font
                textView.setIncludeFontPadding(false);
            }
            // It is no longer the first time you call onLayout
            firstTimeLayout = !firstTimeLayout;
        }
    }

    /**
     * Initializes the GirafButton
     *
     * @param attrs the attributes from the xml-declaration
     */
    private void initializeButton(AttributeSet attrs) {

        // Set the orientation of the layout
        this.setOrientation(LinearLayout.HORIZONTAL);

        // Set the padding of the button
        this.setPadding(BUTTON_PADDING, BUTTON_PADDING, BUTTON_PADDING, BUTTON_PADDING);
        this.setGravity(Gravity.CENTER);

        // Make instance of the icon (iconView) and the text (textView) of the button
        ImageView iconView = new ImageView(this.getContext());
        textView = new TextView(this.getContext());

        // Adjusts the size after matching the actual size values
        iconView.setAdjustViewBounds(true);

        // Set maximum sizes
        iconView.setMaxWidth(ICON_MAX_WIDTH);
        iconView.setMaxHeight(ICON_MAX_HEIGHT);

        // If attributes given in xml use them if the one given in the constructor is not set
        if (attrs != null) {

            TypedArray girafButtonAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.GirafButton);
            if(icon == null) {
                // Finds the icon from xml
                icon = girafButtonAttributes.getDrawable(R.styleable.GirafButton_icon);
            }

            if(buttonText == null) {
                // Find the attributes from the instance of the GirafButton
                buttonText = girafButtonAttributes.getString(R.styleable.GirafButton_text);
            }

            // Clean attributes from memory
            girafButtonAttributes.recycle();
        }

        if(icon == null && buttonText == null) {
            throw new IllegalArgumentException("A GirafButton must have an icon or some text");
        }

        // If the icon is null remove the iconView
        if(icon == null)
        {
            iconView.setVisibility(View.GONE);
        }
        else {
            iconView.setImageDrawable(icon); // Sets the icon into the ImageView
        }

        // Initialize the textView
        textView.setText(buttonText); // Set the text of the textView
        textView.setGravity(Gravity.CENTER); // Center the textView in the button
        textView.setPadding(0, 0, 0, 0); // Remove padding from the textView

        // Create layout parameters for the textView
        textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // Set the background of the button (with all sates)
        this.setBackgroundResource(R.drawable.giraf_button_background);

        // If the button is disabled make it opaque
        // If you want to change the amount of opacity remember to change it in giraf_button_background too
        if (!this.isEnabled()) {
            iconView.setAlpha(0x59);
        }

        // Add the iconView and the textView to the GirafButton
        this.addView(iconView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.addView(textView, textViewParams);
    }
}
