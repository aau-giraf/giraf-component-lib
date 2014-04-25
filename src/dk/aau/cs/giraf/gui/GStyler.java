package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.content.res.Resources;
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

    private static int baseColor = Color.parseColor("#FFFFD96E");
    //p
    public static int backgroundColor = Color.parseColor("#FFfbfbfb");
    public static int textBaseColor = Color.BLACK;
    public static int buttonBaseColor = Color.parseColor("#FFFFD96E");
    public static int dialogBoxBaseColor = Color.parseColor("#FFFFD96E");
    public static int listBaseColor = Color.parseColor("#FFFFD96E");
    public static int gridBaseColor = Color.parseColor("#FFFFD96E");
    public static int spinnerBaseColor = Color.parseColor("#FFFFD96E");
    public static int toastBaseColor = Color.parseColor("#80000000");
    public static int toastBaseTextColor = Color.parseColor("#FFFFD96E");
    public static int listItemBaseColor = Color.parseColor("#FFFFD96E");
    public static int tooltipBaseColor = Color.parseColor("#FFFFD96E");
    public static int[] dividerBaseColors = { Color.parseColor("#40000000"), Color.parseColor("#25999999") };

    public static int sliderThumbColor = Color.parseColor("#FFFFD96E");
    public static int sliderProgressColor = Color.parseColor("#FFFFD96E");
    public static int sliderUnProgressColor = Color.parseColor("#FFC2C2C2");

    //Dialog base colors
    public static int dialogBackgroundColor = Color.parseColor("#FFfbfbfb");
    public static int dialogBorderColor = calculateGradientColor(Color.parseColor("#FFFFD96E"));
    public static int dialogShadeColor = Color.parseColor("#FF000000");

    public static void SetColors(int color)
    {
        baseColor = color;
        buttonBaseColor = color;
        listBaseColor = color;
        gridBaseColor = color;
        listItemBaseColor = color;
        dialogBackgroundColor = color;
    }

    public static int[] getColors(int color)
    {
        int[] tmp = new int[2];

        tmp[0] = color;
        tmp[1] = calculateGradientColor(color);

        return tmp;
    }

    public static int GetBaseColor()
    {
        return baseColor;
    }

    /**
     * Scales the drawable to match a certain height.
     * @param drawable Drawable to scale.
     * @param length Longest axis to scale the drawable to.
     * @return Scaled drawable.
     */
    public static Drawable scaleDrawable(Drawable drawable, int length, boolean vertical, Resources res){
        Drawable result = drawable;
        Bitmap tempIcon = ((BitmapDrawable)result).getBitmap();

        int maxLength = vertical ? tempIcon.getHeight() : tempIcon.getWidth();

        if (maxLength == 0 || length == 0)
            return result;

        float scale = ((float) length) / ((float)maxLength);

        tempIcon = Bitmap.createScaledBitmap(tempIcon, (int)(tempIcon.getWidth()*scale), (int)(tempIcon.getHeight()*scale), true);
        result = new BitmapDrawable(res, tempIcon);


        return result;
    }

    public static int calculateGradientColor(int color)
    {
        return calculateGradientColor(color, 0.8f);
    }

    public static int ProportionallyAlterVS(int color, float multiplier)
    {
        ColorificationBisimulationRelation HSV = new ColorificationBisimulationRelation(color);
        HSV.SetVal(HSV.GetVal() * multiplier);
        HSV.SetSat(HSV.GetSat() * multiplier);
        return HSV.GetColor();
    }

    public static int InversePropoertionallyAlterVS(int color, float multiplier)
    {
        ColorificationBisimulationRelation HSV = new ColorificationBisimulationRelation(color);
        HSV.SetVal(HSV.GetVal() * multiplier);
        HSV.SetSat(HSV.GetSat() * -multiplier);
        return HSV.GetColor();
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

        if (red > 0xFF0000) red = 0xFF0000;
        if (green > 0xFF00) green = 0xFF00;
        if (blue > 0xFF)    blue = 0xFF;

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
