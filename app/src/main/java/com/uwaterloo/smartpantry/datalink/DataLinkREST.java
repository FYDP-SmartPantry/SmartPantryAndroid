package com.uwaterloo.smartpantry.datalink;

import android.util.Pair;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.couchbase.lite.internal.utils.StringUtils;
import com.uwaterloo.smartpantry.data.UserInfo;
import com.uwaterloo.smartpantry.inventory.GroceryItem;
import com.uwaterloo.smartpantry.user.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataLinkREST {
    private DataLinkREST() {};
    private static String base_url = "http://18.224.136.80:5000/";
    private static String food_url = "https://api.spoonacular.com/";
    private static String predict_url = "http://18.224.136.80:5000/predict";
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

    public static void GetPrediction(String imagePath, VolleyResponseListener volleyResponseListener) {
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, predict_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            volleyResponseListener.onSuccess(jsonObject, "Predict");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                    volleyResponseListener.onFailure(error);
                }
        });
        smr.addFile("file", imagePath);
        smr.setRetryPolicy(new DefaultRetryPolicy(
           8000, 2, 0
        ));
        smr.setShouldCache(false);
        DataLink.getInstance().addToRequestQueue(smr);
    }

    public static void SearchRecipes(ArrayList<String> filteredIngredients, ArrayList<String> filteredType, ArrayList<String> filteredCuisine, ArrayList<String> filteredDiet, VolleyResponseListener volleyResponseListener) {
        String url = food_url + "recipes/complexSearch?apiKey=" + api_key;

        String ingr = String.join(",", filteredIngredients);
        String type = String.join(",", filteredType);
        String cuis = String.join(",", filteredCuisine);
        String diet = String.join(",", filteredDiet);

        url = StringUtils.isEmpty(ingr) ? url: url + "&includeIngredients=" + ingr;
        url = StringUtils.isEmpty(type) ? url: url + "&type=" + type;
        url = StringUtils.isEmpty(cuis) ? url: url + "&cuisine=" + cuis;
        url = StringUtils.isEmpty(diet) ? url: url + "&diet=" + diet;

        JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
        DataLink.getInstance().addToRequestQueue(json);
    }

    public static void GetRandomRecipes(VolleyResponseListener volleyResponseListener) {
        String url = food_url + "recipes/random?apiKey=" + api_key + "&number=15";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        volleyResponseListener.onSuccess(response, "RandomRecommendation");
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
        jsonObjectRequest.setShouldCache(false);
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

        jsonArrayRequest.setShouldCache(false);
        DataLink.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    public static void GetRecipeInstructions(int recipeId, VolleyResponseListener volleyResponseListener) {
        String url = food_url + "recipes/" + recipeId + "/information?apiKey=" + api_key;

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
        shoppingList.add(new GroceryItem("blueberries",3.0, "lbs"));
        shoppingList.add(new GroceryItem("rhubarb", 2.0, "lbs"));
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
        jsonObjectRequest.setShouldCache(false);
        DataLink.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    // TODO: Add date handling and bulk recipes if necessary
    public static void AddMeal(List<Pair<Integer, String>> recipes, String timestamp, UserInfo userInfo, VolleyResponseListener volleyResponseListener) {
        String username = userInfo.getUsername();
        String hash = userInfo.getHash();

        String url = food_url + "mealplanner/" + username + "/items?apiKey=" + api_key + "&hash=" + hash;
        JSONArray bodyParameters = new JSONArray();
        JSONObject bodyItem = new JSONObject();
        for (int i = 0; i < recipes.size(); i++) {
            int recipeId = recipes.get(i).first;
            String recipeTitle = recipes.get(i).second;

            try {
                bodyItem.put("date", timestamp);
                bodyItem.put("slot", 1);
                bodyItem.put("position", "0");
                bodyItem.put("type", "RECIPE");

                JSONObject value = new JSONObject();
                value.put("id", recipeId);
                value.put("title", recipeTitle);
                value.put("servings", "1");
                bodyItem.put("value", value);
                bodyParameters.put(bodyItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String requestBody = bodyParameters.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println(response);
                volleyResponseListener.onSuccess(new JSONObject(), "MealPlanAdd");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onFailure(error);
            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        stringRequest.setShouldCache(false);
        DataLink.getInstance().addToRequestQueue(stringRequest);
    }

    public static void GetMealPlan(String date, UserInfo userInfo, VolleyResponseListener volleyResponseListener) {
        String username = userInfo.getUsername();
        String hash = userInfo.getHash();

        // Date in yyyy-MM-dd
        String url = food_url + "mealplanner/" + username + "/week/" + date + "?hash=" + hash + "&apiKey=" + api_key;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                volleyResponseListener.onSuccess(response, "GetMealPlan");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onFailure(error);
            }
        });

        jsonObjectRequest.setShouldCache(false);
        DataLink.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public static void RemoveMeal(String itemId, UserInfo userInfo, VolleyResponseListener volleyResponseListener) {
        String username = userInfo.getUsername();
        String hash = userInfo.getHash();

        String url = food_url + "mealplanner/" + username + "/items/" + itemId + "?apiKey=" + api_key + "&hash=" + hash;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        volleyResponseListener.onSuccess(response, "MealPlanItemDelete");
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
        jsonObjectRequest.setShouldCache(false);
        DataLink.getInstance().addToRequestQueue(jsonObjectRequest);
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
