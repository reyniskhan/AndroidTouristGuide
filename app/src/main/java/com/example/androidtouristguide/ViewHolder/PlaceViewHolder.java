package com.example.androidtouristguide.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtouristguide.Interface.ItemClickListener;
import com.example.androidtouristguide.R;

public class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtlaceName, txtPlaceDescription, txtPlaceDistance;
    public ImageView imageView;
    public ItemClickListener listener;


    public PlaceViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.place_image);
        txtPlaceDescription = itemView.findViewById(R.id.place_description);
        txtlaceName = itemView.findViewById(R.id.place_name);
        txtPlaceDistance = itemView.findViewById(R.id.place_distance);

    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;

    }

    @Override
    public void onClick(View view) {

        listener.onClick(view, getAdapterPosition(),false);

    }
}
