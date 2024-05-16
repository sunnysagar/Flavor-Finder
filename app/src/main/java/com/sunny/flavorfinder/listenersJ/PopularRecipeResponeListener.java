package com.sunny.flavorfinder.listenersJ;

import com.sunny.flavorfinder.modelJ.PopularRecipeApiRespone;
import com.sunny.flavorfinder.modelJ.RandomRecipeApiResponse;
import com.sunny.flavorfinder.modelJ.Recipe;

import java.util.ArrayList;

public interface PopularRecipeResponeListener {

    void didFetch(ArrayList<Recipe> list, String message);
    void didError(String message);
}
