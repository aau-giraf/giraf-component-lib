/**
 * 
 */
package dk.aau.cs.giraf.gui;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GButton extends Button {
    private enum Location{
        LEFT, TOP, RIGHT, BOTTOM
    }

    private Location buttonImageLocation;
    private Drawable buttonImage;
    private boolean isScaled = false;
    protected Drawable stylePressed;
    protected Drawable styleUnPressed;
    private boolean hasDrawnStroke = false;


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

    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter)
    {
        if (buttonImage != null)
        {
            if (lengthAfter == 0 && lengthBefore != 0)
                isScaled = false;
            else if (lengthAfter != 0 && lengthBefore == 0)
                isScaled = false;
        }
    }

    public void onDraw(Canvas c)
    {
        super.onDraw(c);

        if (!hasDrawnStroke)
        {

            int padding = 1;
            int myColor = GStyler.InversePropoertionallyAlterVS(GStyler.buttonBaseColor, 0.75f);

            Bitmap bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
            Bitmap bitmap2 = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canviassjavicans = new Canvas(bitmap);
            Canvas c2 = new Canvas(bitmap2);

            //stylePressed.draw(canviassjavicans);

            ColorDrawable gd = new ColorDrawable(Color.MAGENTA);
            gd.draw(canviassjavicans);
            canviassjavicans.save();
            stylePressed = new BitmapDrawable(getResources(), bitmap);//GStyler.getRoundedCornerBitmap(bitmap, myColor, 10, padding, getResources()));

            styleUnPressed.draw(c2);
            styleUnPressed = new BitmapDrawable(getResources(), GStyler.getRoundedCornerBitmap(bitmap2, myColor, 10, padding, getResources()));

            Log.e("test", stylePressed.toString());

/*
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[] {android.R.attr.state_pressed}, bD);
            stateListDrawable.addState(StateSet.WILD_CARD, bDU);
*/

            //this.setBackgroundDrawable(stateListDrawable);

            hasDrawnStroke = true;
        }

        //Sets the button image.
        if (buttonImage != null)
        {

            //Scales drawable to match size of button
            if (!isScaled)
            {
                Drawable buttonImageTemp;
                int scaleWidth = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
                int scaleHeight = this.getHeight() - this.getPaddingBottom() - this.getPaddingTop();

                if (this.getText().length() > 0) //in this case, scale on the logical axis
                {

                    if (buttonImageLocation == Location.LEFT || buttonImageLocation == Location.RIGHT)
                    {
                        buttonImageTemp = GStyler.scaleDrawable(buttonImage.mutate().getConstantState().newDrawable(),
                                                                        scaleHeight, true, getResources());
                    }
                    else //top or bottom
                        buttonImageTemp = GStyler.scaleDrawable(buttonImage.mutate().getConstantState().newDrawable(), scaleWidth, false, getResources());

                    switch(buttonImageLocation){
                        case LEFT:
                            super.setCompoundDrawablesWithIntrinsicBounds(buttonImageTemp, null, null, null);
                            break;
                        case TOP:
                            super.setCompoundDrawablesWithIntrinsicBounds(null, buttonImageTemp, null, null);
                            break;
                        case RIGHT:
                            super.setCompoundDrawablesWithIntrinsicBounds(null, null, buttonImageTemp, null);
                            break;
                        default: //BOTTOM
                            super.setCompoundDrawablesWithIntrinsicBounds(null, null, null, buttonImageTemp);
                            break;
                    }
                }
                else //no text, they want to center the image, simply scale to the shortest axis
                {
                    ViewGroup.LayoutParams layoutParams = this.getLayoutParams();

                    if (layoutParams.height == layoutParams.WRAP_CONTENT && layoutParams.width == layoutParams.WRAP_CONTENT)
                    {
                        //no text but wrap size to icon
                        //this is bad because the icon will scale to fit nothing basically
                        //instead scale to size, had there been text
                        buttonImageTemp = GStyler.scaleDrawable(buttonImage.mutate().getConstantState().newDrawable(),
                                (int)(this.getTextSize() * 2 + 0.5f) + this.getPaddingBottom() + this.getPaddingTop() + this.getCompoundDrawablePadding(), true, getResources());
                        //I have literally no idea why I have to multiply getTextSize() by 2, but the numbers apparently add up

                        super.setCompoundDrawablesWithIntrinsicBounds(buttonImageTemp, null, null, null);
                    }
                    else
                    {
                        if (scaleWidth < scaleHeight)
                        {
                            buttonImageTemp = GStyler.scaleDrawable(buttonImage.mutate().getConstantState().newDrawable(),
                                    scaleWidth, false, getResources());
                            super.setCompoundDrawablesWithIntrinsicBounds(buttonImageTemp, null, null, null);
                        }
                        else
                        {
                            buttonImageTemp = GStyler.scaleDrawable(buttonImage.mutate().getConstantState().newDrawable(),
                                    scaleHeight, true, getResources());
                            super.setCompoundDrawablesWithIntrinsicBounds(null, buttonImageTemp, null, null);
                        }
                    }
                }
                isScaled = true;
            }


        }

    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


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
        GradientDrawable gdU = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        GradientDrawable gdP = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colorsPressed);

        //round corners and give edges
        gdU.setCornerRadius(10);
        //styleUnPressed.setStroke(1, GStyler.calculateGradientColor(colors[0], 0.75f));
        gdU.setStroke(2, GStyler.ProportionallyAlterVS(colors[0], 1.5f));
        gdP.setCornerRadius(10);
        gdP.setStroke(2, GStyler.ProportionallyAlterVS(colors[0], 1.5f));

        stylePressed = gdP;
        styleUnPressed = gdU;

        //set state_pressed to gdPressed and all others to gd
        stateListDrawable.addState(new int[] {android.R.attr.state_pressed}, stylePressed);
        stateListDrawable.addState(StateSet.WILD_CARD, styleUnPressed);

        this.setPadding(GStyler.dpToPixel(10, this.getContext())
                ,GStyler.dpToPixel(5, this.getContext())
                ,GStyler.dpToPixel(10, this.getContext())
                ,GStyler.dpToPixel(5, this.getContext()));

        this.setCompoundDrawablePadding(GStyler.dpToPixel(3, this.getContext()));

        //this.setBackgroundDrawable(stateListDrawable);
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
