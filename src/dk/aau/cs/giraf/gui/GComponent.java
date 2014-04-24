package dk.aau.cs.giraf.gui;

import android.content.Context;

import java.util.HashMap;

import dk.aau.cs.giraf.oasis.lib.models.Profile;
import dk.aau.cs.giraf.oasis.lib.models.Setting;

public class GComponent {


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

}
