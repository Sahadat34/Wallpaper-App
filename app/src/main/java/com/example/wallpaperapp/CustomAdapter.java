package com.example.wallpaperapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaperapp.listeners.OnRecyclerClickListner;
import com.example.wallpaperapp.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Myholder> {
   Context context;
   List<Photo> list;
   OnRecyclerClickListner listner;

    public CustomAdapter(Context context, List<Photo> list, OnRecyclerClickListner listner) {
        this.context = context;
        this.list = list;
        this.listner = listner;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Myholder(LayoutInflater.from(context).inflate(R.layout.home,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        Picasso.get().load(list.get(position).getSrc().getMedium()).placeholder(R.drawable.not_available).into(holder.list_image);
        holder.mainCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onClick(list.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Myholder extends RecyclerView.ViewHolder {
        ImageView list_image;
        CardView mainCardView;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            list_image = itemView.findViewById(R.id.image_view_list);
            mainCardView = itemView.findViewById(R.id.card_view_home);
        }
    }
}
