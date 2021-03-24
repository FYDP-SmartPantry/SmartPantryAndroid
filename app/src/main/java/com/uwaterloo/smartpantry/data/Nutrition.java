package com.uwaterloo.smartpantry.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Nutrition {
    private int fat;
    private int carbohydrates;
    private int protein;

    public Nutrition(){
        this.fat = 0;
        this.carbohydrates = 0;
        this.protein = 0;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public static Nutrition parseNutrients(JSONObject obj) {
        Nutrition nutrition = new Nutrition();
        try {
            JSONArray days = obj.getJSONArray("days");
            for (int i = 0; i < days.length(); i++) {
                JSONObject day = days.getJSONObject(i);
                JSONObject nutritionSummary = day.getJSONObject("nutritionSummary");
                JSONArray nutrients = nutritionSummary.getJSONArray("nutrients");
                for (i = 0; i < nutrients.length(); i++) {
                    JSONObject res = nutrients.getJSONObject(i);
                    switch (res.getString("title")) {
                        case "Fat":
                            nutrition.setFat(nutrition.getFat() + res.getInt("amount"));
                            break;
                        case "Carbohydrates":
                            nutrition.setCarbohydrates(nutrition.getCarbohydrates() + res.getInt("amount"));
                            break;
                        case "Protein":
                            nutrition.setProtein(nutrition.getProtein() + res.getInt("amount"));
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nutrition;
    }
}
