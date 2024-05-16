package com.sunny.flavorfinder.listenersJ;

import com.sunny.flavorfinder.modelJ.RandomRecipeApiResponse;
import com.sunny.flavorfinder.modelJ.Root;
import com.sunny.flavorfinder.modelJ.SearchRecipeApiResponse;

public interface SearchedRecipeResponseListener {

    void didFetch(Root response, String message);
    void didError(String message);
}
