package com.sunny.flavorfinder.listenersJ;

import com.sunny.flavorfinder.modelJ.SimilarRecipeResponse;

import java.util.List;

public interface SimilarRecipeResponseListener {

    void didFetch(List<SimilarRecipeResponse> response, String message);
    void didError(String message);
}
