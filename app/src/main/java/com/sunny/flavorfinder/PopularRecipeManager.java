package com.sunny.flavorfinder;

import com.sunny.flavorfinder.modelJ.Recipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class PopularRecipeManager {

    private static Comparator<Recipe> likesComparator = Comparator.comparingInt(Recipe::getAggregateLikes).reversed();

    public List<Recipe> getPopularRecipes(List<Recipe> allRecipes){
        // creating pq based on likes
        PriorityQueue<Recipe> mostPopular = new PriorityQueue<>(likesComparator);

        for(Recipe recipe : allRecipes)
        {
            mostPopular.offer(recipe);

            // maintain the size of pq i.e. 5
            if(mostPopular.size()>5)
                mostPopular.poll();
        }

        List<Recipe> popularRecipe = new ArrayList<>();

        // retrieve recipes from the priority queue and add them to the list

        while(!mostPopular.isEmpty())
            popularRecipe.add(mostPopular.poll());

        return popularRecipe;
    }
}
