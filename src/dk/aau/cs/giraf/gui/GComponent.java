package dk.aau.cs.giraf.gui;

import java.util.HashMap;

import dk.aau.cs.giraf.oasis.lib.models.Profile;
import dk.aau.cs.giraf.oasis.lib.models.Setting;

/*
 * The main interface between any app and GComponents.
 * The most important usage of this class is to make sure that GComponent has been initialized.
 *
 * WARNING: THIS CLASS DOES NOT WORK - NOR DOES ANY OF THE FUNCTIONALITY WORK.
*/
public class GComponent {

    private static boolean isInitialized = false;

    /***
     *
     * @param profile the profile of the currently logged in user
     * @param appId the application ID of the app using GComponents
     * @throws Exception
     * Will initialize the GComponents which is basically setting the base color for the framework.
     * If GComponent is not initialized, it will crash.
     */
    public static void Initialize(Profile profile, int appId) throws Exception{
        Setting settings;
        int backgroundColor = -1;

        //If the appID is not what we expect, or the profile is not correctly set, die.
        if(appId > -1 || profile == null)
        {
            throw new Exception("GComponent must be initialized before use. If you have not done this read on the wiki how to do so. <insert link>");
        }

        //The hash table containing the base color is accessed through the Setting helper class.
        settings = profile.getSettings();

        // If settings for the given app exists.
        if (settings != null && settings.containsKey(String.valueOf(appId))) {
            HashMap<String, String> appSettings = (HashMap<String, String>)settings.get(String.valueOf(appId));

        //If the hash table contains the key, get it.
        if(appSettings != null && appSettings.containsKey("backgroundColor"))
            backgroundColor = Integer.parseInt((appSettings.get("backgroundColor")));

            if(backgroundColor > -1)
                GStyler.SetBaseColor(backgroundColor);
            else
                throw new Exception("GComponent could not initialize because the background color for the app was not valid");

            isInitialized = true;
        }
        else
        {
            throw new Exception("GComponent could not initialize because profile.getSettings() was corrupted");
        }

    }


    public static boolean IsInitialized() throws Exception
    {
        if(!isInitialized)
            throw new Exception("GComponent must be initialized before use. If you have not done this read on the wiki how to do so. <insert link>");

        return true;
    }
}
