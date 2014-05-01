package dk.aau.cs.giraf.gui;

import android.graphics.Color;

public class ColorificationBisimulationRelation {
    private static float[] HSV = new float[3];

    public static int ProportionallyAlterVS(float multiplier)
    {
        ResetColor();
        SetVal(GetVal() * multiplier);
        SetSat(GetSat() * multiplier);
        return Color.HSVToColor(HSV);
    }

    public static int InversePropoertionallyAlterVS(float multiplier)
    {
        ResetColor();
        float diff = multiplier - 1.f;
        SetVal(GetVal() * 1.f + diff);
        SetSat(GetSat() * 1.f - diff);
        return Color.HSVToColor(HSV);
    }

    private static void ResetColor()
    {
        Color.colorToHSV(GStyler.baseColor, HSV);
    }

    private static float GetHue()
    {
        return HSV[0];
    }

    private static void SetHue(float hue)
    {
        HSV[0] = hue;
    }

    private static float GetSat()
    {
        return HSV[1];
    }

    private static void SetSat(float sat)
    {
        HSV[1] = sat;
    }

    private static float GetVal()
    {
        return HSV[2];
    }

    private static void SetVal(float val)
    {
        HSV[2] = val;
    }
}
