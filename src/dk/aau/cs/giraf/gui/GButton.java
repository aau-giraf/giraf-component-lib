/**
 * 
 */
package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author thomaskobberpanum aka asshole
 *
 */
public class GButton extends Button {

    private static final float shadingMultiplier = 1.2f;
    private Drawable buttonImage;
    private int test = 0;


    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom)
    {
        if (left != null) buttonImage = left;
        else if (top != null) buttonImage = top;
        else if (right != null) buttonImage = right;
        else if (bottom != null) buttonImage = bottom;
    }

    public void onDraw(Canvas c)
    {
        super.onDraw(c);
        buttonImage = resizeIcon(buttonImage, this.getHeight() - this.getPaddingBottom() - this.getPaddingTop());
        super.setCompoundDrawablesWithIntrinsicBounds(buttonImage, null, null, null);
    }
    /**
     * Styles the button according to the giraf standards.
     * Theme support pending.
     */
	private void setStyle() {
		this.setBackgroundResource(R.drawable.gbutton);

        //default colors
        this.setTextColor(Color.parseColor("#9E6435"));
        int colorStart = Color.parseColor("#FFFFD96E");

        //collect the RGB values from the hex color code via bit-shifting
        int red = colorStart & (0xFF << 16);
        int green = colorStart & (0xFF << 8);
        int blue = colorStart & 0xFF;

        //darken the color by the shadingMultiplier coefficient
        red = (int)(red/shadingMultiplier);
        green = (int)(green/shadingMultiplier);
        blue = (int)(blue/shadingMultiplier);

        //re-assemble into new color
        //bitwise AND is there to ensure that no data is spilling from red and green
        int colorEnd = 0xFF000000 |
                red & 0xFF0000 |
                green & 0xFF00 |
                blue;

        int[] colors = new int[2];
        colors[0] = colorStart;
        colors[1] = colorEnd;

        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        this.setBackgroundDrawable(gd);
	}

    private Drawable resizeIcon(Drawable img, int height){
        Drawable result = img;
        Bitmap tempIcon = ((BitmapDrawable)result).getBitmap();

        final int oldWidth = tempIcon.getWidth();
        final int oldHeight = tempIcon.getHeight();

        float scale = ((float) height) / ((float)tempIcon.getHeight());

        tempIcon = Bitmap.createScaledBitmap(tempIcon, (int)(tempIcon.getWidth()*scale), (int)(tempIcon.getHeight()*scale), true);
        result = new BitmapDrawable(tempIcon);

        return result;
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
