/**
 * 
 */
package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.StateSet;
import android.widget.Button;

public class GButton extends Button {

    private Drawable buttonImage;
    private boolean isScaled = false;
    private float dpScale;


    /**
     * @param context
     */
    public GButton(Context context) {
        super(context);
        // TODO Auto-generated constructor
        this.setStyle();
    }

    /**
     * @param context
     * @param attrs
     */
    public GButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.setStyle();
        //thatID = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "drawableLeft", 0);

    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public GButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        this.setStyle();
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom)
    {
        //Idea of method:
        //          Avoid setting the image in this method, only store it. Actual setting is done in onDraw().
        //Reason:
        //          Default XML behavior undesirable, we needed to scale the image before it is sat, to avoid button
        //          scaling when sat to wrap-content.

        //Save drawable for future use
        if (left != null) buttonImage = left;
        else if (top != null) buttonImage = top;
        else if (right != null) buttonImage = right;
        else if (bottom != null) buttonImage = bottom;

        isScaled = false;
    }

    public void onDraw(Canvas c)
    {
        super.onDraw(c);

        //Sets the button image.
        if (buttonImage != null)
        {
            //Scales drawable to match size of button
            if (!isScaled)
            {
                buttonImage = GStyler.scaleDrawable(buttonImage, this.getHeight() - this.getPaddingBottom() - this.getPaddingTop());
                isScaled = true;
            }

            super.setCompoundDrawablesWithIntrinsicBounds(buttonImage, null, null, null);
        }
    }

    /**
     * Converts dp to pixels for device indepedency
     * @param dpInput number of dp
     * @return number of pixels
     */
    private int dpToPixel(int dpInput)
    {
        return (int) (dpInput * dpScale  + 0.5f);
    }

    /**
     * Styles the button according to the giraf standards.
     * Theme support pending.
     */
    private void setStyle() {
        dpScale = getContext().getResources().getDisplayMetrics().density;

        //default colors
        this.setTextColor(Color.BLACK);
        int colorStart = Color.parseColor("#FFFFD96E");

        //this will be the backrounddrawable
        StateListDrawable stateListDrawable = new StateListDrawable();

        //default colors
        int[] colors = new int[2];
        colors[0] = colorStart;
        colors[1] = GStyler.calculateGradientColor(colorStart);

        //colors when pressed
        int[] colorsPressed = new int[2];
        colorsPressed[0] = colors[1];
        colorsPressed[1] = GStyler.calculateGradientColor(colorsPressed[0]);

        //make the two gradients
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        GradientDrawable gdPressed = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colorsPressed);

        //round cornors and give edges
        gd.setCornerRadius(10);
        gd.setStroke(3, GStyler.calculateGradientColor(colors[0], 0.75f));
        gdPressed.setCornerRadius(10);
        gdPressed.setStroke(3, GStyler.calculateGradientColor(colorsPressed[0], 0.75f));

        //set state_pressed to gdPressed and all others to gd
        stateListDrawable.addState(new int[] {android.R.attr.state_pressed}, gdPressed);
        stateListDrawable.addState(StateSet.WILD_CARD, gd);

        this.setPadding(dpToPixel(20),dpToPixel(10),dpToPixel(20),dpToPixel(10));
        this.setCompoundDrawablePadding(dpToPixel(5));

        this.setBackgroundDrawable(stateListDrawable);
    }


}
