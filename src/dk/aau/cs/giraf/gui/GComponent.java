package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import java.util.HashMap;

import dk.aau.cs.giraf.oasis.lib.models.Profile;
import dk.aau.cs.giraf.oasis.lib.models.Setting;

public class GComponent {

    private ColorificationBisimulationRelation c = new ColorificationBisimulationRelation(GStyler.baseColor);

    public static void SetBaseColor(int color)
    {
        GStyler.SetColors(color);
    }

    public static int GetBackgroundColor(){
        return GStyler.backgroundColor;
    }

    public static int DpToPixel(int dp, Context context)
    {
        return GStyler.dpToPixel(dp, context);
    }

    public static GradientDrawable GetBackgroundGradient()
    {
        return new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] { GStyler.baseColor, GStyler.InversePropoertionallyAlterVS(GStyler.baseColor, 0.75f) });
    }

}
