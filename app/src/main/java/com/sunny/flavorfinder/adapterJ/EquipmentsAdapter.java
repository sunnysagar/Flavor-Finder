package com.sunny.flavorfinder.adapterJ;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.sunny.flavorfinder.R;
import com.sunny.flavorfinder.modelJ.Equipment;

import java.util.List;

public class EquipmentsAdapter extends RecyclerView.Adapter<EquipmentViewHolder> {

    Context context;

    List<Equipment> list;

    public EquipmentsAdapter(Context context, List<Equipment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EquipmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EquipmentViewHolder(LayoutInflater.from(context).inflate(R.layout.list_meal_equipments, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EquipmentViewHolder holder, int position) {

        holder.textView_equipment_name.setText(list.get(position).name);
        holder.textView_equipment_name.setSelected(true);

        Picasso.get().load("https://img.spoonacular.com/equipment_100x100/" +
                list.get(position).image).into(holder.imageView_equipments);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class EquipmentViewHolder extends RecyclerView.ViewHolder{

    TextView textView_equipment_name;
    ImageView imageView_equipments;
    public EquipmentViewHolder(@NonNull View itemView) {
        super(itemView);

        textView_equipment_name = itemView.findViewById(R.id.textView_equipment_name);
        imageView_equipments = itemView.findViewById(R.id.imageView_equipment);
    }
}
