package dk.aau.cs.giraf.gui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Malakahh on 3/7/14.
 */
class GStyler {
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
}
