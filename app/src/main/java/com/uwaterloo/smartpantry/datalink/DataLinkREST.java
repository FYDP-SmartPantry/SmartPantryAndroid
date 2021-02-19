package com.uwaterloo.smartpantry.datalink;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.uwaterloo.smartpantry.user.User;

import org.json.JSONArray;
import org.json.JSONObject;

public class DataLinkREST {
    private DataLinkREST() {};
    private static String base_url = "http://3.18.111.90:5000/";

    public static void CreateUser(User user) {
        String url = base_url + "users/create";
        JSONObject userInfo = new JSONObject();

        // pack user based info to jsonobject
        //userinfo.put()

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, userInfo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        // usually we do not care about the return for POST commend
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                    }
        });
        DataLink.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    // get shopping list recommendation for current user
    public static void GetShoppingList(User user) {
        String url = base_url + "/users/{user_id}/shopping";
        JSONObject userInfo = new JSONObject();

        // pack user based info to jsonobject
        //userinfo.put()

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.
                        // usually we do not care about the return for POST commend
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                    }
        });
        DataLink.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    // generate recipe based on foodlist
    public static void GetRecipeRecommendation(JSONArray foodList) {
        String url = base_url + "/users/{user_id}/shopping";
        JSONObject userInfo = new JSONObject();

        // pack user based info to jsonobject
        //userinfo.put()

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.
                        // usually we do not care about the return for POST commend
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        DataLink.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    // TODO: to be designed
    public static void GetNutrition(User user) {
        String url = base_url + "/users/{user_id}/shopping";
        JSONObject userInfo = new JSONObject();

        // pack user based info to jsonobject
        //userinfo.put()

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.
                        // usually we do not care about the return for POST commend
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        DataLink.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    // Get User stats from last cycle
    public static void GetUserStats(User user) {
        String url = base_url + "/users/{user_id}/shopping";
        JSONObject userInfo = new JSONObject();

        // pack user based info to jsonobject
        //userinfo.put()

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.
                        // usually we do not care about the return for POST commend
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        DataLink.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    // if user is not on local, we need to pull that user info from cloud. For demo this is probably not needed
    public static void GetInventory(User user) {
        String url = base_url + "/users/{user_id}/shopping";
        JSONObject userInfo = new JSONObject();

        // pack user based info to jsonobject
        //userinfo.put()

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.
                        // usually we do not care about the return for POST commend
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        DataLink.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    // Post Inventory to Cloud
    public static void PostInventory(User user, JSONArray inventory) {
        String url = base_url + "/users/{user_id}/shopping";
        JSONObject userInfo = new JSONObject();

        // pack user based info to jsonobject
        //userinfo.put()

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.
                        // usually we do not care about the return for POST commend
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        DataLink.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    // once user commit a recipe, we upload it to cloud
    // TODO: to be designed.
    public static void PostRecipe(User user, JSONObject recipe) {
        String url = base_url + "/users/{user_id}/shopping";
        JSONObject userInfo = new JSONObject();

        // pack user based info to jsonobject
        //userinfo.put()

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.
                        // usually we do not care about the return for POST commend
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        DataLink.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
