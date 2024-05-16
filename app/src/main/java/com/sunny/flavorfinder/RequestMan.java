package com.sunny.flavorfinder;

import android.content.Context;

import com.sunny.flavorfinder.listenersJ.PopularRecipeResponeListener;
import com.sunny.flavorfinder.listenersJ.RandomRecipeResponeListener;
import com.sunny.flavorfinder.listenersJ.RecipeDetailsListener;
import com.sunny.flavorfinder.listenersJ.SimilarRecipeResponseListener;
import com.sunny.flavorfinder.modelJ.PopularRecipeApiRespone;
import com.sunny.flavorfinder.modelJ.RandomRecipeApiResponse;
import com.sunny.flavorfinder.modelJ.Recipe;
import com.sunny.flavorfinder.modelJ.RecipeDetailsResponse;
import com.sunny.flavorfinder.modelJ.SimilarRecipeResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RequestMan {

    Context context;
    Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestMan(Context context) {
        this.context = context;
    }

    public void getRandomRecipe(RandomRecipeResponeListener listener, List<String> tags){
        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        Call<RandomRecipeApiResponse> call = callRandomRecipes.callRandomRecipe(context.getString(R.string.api_key), "20", tags);
        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }

    public void getRecipeDetails(RecipeDetailsListener listener, int id){
        CallRecipeDetails callRecipeDetails = retrofit.create(CallRecipeDetails.class);
        Call<RecipeDetailsResponse> call = callRecipeDetails.callRecipeDetail(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipeDetailsResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailsResponse> call, Response<RecipeDetailsResponse> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                }

                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeDetailsResponse> call, Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }

    public void getTopPopularRecipes(PopularRecipeResponeListener listener) {

        Comparator<Recipe> likesComparator = Comparator.comparingInt(Recipe::getAggregateLikes).reversed();
        PriorityQueue<Recipe> mostPopular = new PriorityQueue<>(likesComparator);

        CallPopularRecipes callPopularRecipes= retrofit.create(CallPopularRecipes.class);
        Call<RandomRecipeApiResponse> call = callPopularRecipes.callPopularRecipe(context.getString(R.string.api_key),"5");
        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }

////                assert response.body() != null;
                assert response.body() != null;
                ArrayList<Recipe> allRecipes= response.body().getRecipes();
//                allRecipes.addAll(response.body().getPopulaRecipe());


                for(Recipe recipe : allRecipes)
                {
                    mostPopular.offer(recipe);

                    // maintain the size of pq i.e. 5
                    if(mostPopular.size()>5)
                        mostPopular.poll();
                }

                ArrayList<Recipe> popularRecipe = new ArrayList<>(mostPopular);

                Collections.sort(popularRecipe, likesComparator);

                // retrieve recipes from the priority queue and add them to the list

//                while(!mostPopular.isEmpty())
//                    popularRecipe.add(mostPopular.poll());


                listener.didFetch(popularRecipe, response.message());

            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable throwable) {
                listener.didError(throwable.getMessage());

            }
        });
    }

    public void getSimilarRecipes(SimilarRecipeResponseListener listener, int id){
        CallSimilarRecipes callSimilarRecipes = retrofit.create(CallSimilarRecipes.class);
       Call<List<SimilarRecipeResponse>> call = callSimilarRecipes.callSimilarRecipe(id, "10", context.getString(R.string.api_key));
       call.enqueue(new Callback<List<SimilarRecipeResponse>>() {
           @Override
           public void onResponse(Call<List<SimilarRecipeResponse>> call, Response<List<SimilarRecipeResponse>> response) {
               if(!response.isSuccessful()){
                   listener.didError(response.message());
               }
               listener.didFetch(response.body(), response.message());
           }

           @Override
           public void onFailure(Call<List<SimilarRecipeResponse>> call, Throwable throwable) {
               listener.didError(throwable.getMessage());

           }
       });
    }


    private interface CallRandomRecipes{
        @GET("recipes/random")
        Call<RandomRecipeApiResponse> callRandomRecipe(
                @Query("apiKey") String apiKey,
                @Query("number")String number,
                @Query("tags")List<String> tags
                );
    }


    private interface CallRecipeDetails{
        @GET("recipes/{id}/information")
        retrofit2.Call<RecipeDetailsResponse> callRecipeDetail(
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );
    }

    private interface CallPopularRecipes{
        @GET("recipes/random")
        retrofit2.Call<RandomRecipeApiResponse> callPopularRecipe(
                @Query("apiKey") String apiKey,
                @Query("number") String number
        );
    }

    private interface CallSimilarRecipes{
        @GET("recipes/{id}/similar")
        Call<List<SimilarRecipeResponse>> callSimilarRecipe(
                @Path("id") int id,
                @Query("number") String number,
                @Query("apiKey") String apiKey

        );
    }

}
