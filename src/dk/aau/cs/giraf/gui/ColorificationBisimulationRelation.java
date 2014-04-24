package dk.aau.cs.giraf.gui;

import android.graphics.Color;

/**
 * Created by Malakahh on 4/24/14.
 */
public class ColorificationBisimulationRelation {
    float[] HSV = new float[3];
    public ColorificationBisimulationRelation(int color)
    {
        Color.colorToHSV(color, HSV);
    }

    public int GetColor()
    {
        return Color.HSVToColor(HSV);
    }

    public float GetHue()
    {
        return HSV[0];
    }

    public void SetHue(float hue)
    {
        HSV[0] = hue;
    }

    public float GetSat()
    {
        return HSV[1];
    }

    public void SetSat(float sat)
    {
        HSV[1] = sat;
    }

    public float GetVal()
    {
        return HSV[2];
    }

    public void SetVal(float val)
    {
        HSV[2] = val;
    }
}
