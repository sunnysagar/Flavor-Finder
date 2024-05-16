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
import com.sunny.flavorfinder.modelJ.SimilarRecipeResponse;

import org.w3c.dom.Text;

import java.util.List;

public class SimilarRecipeAdapter extends RecyclerView.Adapter<SimilarRecipeViewHolder>{

    Context context;
    List<SimilarRecipeResponse> similarList;

    RecipeClickListener listener;

    public SimilarRecipeAdapter(Context context, List<SimilarRecipeResponse> similarList, RecipeClickListener listener) {
        this.context = context;
        this.similarList = similarList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SimilarRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SimilarRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.similar_recipe_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarRecipeViewHolder holder, int position) {

        holder.similar_recipe_title.setText(similarList.get(position).title);
        holder.similar_recipe_title.setSelected(true);
        holder.similar_recipe_time.setText(similarList.get(position).readyInMinutes+"minutes");

        Picasso.get().load("https://img.spoonacular.com/recipes/" + similarList.get(position).id + "-556x370."+similarList.get(position).imageType).into(holder.similar_recipe_image);

        holder.similarCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecipeClicked(String.valueOf(similarList.get(holder.getAdapterPosition()).id));
            }
        });

    }

    @Override
    public int getItemCount() {
        return similarList.size();
    }
}

class SimilarRecipeViewHolder extends RecyclerView.ViewHolder{

    CardView similarCard;
    TextView similar_recipe_time, similar_recipe_title;

    ImageView similar_recipe_image;

    public SimilarRecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        similarCard  = itemView.findViewById(R.id.similarCard);

        similar_recipe_time = itemView.findViewById(R.id.similar_recipe_time);
        similar_recipe_title = itemView.findViewById(R.id.similar_recipe_title);
        similar_recipe_image = itemView.findViewById(R.id.similar_recipe_image);
    }
}
