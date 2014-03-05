/**
 * 
 */
package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Color;
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

    /**
     * Styles the button according to the giraf standards.
     * Theme support pending.
     */
	private void setStyle(AttributeSet attrs) {
		this.setBackgroundResource(R.drawable.gbutton);

        String test = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableLeft");

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
        this.setText(test);

        int[] colors = new int[2];
        colors[0] = colorStart;
        colors[1] = colorEnd;

        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        this.setBackgroundDrawable(gd);
	}
	
	/**
	 * @param context
	 */
	public GButton(Context context) {
		super(context);
		// TODO Auto-generated constructor
		//this.setStyle();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public GButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.setStyle(attrs);

	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public GButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.setStyle(attrs);
	}

}
