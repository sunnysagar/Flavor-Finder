package com.sunny.flavorfinder.adapterJ;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.sunny.flavorfinder.R;
import com.sunny.flavorfinder.listenersJ.RecipeClickListener;
import com.sunny.flavorfinder.modelJ.Recipe;
import com.sunny.flavorfinder.modelJ.Result;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchRecipeViewHolder> {

    Context context;
    List<Result> recipeList;

    RecipeClickListener listener;

    public SearchAdapter(Context context, List<Result> recipeList, RecipeClickListener listener) {
        this.context = context;
        this.recipeList = recipeList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public SearchRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.search_recipe_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecipeViewHolder holder, int position) {
        holder.textView_recipe_title.setText(recipeList.get(position).title);
        holder.textView_recipe_title.setSelected(true);

        Picasso.get().load(recipeList.get(position).image).into(holder.imageView_search_food);

        holder.searchRecipeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecipeClicked(String.valueOf(recipeList.get(holder.getAdapterPosition()).id));
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

}

class SearchRecipeViewHolder extends RecyclerView.ViewHolder{

    RelativeLayout searchRecipeList;

    TextView textView_recipe_title;

    ImageView imageView_search_food;

    public SearchRecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        searchRecipeList = itemView.findViewById(R.id.searchRecipeList);
        textView_recipe_title = itemView.findViewById(R.id.textView_recipe_title);
        imageView_search_food = itemView.findViewById(R.id.imageView_search_food);
    }
}
