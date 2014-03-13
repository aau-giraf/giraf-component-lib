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

    private enum Location{
        LEFT, TOP, RIGHT, BOTTOM
    }

    private Location buttonImageLocation;
    private Drawable buttonImage;
    private boolean isScaled = false;



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
        if (left != null)
        {
            buttonImage = left;
            buttonImageLocation = Location.LEFT;
        }
        else if (top != null)
        {
            buttonImage = top;
            buttonImageLocation = Location.TOP;
        }
        else if (right != null)
        {
            buttonImage = right;
            buttonImageLocation = Location.RIGHT;
        }
        else if (bottom != null)
        {
            buttonImage = bottom;
            buttonImageLocation = Location.BOTTOM;
        }

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
                if (buttonImageLocation == Location.LEFT || buttonImageLocation == Location.RIGHT)
                {
                    buttonImage = GStyler.scaleDrawable(buttonImage, this.getHeight() - this.getPaddingBottom() - this.getPaddingTop());
                }
                else //top or bottom
                    buttonImage = GStyler.scaleDrawable(buttonImage, this.getWidth() - this.getPaddingLeft() - this.getPaddingRight());
                isScaled = true;
            }

            switch(buttonImageLocation){
                case LEFT:
                    super.setCompoundDrawablesWithIntrinsicBounds(buttonImage, null, null, null);
                    this.setText("Left");
                    break;
                case TOP:
                    super.setCompoundDrawablesWithIntrinsicBounds(null, buttonImage, null, null);
                    this.setText("Top");
                    break;
                case RIGHT:
                    super.setCompoundDrawablesWithIntrinsicBounds(null, null, buttonImage, null);
                    this.setText("Right");
                    break;
                default: //BOTTOM
                    super.setCompoundDrawablesWithIntrinsicBounds(null, null, null, buttonImage);
                    this.setText("Bot");
                    break;
            }
        }
    }

    /**
     * Styles the button according to the giraf standards.
     * Theme support pending.
     */
    private void setStyle() {
        //default colors
        this.setTextColor(Color.BLACK);

        //this will be the backrounddrawable
        StateListDrawable stateListDrawable = new StateListDrawable();

        //default colors
        int[] colors = GStyler.getColors(GStyler.buttonBaseColor);

        //colors when pressed
        int[] colorsPressed = new int[2];
        colorsPressed[0] = colors[1];
        colorsPressed[1] = GStyler.calculateGradientColor(colorsPressed[0]);

        //make the two gradients
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        GradientDrawable gdPressed = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colorsPressed);

        //round corners and give edges
        gd.setCornerRadius(10);
        gd.setStroke(3, GStyler.calculateGradientColor(colors[0], 0.75f));
        gdPressed.setCornerRadius(10);
        gdPressed.setStroke(3, GStyler.calculateGradientColor(colorsPressed[0], 0.75f));

        //set state_pressed to gdPressed and all others to gd
        stateListDrawable.addState(new int[] {android.R.attr.state_pressed}, gdPressed);
        stateListDrawable.addState(StateSet.WILD_CARD, gd);

        this.setPadding(GStyler.dpToPixel(20, this.getContext())
                ,GStyler.dpToPixel(10, this.getContext())
                ,GStyler.dpToPixel(20, this.getContext())
                ,GStyler.dpToPixel(10, this.getContext()));

        this.setCompoundDrawablePadding(GStyler.dpToPixel(5, this.getContext()));

        this.setBackgroundDrawable(stateListDrawable);
    }

    /**
	 * Sets the image of on the button.
	 * @param image The new image to use.
	 */
    public void SetImage(Drawable image)
    {
    	this.buttonImage = image;
    }
  }
