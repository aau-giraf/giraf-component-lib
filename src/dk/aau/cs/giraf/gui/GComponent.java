package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import java.util.HashMap;

import dk.aau.cs.giraf.oasis.lib.models.Profile;
import dk.aau.cs.giraf.oasis.lib.models.Setting;

public class GComponent {

    public enum Background{
        SOLID,
        GRADIENT,
        SUBTLEGRADIENT
    }

    public static void SetBaseColor(int color)
    {
        GStyler.SetColors(color);
    }

    public static int GetBackgroundColor(){
        return GStyler.backgroundColor;
    }

    public static Drawable GetBackground(Background type)
    {
        switch (type){
            case SOLID:
                return new ColorDrawable(GStyler.backgroundColor);
            case GRADIENT:
                return new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] {
                        Color.parseColor("#FFffe062"),
                        Color.parseColor("#FFebbc39")
                });
            default:
                return new ColorDrawable(GStyler.backgroundColor);
        }
    }

    public static int DpToPixel(int dp, Context context)
    {
        return GStyler.dpToPixel(dp, context);
    }

}
