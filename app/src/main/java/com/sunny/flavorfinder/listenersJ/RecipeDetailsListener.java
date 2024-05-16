package com.sunny.flavorfinder.listenersJ;

import com.sunny.flavorfinder.modelJ.RecipeDetailsResponse;

public interface RecipeDetailsListener {

    void didFetch(RecipeDetailsResponse response, String message);
    void didError(String message);
}
