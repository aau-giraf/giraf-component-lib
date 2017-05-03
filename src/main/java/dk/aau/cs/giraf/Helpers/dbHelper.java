package dk.aau.cs.giraf.Helpers;

import dk.aau.cs.giraf.librest.requests.DeleteRequest;
import dk.aau.cs.giraf.librest.requests.GetRequest;
import dk.aau.cs.giraf.librest.requests.LoginRequest;
import dk.aau.cs.giraf.librest.requests.PostRequest;
import dk.aau.cs.giraf.librest.requests.PutRequest;
import dk.aau.cs.giraf.librest.requests.RequestQueueHandler;
import dk.aau.cs.giraf.librest.serialization.Translator;
import dk.aau.cs.giraf.models.core.AccessLevel;
import dk.aau.cs.giraf.models.core.Department;
import dk.aau.cs.giraf.models.core.Pictogram;
import dk.aau.cs.giraf.models.core.User;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.VolleyError;

/**
 * Created by Track on 02-05-2017.
 */

public class dbHelper {

    public dbHelper(){

    }

    public Object getFromDB() {
        GetRequest<Pictogram> genGetRequest = new GetRequest<Pictogram>(18, Pictogram.class,
            new Response.Listener<Pictogram>() {
                @Override
                public void onResponse(Pictogram response) {
                    return
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
        );

    }

}
