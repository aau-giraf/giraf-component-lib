package dk.aau.cs.giraf.gui;

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

}
