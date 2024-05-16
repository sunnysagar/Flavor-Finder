package com.sunny.flavorfinder.adapterJ;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.sunny.flavorfinder.R;
import com.sunny.flavorfinder.listenersJ.RecipeClickListener;
import com.sunny.flavorfinder.modelJ.Recipe;

import java.util.List;

public class PopularRecipeAdapter extends RecyclerView.Adapter<PopularRecipeViewHolder> {

    Context context;

    List<Recipe> popularList;

    RecipeClickListener listener;

    public PopularRecipeAdapter(Context context, List<Recipe> popularList, RecipeClickListener listener) {
        this.context = context;
        this.popularList = popularList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PopularRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PopularRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_popular_recipes, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopularRecipeViewHolder holder, int position) {

        holder.textView_popular_title.setText(popularList.get(position).title);
        holder.textView_popular_title.setSelected(true);

        holder.textView_popular_time.setText(popularList.get(position).readyInMinutes+" Mins");

        Picasso.get().load(popularList.get(position).image).into(holder.imageView_popular_food);

        holder.popularCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecipeClicked(String.valueOf(popularList.get(holder.getAdapterPosition()).id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return popularList.size();
    }
}

class PopularRecipeViewHolder extends RecyclerView.ViewHolder{

    CardView popularCard;

    TextView textView_popular_time, textView_popular_title;

    ImageView imageView_popular_food;

    public PopularRecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        popularCard = itemView.findViewById(R.id.popularCard);
        textView_popular_title = itemView.findViewById(R.id.textView_popular_title);
        textView_popular_time = itemView.findViewById(R.id.textView_popular_time);

        imageView_popular_food = itemView.findViewById(R.id.imageView_popular_food);

    }
}
