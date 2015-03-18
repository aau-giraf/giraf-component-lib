package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import dk.aau.cs.giraf.utilities.GirafScalingUtilities;

/**
 * Created on 24/02/15.
 * <p>To use this component in xml remember to set the type of the button:</p>
 * <p><b>Example of an button for camera:</b></p>
 * <pre>{@code
 *  <dk.aau.cs.giraf.gui.GirafButton
 *      android:id="@+id/giraf_button"
 *      android:layout_width="wrap_content"
 *      android:layout_height="wrap_content"
 *      app:icon="@drawable/icon_camera" />}</pre>
 */
public class GirafButton extends LinearLayout {

    private ImageView iconView;
    private Drawable icon;

    // Sets the max width and height of the button
    private final int ICON_MAX_WIDTH = (int) GirafScalingUtilities.convertDpToPixel(this.getContext(), 45);
    private final int ICON_MAX_HEIGHT = (int) GirafScalingUtilities.convertDpToPixel(this.getContext(), 45);

    public GirafButton(Context context, Drawable icon) {
        super(context);
        this.icon = icon; // Set the icon to the property
        initializeButton(null); // Use the dynamic icon from code
    }

    public GirafButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeButton(attrs);
    }

    public GirafButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeButton(attrs);
    }

    /**
     * Initializes the GirafButton
     * @param attrs the attributes from the xml-declaration
     */
    private void initializeButton(AttributeSet attrs) {

        // Set the orientation of the layout
        this.setOrientation(LinearLayout.HORIZONTAL);

        // Make instance of the icon (iconView) and the text (textView) of the button
        iconView = new ImageView(this.getContext());
        // Adjusts the size after matching the actual size values
        iconView.setAdjustViewBounds(true);

        // Set maximum sizes
        iconView.setMaxWidth(ICON_MAX_WIDTH);
        iconView.setMaxHeight(ICON_MAX_HEIGHT);

        Drawable icon; // Declare the drawable of the iconView


        // If attributes given in xml use them otherwise use the one given in the constructor
        if(attrs != null) {
            TypedArray girafButtonAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.GirafButton);

            // Find the attributes from the instance of the GirafButton
            icon = girafButtonAttributes.getDrawable(R.styleable.GirafButton_icon); // Finds the icon
        } else {
            icon = this.icon;
        }

        iconView.setImageDrawable(icon); // Sets the icon into the ImageView

        // Set the background of the button (with all sates)
        this.setBackgroundResource(R.drawable.giraf_button_background);

        // If the button is disabled make it opaque
        // If you want to change the amount of opacity remember to change it in giraf_button_background too
        if(!this.isEnabled()) {
            iconView.setAlpha(0x59);
        }

        // Add the icon of the button
        this.addView(iconView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

}
