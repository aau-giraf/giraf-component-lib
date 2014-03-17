package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Contains helper functions which are used by the GComponent GUI Library.
 */
class GStyler {

    /**Colors is are currently static.
     *Future sprint would involve making them dynamic from a database
     **/
    public static int buttonBaseColor = Color.parseColor("#FFEEEEEE");
    public static int dialogBoxBaseColor = Color.parseColor("#FFEEEEEE");
    public static int listBaseColor = Color.parseColor("#FFEEEEEE");
    public static int baseAlphaBackground = Color.parseColor("#80000000");
    public static int listItemBaseColor = Color.parseColor("#FFEEEEEE");

    public static int[] getColors(int color)
    {
        int[] tmp = new int[2];

        tmp[0] = color;
        tmp[1] = calculateGradientColor(color);

        return tmp;
    }

    /**
     * Scales the drawable to match a certain height.
     * @param drawable Drawable to scale.
     * @param height Height to scale the drawable to.
     * @return Scaled drawable.
     */
    public static Drawable scaleDrawable(Drawable drawable, int height){
        Drawable result = drawable;
        Bitmap tempIcon = ((BitmapDrawable)result).getBitmap();

        float scale = ((float) height) / ((float)tempIcon.getHeight());

        tempIcon = Bitmap.createScaledBitmap(tempIcon, (int)(tempIcon.getWidth()*scale), (int)(tempIcon.getHeight()*scale), true);
        result = new BitmapDrawable(tempIcon);

        return result;
    }

    public static int calculateGradientColor(int color)
    {
        return calculateGradientColor(color, 0.8f);
    }

    public static int calculateGradientColor(int color, float shadingMultiplier)
    {
        //collect the RGB values from the hex color code via bit-shifting
        int red = color & 0xFF0000;
        int green = color & 0xFF00;
        int blue = color & 0xFF;

        //darken the color by the shadingMultiplier coefficient
        red = (int)(red*shadingMultiplier);
        green = (int)(green*shadingMultiplier);
        blue = (int)(blue*shadingMultiplier);

        //re-assemble into new color
        //bitwise AND is there to ensure that no data is spilling from red and green
        int colorRes = 0xFF000000 |
                red & 0xFF0000 |
                green & 0xFF00 |
                blue;

        return colorRes;
    }

    /**
     * Converts dp to pixels for device indepedency
     * @param dpInput number of dp
     * @param context the current context
     * @return number of pixels
     */
    public static int dpToPixel(int dpInput, Context context)
    {
        return (int) (dpInput * context.getResources().getDisplayMetrics().density  + 0.5f);
    }

}
