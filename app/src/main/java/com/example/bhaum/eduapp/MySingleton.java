package cfilt.iitb.ac.in.hindishabdamitra.hindishabdamitra;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by CFILT on 3/7/2018.
 */

public class MySingleton {
    private static MySingleton instance;
    private RequestQueue requestQueue;
    private Context context;

    private MySingleton(Context context) {
        this.context = context;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static MySingleton getInstance(Context context) {
        if (instance == null)
            instance = new MySingleton(context);

        return instance;
    }
}
