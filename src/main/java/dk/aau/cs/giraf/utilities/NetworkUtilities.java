package dk.aau.cs.giraf.utilities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class contains relevant content for network related to giraf
 */

public abstract class NetworkUtilities {

    /**
     * Checks whether or not the device is connected to the internet
     * Implements sophisticated techniques to detect if a connection may be dead i.e. available but not connected to the internet
     *
     * @param thisActivity references the activity, used to get connectivity manager
     * @return true or false based on the connection status
     */
    public static boolean isNetworkAvailable(Activity thisActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) thisActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            //Now you might be connected to a wifi, but still have no internet access.
            //How do we find out if that is the case? Ping google hack to the rescue
            Runtime runtime = Runtime.getRuntime();
            try {
                //Should ping the giraf db, but no one can give me url *sadface*
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int     exitValue = ipProcess.waitFor();
                return (exitValue == 0);
            } catch (Exception e)  { e.printStackTrace(); }
        }
        return false;
    }
}
