package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

/**
 * Contains helper functions which are used by the GComponent GUI Library.
 */
class GStyler {

    /**
     * Default colors
     */
    public static int baseColor             = 0xFFFFD96E;
    public static int buttonBaseColor       = 0xFFFFD96E;
    public static int dialogBoxBaseColor    = 0xFFFFD96E;
    public static int listBaseColor         = 0xFFFFD96E;
    public static int gridBaseColor         = 0xFFFFD96E;
    public static int toastBaseTextColor    = 0xFFFFD96E;
    public static int listItemBaseColor     = 0xFFFFD96E;
    public static int tooltipBaseColor      = 0xFFFFD96E;
    public static int spinnerBaseColor      = 0xFFFFD96E;
    public static int sliderThumbColor      = 0xFFFFD96E;
    public static int sliderProgressColor   = 0xFFFFD96E;

    public static int backgroundColor       = 0xFFFFEAA1;
    public static int textBaseColor         = 0xFF000000; // Not changed
    public static int toastBaseColor        = 0x80000000; // Not changed
    public static int dialogBorderColor     = ColorificationBisimulationRelation.InversePropoertionallyAlterVS(0.8f);
    public static int[] dividerBaseColors   = { 0x40000000, 0x25999999 }; // May need to be changed to be dynamic, currently is not
    public static int sliderUnProgressColor = 0xFFC2C2C2; // Fixed
    public static int dialogBackgroundColor = ColorificationBisimulationRelation.ProportionallyAlterVS(0.8f);
    public static int dialogShadeColor = 0xFF000000;



    //Dialog base colors




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
    @Deprecated
    public static int calculateGradientColor(int color)
    {
        return calculateGradientColor(color, 0.8f);
    }

    @Deprecated
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

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int color, int cornerDips, int borderDips, Resources res) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int borderSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) borderDips*2,
                res.getDisplayMetrics());
        final int cornerSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) cornerDips,
                res.getDisplayMetrics());
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        // prepare canvas for transfer
        paint.setAntiAlias(true);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

        // draw bitmap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        // draw border
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) borderSizePx);
        canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

        return output;
    }

}
