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

public class RandomRecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder>{

    Context context;
    List<Recipe> list;

    RecipeClickListener listener;

    public RandomRecipeAdapter(Context context, List<Recipe> list, RecipeClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_random_recipe, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.textView_title.setText(list.get(position).title);
        holder.textView_title.setSelected(true);

        holder.textView_time.setText(list.get(position).readyInMinutes+" Minutes");

        Picasso.get().load(list.get(position).image).into(holder.imageView_food);

        holder.randomCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecipeClicked(String.valueOf(list.get(holder.getAdapterPosition()).id));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class RecipeViewHolder extends RecyclerView.ViewHolder{
    CardView randomCard;
    TextView textView_title, textView_time;

    ImageView imageView_food;
    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        randomCard  = itemView.findViewById(R.id.randomCard);

        textView_title = itemView.findViewById(R.id.textView_title);

        textView_time = itemView.findViewById(R.id.textView_time);


        imageView_food = itemView.findViewById(R.id.imageView_food);


    }
}
