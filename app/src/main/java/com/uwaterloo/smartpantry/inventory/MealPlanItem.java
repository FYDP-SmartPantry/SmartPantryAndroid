package com.uwaterloo.smartpantry.inventory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MealPlanItem {
    private String id;
    private String recipeId;
    private String title;

    public MealPlanItem(String id, String recipeId, String title){
        this.id = id;
        this.recipeId = recipeId;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static List<MealPlanItem> parseItems(JSONObject obj) {
        List<MealPlanItem> mealPlanItemsList = new ArrayList<MealPlanItem>();
        try {
            JSONArray days = obj.getJSONArray("days");
            for (int i = 0; i < days.length(); i++) {
                JSONObject day = days.getJSONObject(i);
                JSONArray items = day.getJSONArray("items");
                for (int j = 0; j < items.length(); j++) {
                    JSONObject item = items.getJSONObject(j);
                    String id = item.getString("id");
                    String recipeId = item.getJSONObject("value").getString("id");
                    String title = item.getJSONObject("value").getString("title");

                    MealPlanItem mealPlanItem = new MealPlanItem(id, recipeId, title);
                    mealPlanItemsList.add(mealPlanItem);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mealPlanItemsList;
    }
}
