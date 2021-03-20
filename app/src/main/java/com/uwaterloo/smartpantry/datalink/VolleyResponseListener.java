package com.uwaterloo.smartpantry.datalink;



import com.android.volley.error.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

public interface VolleyResponseListener {
    public void onSuccess(JSONArray response, String type);
    public void onSuccess(JSONObject response, String type);
    public void onFailure(VolleyError error);
}
