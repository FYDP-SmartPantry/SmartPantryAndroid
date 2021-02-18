package com.uwaterloo.smartpantry;
import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.uwaterloo.smartpantry.datalink.DataLink;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DataLinkInstrumentedTest {

    @Test
    public void SimpleRequestTest() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        DataLink.getInstance().initDataLink(appContext);
        String url ="http://3.18.111.90:5000/";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        DataLink.getInstance().addToRequestQueue(stringRequest);
    }

    //@Test
    public void jsonRequestTest() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        DataLink.getInstance().initDataLink(appContext);
        RequestQueue queue = DataLink.getInstance().getRequestQueue();
        String url ="http://3.18.111.90:5000/item";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String cat = response.getString("Category");
                            System.out.println(cat);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("no found");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println(error.getMessage());
                    }
                });

        DataLink.getInstance().addToRequestQueue(jsonObjectRequest);

    }
}
