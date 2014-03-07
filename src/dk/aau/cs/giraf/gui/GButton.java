/**
 * 
 */
package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.Button;

public class GButton extends Button {

    private Drawable buttonImage;
    private boolean isScaled = false;

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
     * Styles the button according to the giraf standards.
     * Theme support pending.
     */
    private void setStyle() {
        //this.setBackgroundResource(R.drawable.gbutton);

        //default colors
        this.setTextColor(Color.parseColor("#9E6435"));
        //int colorStart = Color.parseColor("#FFFFD96E");
        int colorStart = Color.parseColor("#FFFFFFFF");

        int[] colors = new int[2];
        colors[0] = colorStart;
        colors[1] = GStyler.calculateGradientColor(colorStart);

        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);

        gd.setCornerRadius(10);
        gd.setStroke(3, GStyler.calculateGradientColor(colorStart, 0.75f));

        this.setPadding(20,10,20,10);

        this.setBackgroundDrawable(gd);
    }
    
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

}
