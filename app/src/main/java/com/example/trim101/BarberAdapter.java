package com.example.trim101;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class BarberAdapter extends RecyclerView.Adapter<BarberAdapter.BarberViewHolder> {

    private Context context;
    private List<Barber> barberList;
    private OnItemClickListener listener;

    public BarberAdapter(Context context, List<Barber> barberList) {
        this.context = context;
        this.barberList = barberList;
    }

    public interface OnItemClickListener {
        void onItemClick(Barber barber);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BarberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_barber, parent, false);
        return new BarberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberViewHolder holder, int position) {
        Barber barber = barberList.get(position);
        holder.textName.setText(barber.getUsername());

        // Load profile picture with Glide


        // Attach click listener to item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(barber);
            }
        });
    }

    @Override
    public int getItemCount() {
        return barberList.size();
    }

    static class BarberViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile;
        TextView textName;

        BarberViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            textName = itemView.findViewById(R.id.textName);
        }
    }
}
