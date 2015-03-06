package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

    private ImageView imageView;

    // Sets the max width and height of the button
    private final int ICON_MAX_WIDTH = (int) convertDpToPixel(this.getContext(), 45);
    private final int ICON_MAX_HEIGHT = (int) convertDpToPixel(this.getContext(), 45);


    public GirafButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeButton(attrs);
    }

    public GirafButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeButton(attrs);
    }

    // Flag to ensure that onLayout is only called once
    private boolean onFirstLayout = true;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if(onFirstLayout) {
            this.getLayoutParams().width = ICON_MAX_WIDTH + this.getPaddingBottom() + this.getPaddingTop();
            this.getLayoutParams().height = ICON_MAX_HEIGHT + this.getPaddingLeft() + this.getPaddingRight();
            onFirstLayout = !onFirstLayout;
        }
    }

    /**
     * Initializes the GirafButton
     * @param attrs the attributes from the xml-declaration
     */
    private void initializeButton(AttributeSet attrs) {

        // Set the orientation of the layout
        this.setOrientation(LinearLayout.HORIZONTAL);

        // Make instance of the icon (imageView) and the text (textView) of the button
        imageView = new ImageView(this.getContext(), attrs);

        TypedArray girafButtonAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.GirafButton);

        // Find the attributes from the instance of the GirafButton
        Drawable icon = girafButtonAttributes.getDrawable(R.styleable.GirafButton_icon); // Finds the icon

        imageView.setImageDrawable(icon); // Sets the icon into the ImageView

        // Set the background of the button (with all sates)
        this.setBackgroundResource(R.drawable.giraf_button_background);

        // If the button is disabled make it opaque
        // If you want to change the amount of opacity remember to change it in giraf_button_background too
        if(!this.isEnabled()) {
            imageView.setAlpha(0x59);
        }

        // Add the icon of the button
        this.addView(imageView);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(Context context, float dp){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }
}
