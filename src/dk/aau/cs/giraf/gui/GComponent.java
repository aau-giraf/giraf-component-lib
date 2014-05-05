package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;


public class GComponent {

    public enum Background{
        SOLID,
        GRADIENT,
        GRADIENT_INVERSE,
        SUBTLEGRADIENT
    }

    public static void SetBaseColor(int color)
    {
        GStyler.baseColor                 =
            GStyler.buttonBaseColor       =
            GStyler.dialogBoxBaseColor    =
            GStyler.listBaseColor         =
            GStyler.toastBaseTextColor    =
            GStyler.gridBaseColor         =
            GStyler.listItemBaseColor     =
            GStyler.tooltipBaseColor      =
            GStyler.spinnerBaseColor      =
            GStyler.sliderThumbColor      =
            GStyler.sliderProgressColor   = color;

        GStyler.backgroundColor = ColorificationBisimulationRelation.InversePropoertionallyAlterVS(0.9f);
        GStyler.dialogBorderColor = ColorificationBisimulationRelation.InversePropoertionallyAlterVS(0.8f);
        GStyler.backgroundColor = ColorificationBisimulationRelation.InversePropoertionallyAlterVS(0.2f);
        GStyler.dialogBackgroundColor = ColorificationBisimulationRelation.ProportionallyAlterVS(0.8f);

    }

    public static int GetTextColor()
    {
        return GStyler.textBaseColor;
    }

    public static int GetBackgroundColor(){
        return GStyler.backgroundColor;
    }

    public static Drawable GetBackground(Background type)
    {
        int startColor = ColorificationBisimulationRelation.SetAndGetColor(0.6f);
        switch (type){
            case SOLID:
                return new ColorDrawable(GStyler.backgroundColor);
            case GRADIENT:
                return new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] {
                        startColor,
                        ColorificationBisimulationRelation.InversePropoertionallyAlterVS(1.1f)
                });
            case GRADIENT_INVERSE:
                return new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] {
                        ColorificationBisimulationRelation.InversePropoertionallyAlterVS(1.1f),
                        startColor
                });
            default:
                return new ColorDrawable(GStyler.backgroundColor);
        }
    }

    public static int DpToPixel(int dp, Context context)
    {
        return GStyler.dpToPixel(dp, context);
    }

    public static GradientDrawable GetBackgroundGradient()
    {
        return new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] { GStyler.baseColor, ColorificationBisimulationRelation.InversePropoertionallyAlterVS(0.75f) });
    }

}
