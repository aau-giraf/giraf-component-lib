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

        GStyler.dialogBorderColor = ColorificationBisimulationRelation.InversePropoertionallyAlterVS(0.8f);
        GStyler.backgroundColor = ColorificationBisimulationRelation.InversePropoertionallyAlterVS(0.2f);
        GStyler.dialogBackgroundColor = ColorificationBisimulationRelation.ProportionallyAlterVS(0.8f);

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

    public static GradientDrawable GetBackgroundGradient()
    {
        return new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] { GStyler.baseColor, ColorificationBisimulationRelation.InversePropoertionallyAlterVS(0.75f) });
    }

}
