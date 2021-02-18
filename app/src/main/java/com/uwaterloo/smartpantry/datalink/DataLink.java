package com.uwaterloo.smartpantry.datalink;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

/*
* TODO: I still think this is better to be achieved through gRPC instead of RESTFUL implementation. Since this is not required on the Spec, it can be signatured for future research
* */
public class DataLink {
    private static DataLink instance;
    private RequestQueue requestQueue;
    private static Context ctx;
    private boolean isInitialized = false;

    private DataLink() {};

    public static synchronized DataLink getInstance() {
        if (instance == null) {
            instance = new DataLink();
        }
        return instance;
    }

    public void initDataLink(Context context) {
        ctx = context;
        isInitialized = true;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T>  void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public boolean isDataLinkInitialized() {
        return isInitialized;
    }
}
