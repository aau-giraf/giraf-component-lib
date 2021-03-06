package dk.aau.cs.giraf.utilities;

import android.app.Activity;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import dk.aau.cs.giraf.librest.requests.GetRequest;
import dk.aau.cs.giraf.librest.requests.LoginRequest;
import dk.aau.cs.giraf.librest.requests.RequestQueueHandler;
import dk.aau.cs.giraf.models.core.User;

import static android.view.View.LAYER_TYPE_HARDWARE;
import static android.view.View.LAYER_TYPE_NONE;

public class GrayScaleHelper {

    public static void setGrayScaleForActivity(Activity activity, boolean shouldBeGray) {
        //Get the root view
        View view = activity.getWindow().getDecorView().getRootView();
        setGray(view, shouldBeGray);
    }

    private static void setGray(View view, boolean state) {
        if (state) {
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0); //Set the color saturation to 0
            Paint grayscalePaint = new Paint();
            grayscalePaint.setColorFilter(new ColorMatrixColorFilter(cm));
            // Create a hardware layer with the greyscale paint
            view.setLayerType(LAYER_TYPE_HARDWARE, grayscalePaint);
        } else {
            //Removes the grayscale
            view.setLayerType(LAYER_TYPE_NONE, null);
        }
    }

    /**
     * Requests a updated user from the server to get the grayscale boolean and uses it.
     * @param activity the activity for which it is called.
     * @param user the user that are logged in.
     */
    public static void setGrayScaleForActivityByUser(final Activity activity, final User user) {
        final RequestQueue queue = RequestQueueHandler.getInstance(activity.getApplicationContext()).getRequestQueue();
        GetRequest<User> userGetRequest = new GetRequest<User>(user.getUsername(), User.class, new Response.Listener<User>() {
            @Override
            public void onResponse(User response) {
                if(response.getSettings() != null) {
                    setGrayScaleForActivity(activity, response.getSettings().getUseGrayScale());
                }
                else{ Log.e("LAUNCHER","Settings nullpointer");}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == 401) {
                    LoginRequest loginRequest = new LoginRequest(user, new Response.Listener<Integer>() {
                        @Override
                        public void onResponse(Integer response) {
                            GetRequest<User> userGetRequest = new GetRequest<User>(user.getUsername(), User.class, new Response.Listener<User>() {
                                @Override
                                public void onResponse(User response) {
                                    setGrayScaleForActivity(activity, response.getSettings().getUseGrayScale());
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error.networkResponse.statusCode == 401) {
                                        Log.e("Giraf-component", "User has not permission for settings grayscale");
                                    }
                                }
                            });
                            queue.add(userGetRequest);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Giraf-component", "Network error in Grayscale");
                        }
                    });
                    queue.add(loginRequest);
                } else {
                    Log.e("Giraf-component", "Network error in Grayscale");
                }
            }
        });
        queue.add(userGetRequest);
    }
}
