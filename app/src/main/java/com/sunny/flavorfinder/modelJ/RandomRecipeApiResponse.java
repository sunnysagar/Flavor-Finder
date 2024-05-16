package com.sunny.flavorfinder.modelJ;

import java.util.ArrayList;

public class RandomRecipeApiResponse {
    public ArrayList<Recipe> recipes;

    public RandomRecipeApiResponse(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }
}
