package com.sunny.flavorfinder

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.sunny.flavorfinder.listenersJ.EquipmentDetailsListener
import com.sunny.flavorfinder.listenersJ.PopularRecipeResponeListener
import com.sunny.flavorfinder.listenersJ.RandomRecipeResponeListener
import com.sunny.flavorfinder.listenersJ.RecipeDetailsListener
import com.sunny.flavorfinder.listenersJ.SearchedRecipeResponseListener
import com.sunny.flavorfinder.listenersJ.SimilarRecipeResponseListener
import com.sunny.flavorfinder.modelJ.RandomRecipeApiResponse
import com.sunny.flavorfinder.modelJ.Recipe
import com.sunny.flavorfinder.modelJ.RecipeDetailsResponse
import com.sunny.flavorfinder.modelJ.Root
import com.sunny.flavorfinder.modelJ.SimilarRecipeResponse
import com.sunny.flavorfinder.modelJ.Step
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Collections
import java.util.PriorityQueue

class RequestManager(mainActivity: FragmentActivity) {

    var context: Context = mainActivity.applicationContext

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.spoonacular.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

//    fun RequestMan(context: Context?) {
//        this.context = context
//    }

    fun getRandomRecipe(listener: RandomRecipeResponeListener, tags: List<String>) {
        val callRandomRecipes = retrofit.create(CallRandomRecipes::class.java)
        callRandomRecipes.callRandomRecipe(context.getString(R.string.api_key), "20", tags)
            ?.enqueue(object : Callback<RandomRecipeApiResponse?> {
                override fun onResponse(
                    call: Call<RandomRecipeApiResponse?>,
                    response: Response<RandomRecipeApiResponse?>
                ) {
                    if (!response.isSuccessful && response.body() == null) {
                        listener.didError(response.message())
                    }
                    if(response.body() == null || response.body() != null) {
                        listener.didFetch(response.body(), response.message())
                    }
                }

                override fun onFailure(call: Call<RandomRecipeApiResponse?>, throwable: Throwable) {
                    listener.didError(throwable.message)
                }
            })
    }


    fun getSearchedRecipe(listener: SearchedRecipeResponseListener, query:String) {
        val callSearchedRecipes = retrofit.create(CallSearchedRecipes::class.java)
        callSearchedRecipes.callSearchedRecipe(context.getString(R.string.api_key),query)
            ?.enqueue(object : Callback<Root?> {
                override fun onResponse(
                    call: Call<Root?>,
                    response: Response<Root?>
                ) {
                    if (response.isSuccessful) {
                        listener.didFetch(response.body(), response.message())
                    } else {
                        listener.didError(response.message())
                    }

                }

                override fun onFailure(call: Call<Root?>, throwable: Throwable) {
                    listener.didError(throwable.message)
                }
            })
    }

    fun getTopPopularRecipes(listener: PopularRecipeResponeListener) {

        val likesComparator = Comparator.comparingInt { obj: Recipe -> obj.getAggregateLikes() }
            .reversed()
        val mostPopular = PriorityQueue(likesComparator)

        val callPopularRecipes = retrofit.create(CallPopularRecipes::class.java)
        callPopularRecipes.callPopularRecipe(context.getString(R.string.api_key), "5")?.enqueue(object : Callback<RandomRecipeApiResponse?> {
            override fun onResponse(
                call: Call<RandomRecipeApiResponse?>,
                response: Response<RandomRecipeApiResponse?>
            ) {
                if (!response.isSuccessful) {
                    listener.didError(response.message())
                    return
                }

                assert(response.body() != null)
                val allRecipes = response.body()!!.getRecipes()


                for (recipe in allRecipes) {
                    mostPopular.offer(recipe)

                    // maintain the size of pq i.e. 5
                    if (mostPopular.size > 5) mostPopular.poll()
                }

                val popularRecipe = ArrayList(mostPopular)

                Collections.sort(popularRecipe, likesComparator)


                // retrieve recipes from the priority queue and add them to the list

                //                while(!mostPopular.isEmpty())
                //                    popularRecipe.add(mostPopular.poll());
                listener.didFetch(popularRecipe, response.message())
            }

            override fun onFailure(call: Call<RandomRecipeApiResponse?>, throwable: Throwable) {
                listener.didError(throwable.message)
            }
        })
    }

    fun getRecipeDetails(listener: RecipeDetailsListener, id: Int) {
        val callRecipeDetails = retrofit.create(CallRecipeDetails::class.java)
        val call = callRecipeDetails.callRecipeDetail(id, context.getString(R.string.api_key))
        call?.enqueue(object : Callback<RecipeDetailsResponse?> {
            override fun onResponse(call: Call<RecipeDetailsResponse?>, response: Response<RecipeDetailsResponse?>
            ) {
                if (!response.isSuccessful) {
                    listener.didError(response.message())
                }

                listener.didFetch(response.body(), response.message())
            }

            override fun onFailure(call: Call<RecipeDetailsResponse?>, throwable: Throwable) {
                listener.didError(throwable.message)
            }
        })

    }



    // getting equipment
    fun getEquipmentDetails(listener: EquipmentDetailsListener, id: Int) {
        val getEquipmentForRecipe = retrofit.create(CallEquipmentForRecipe::class.java)
        val call = getEquipmentForRecipe.callEquipmentForRecipe(id, context.getString(R.string.api_key))
        call?.enqueue(object : Callback<Step?> {
            override fun onResponse(call: Call<Step?>, response: Response<Step?>) {
                if (!response.isSuccessful) {
                    listener.didError(response.message())
                }

                listener.didFetch(response.body(), response.message())
            }

            override fun onFailure(call: Call<Step?>, throwable: Throwable) {
                listener.didError(throwable.message)
            }

        })

    }

    fun getSimilarRecipes(listener: SimilarRecipeResponseListener, id: Int) {
        val callSimilarRecipes = retrofit.create(CallSimilarRecipes::class.java)
        val call =
            callSimilarRecipes.callSimilarRecipe(id, "10", context.getString(R.string.api_key))
        call!!.enqueue(object : Callback<List<SimilarRecipeResponse?>?> {
            override fun onResponse(
                call: Call<List<SimilarRecipeResponse?>?>,
                response: Response<List<SimilarRecipeResponse?>?>
            ) {
                if (!response.isSuccessful) {
                    listener.didError(response.message())
                }
                listener.didFetch(response.body(), response.message())
            }

            override fun onFailure(
                call: Call<List<SimilarRecipeResponse?>?>,
                throwable: Throwable
            ) {
                listener.didError(throwable.message)
            }
        })
    }


    private interface CallRandomRecipes {
        @GET("recipes/random")
        fun callRandomRecipe(
            @Query("apiKey") apiKey: String,
            @Query("number") number: String,
            @Query("tags") tags: List<String>
        ): Call<RandomRecipeApiResponse?>?
    }


    private interface CallRecipeDetails {
        @GET("recipes/{id}/information")
        fun callRecipeDetail(
            @Path("id") id: Int,
            @Query("apiKey") apiKey: String?
        ): Call<RecipeDetailsResponse?>?
    }

    private interface CallPopularRecipes {
        @GET("recipes/random")
        fun callPopularRecipe(
            @Query("apiKey") apiKey: String?,
            @Query("number") number: String?
        ): Call<RandomRecipeApiResponse?>?
    }

    private interface CallEquipmentForRecipe{
        @GET("recipes/{id}/equipmentWidget.json")
        fun callEquipmentForRecipe(
            @Path("id") id: Int,
            @Query("apiKey") apiKey: String?
        ): Call<Step?>?
    }

    private interface CallSearchedRecipes {
        @GET("recipes/complexSearch")
        fun callSearchedRecipe(
            @Query("apiKey") apiKey: String?,
           @Query("query") query: String?
        ): Call<Root?>?
    }

    private interface CallSimilarRecipes {
        @GET("recipes/{id}/similar")
        fun callSimilarRecipe(
            @Path("id") id: Int,
            @Query("number") number: String?,
            @Query("apiKey") apiKey: String?

        ): Call<List<SimilarRecipeResponse?>?>?
    }


}