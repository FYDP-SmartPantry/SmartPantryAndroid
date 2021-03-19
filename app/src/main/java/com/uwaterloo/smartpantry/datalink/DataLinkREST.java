package com.uwaterloo.smartpantry.datalink;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.uwaterloo.smartpantry.data.UserInfo;
import com.uwaterloo.smartpantry.inventory.GroceryItem;
import com.uwaterloo.smartpantry.inventory.ShoppingList;
import com.uwaterloo.smartpantry.user.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataLinkREST {
    private DataLinkREST() {};
    private static String base_url = "http://3.18.111.90:5000/";
    private static String food_url = "https://api.spoonacular.com/";
    // TODO: Secure this key later on
    private static String api_key = "5119238a378a45f3aedb5f4e984fc071";

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
    public static void GetRecipeRecommendation(String food, VolleyResponseListener volleyResponseListener) {
//        String url = base_url + "/users/{user_id}/shopping";
        String url = food_url + "recipes/findByIngredients?apiKey=" + api_key + "&ingredients=" + food;
//        JSONObject userInfo = new JSONObject();

        // pack user based info to jsonobject
        //userinfo.put()

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //volleyResponseListener.onSuccess(response, "Recommendation");
                        volleyResponseListener.onSuccess(response, "Recommendation");
                        System.out.println(response);
                        // Display the first 500 characters of the response string.
                        // usually we do not care about the return for POST commend
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onFailure(error);
                System.out.println(error.getMessage());
            }
        });
        DataLink.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    public static void GetRecipeInstructions(int recipeId, VolleyResponseListener volleyResponseListener) {
//        String url = base_url + "/users/{user_id}/shopping";
        String url = food_url + "recipes/" + recipeId + "/information?apiKey=" + api_key;
//        JSONObject userInfo = new JSONObject();

        // pack user based info to jsonobject
        //userinfo.put()

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        volleyResponseListener.onSuccess(response, "RecipeInformation");
                        System.out.println(response);
                        // Display the first 500 characters of the response string.
                        // usually we do not care about the return for POST commend
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onFailure(error);
                System.out.println(error.getMessage());
            }
        });
        DataLink.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public static List<GroceryItem> GetMockShoppingList() {
        List<GroceryItem> shoppingList = new ArrayList<>();
        shoppingList.add(new GroceryItem("blueberries", 222));
        shoppingList.add(new GroceryItem("rhubarb", 10));
        return shoppingList;
    }

    public static void SetupMealPlan(String username, VolleyResponseListener volleyResponseListener) {
        String url = food_url + "users/connect?apiKey=" + api_key;
        JSONObject bodyParameters = new JSONObject();
        try {
            bodyParameters.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, bodyParameters,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                volleyResponseListener.onSuccess(response, "MealPlanRegister");
                System.out.println(response);
                // Display the first 500 characters of the response string.
                // usually we do not care about the return for POST commend
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onFailure(error);
                System.out.println(error.getMessage());
            }
        });
    }

    // TODO: Add date handling and bulk recipes if necessary
    public static void AddMeal(String recipeId, String timestamp, String slot, UserInfo userInfo, VolleyResponseListener volleyResponseListener) {
        String username = userInfo.getUsername();
        String hash = userInfo.getHash();

        String url = food_url + "mealplanner/" + username + "/items?apiKey=" + api_key + "&hash=" + hash;
        JSONObject bodyParameters = new JSONObject();
        try {
            bodyParameters.put("date", timestamp);
            bodyParameters.put("slot", slot);
            // TODO: May have to revise position
            bodyParameters.put("position", "0");
            bodyParameters.put("type", "recipe");

            JSONObject value = new JSONObject();
            value.put("id", recipeId);
            value.put("servings", "1");
            bodyParameters.put("value", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, bodyParameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        volleyResponseListener.onSuccess(response, "MealPlanAdd");
                        System.out.println(response);
                        // Display the first 500 characters of the response string.
                        // usually we do not care about the return for POST commend
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onFailure(error);
                System.out.println(error.getMessage());
            }
        });
    }

    public static void RemoveMeal(String itemId, UserInfo userInfo, VolleyResponseListener volleyResponseListener) {
        String username = userInfo.getUsername();
        String hash = userInfo.getHash();

        String url = food_url + "mealplanner/" + username + "/items/" + itemId + "?apiKey=" + api_key + "&hash=" + hash;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        volleyResponseListener.onSuccess(response, "MealPlanDelete");
                        System.out.println(response);
                        // Display the first 500 characters of the response string.
                        // usually we do not care about the return for POST commend
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onFailure(error);
                System.out.println(error.getMessage());
            }
        });
    }

    public static void GenerateShoppingList(String startDate, String endDate, UserInfo userInfo,
                                            VolleyResponseListener volleyResponseListener) {
        String username = userInfo.getUsername();
        String hash = userInfo.getHash();
        String url = food_url + "mealplanner/" + username + "/shopping-list/"
                + startDate + "/" +  endDate + "?apiKey=" + api_key + "&hash=" + hash;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        volleyResponseListener.onSuccess(response, "MealPlanShoppingList");
                        System.out.println(response);
                        // Display the first 500 characters of the response string.
                        // usually we do not care about the return for POST commend
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onFailure(error);
                System.out.println(error.getMessage());
            }
        });
    }

    public static void GetSimilarRecipes(String recipeId, VolleyResponseListener volleyResponseListener) {
        String url = food_url + "recipes/" + recipeId + "/similar?apiKey=" + api_key;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        volleyResponseListener.onSuccess(response, "SimilarRecipe");
                        System.out.println(response);
                        // Display the first 500 characters of the response string.
                        // usually we do not care about the return for POST commend
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onFailure(error);
                System.out.println(error.getMessage());
            }
        });
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
