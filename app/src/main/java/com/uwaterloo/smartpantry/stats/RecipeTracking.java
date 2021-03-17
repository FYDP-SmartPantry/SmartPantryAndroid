package com.uwaterloo.smartpantry.stats;

public class RecipeTracking {
    final static String recipeIdString = "recipeId";
    final static String countString = "count";

    private String recipeId;
    private int count;

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
