package com.sunny.flavorfinder.listenersJ;

import com.sunny.flavorfinder.modelJ.RandomRecipeApiResponse;

public interface RandomRecipeResponeListener {
    void didFetch(RandomRecipeApiResponse response,String message);
    void didError(String message);
}
