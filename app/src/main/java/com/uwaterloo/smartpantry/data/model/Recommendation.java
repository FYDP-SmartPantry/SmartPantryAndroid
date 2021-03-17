package com.uwaterloo.smartpantry.data.model;

import android.util.Pair;

import com.uwaterloo.smartpantry.inventory.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Recommendation {
    private List<Pair<Integer, String>> recipes;
    private JSONArray recipeInstructions;

    public Recommendation() {
    }

    public List<Pair<Integer, String>> getRecipes() {
        return recipes;
    }

    public void clearRecipes(){
     this.recipes = new ArrayList<Pair<Integer, String>>();
    }

    public void setRecipes(List<Pair<Integer, String>> recipes) {
        this.recipes = recipes;
    }

    public JSONArray getRecipeInstructions() {
        return recipeInstructions;
    }

    public void setRecipeInstructions(JSONArray recipeInstructions) {
        this.recipeInstructions = recipeInstructions;
    }

    public void parseAndAddRecipes(JSONArray jsonArray) {
        List<Pair<Integer, String>> recipes = this.recipes;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Pair<Integer, String> p = Pair.create((Integer) jsonObject.get("id"), (String) jsonObject.get("title"));
                recipes.add(p);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setRecipes(recipes.stream().distinct().collect(Collectors.toList()));
    }

    public void parseAndAddRecipesLimit(JSONArray jsonArray, Food food) {
        List<Pair<Integer, String>> recipes = this.recipes;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray usedIngredients = jsonObject.getJSONArray("usedIngredients");
                double ingredientCount = 0.0;
                for (int j = 0; i < usedIngredients.length(); j++) {
                    String ingredient = usedIngredients.getJSONObject(j).getString("name");
                    if (Pattern.compile(Pattern.quote(ingredient), Pattern.CASE_INSENSITIVE).matcher(food.getName()).find() ||
                            Pattern.compile(Pattern.quote(food.getName()), Pattern.CASE_INSENSITIVE).matcher(ingredient).find()) {
                        ingredientCount = usedIngredients.getJSONObject(j).getDouble("amount");
                        break;
                    }
                }
                if (ingredientCount > 0 && ingredientCount < food.getNumber()) {
                    Pair<Integer, String> p = Pair.create((Integer) jsonObject.get("id"), (String) jsonObject.get("title"));
                    recipes.add(p);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setRecipes(recipes.stream().distinct().collect(Collectors.toList()));
    }

    public List<String> getRecipeNames() {
     List<String> recipeNames = new ArrayList<>();
     for (Pair<Integer, String> recipe: recipes) {
         recipeNames.add(recipe.second);
     }
     return recipeNames;
    }

    public String getSourceUrl(JSONObject jsonObject) {
        String sourceUrl = null;
        try {
            sourceUrl = jsonObject.getString("sourceUrl");
            // spoonacularSourceUrl may not exist for all recipes
            sourceUrl = jsonObject.getString("spoonacularSourceUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sourceUrl;
    }

    public int getRecipeId(int i) {
        return recipes.get(i).first;
    }

}
