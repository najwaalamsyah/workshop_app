package com.example.workshopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Map;

public class WorkshopAdapter extends RecyclerView.Adapter<WorkshopAdapter.WorkshopViewHolder> {
    private List<Map<String, String>> workshopList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    // Define the OnItemClickListener interface
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Constructor with the OnItemClickListener parameter
    public WorkshopAdapter(List<Map<String, String>> workshopList, Context context, OnItemClickListener onItemClickListener) {
        this.workshopList = workshopList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public WorkshopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the updated layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_upload, parent, false);
        return new WorkshopViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkshopViewHolder holder, int position) {
        Map<String, String> workshop = workshopList.get(position);

        // Bind the workshop data to the views
        holder.workshopName.setText(workshop.get("workshopName"));
        holder.date.setText(workshop.get("date"));
        holder.location.setText(workshop.get("location"));
        holder.price.setText(workshop.get("price"));
        holder.maxParticipants.setText(workshop.get("maxParticipants") + " person");

        // Load the image using Glide (assuming imageUrl is stored as "imageUrl")
        String imageUrl = workshop.get("imageUrl");
        Glide.with(context)
                .load(imageUrl)  // The image URL from your data
                .apply(new RequestOptions().placeholder(R.drawable.placeholder))  // Add placeholder
                .into(holder.workshopImage); // ImageView where the image will be displayed

        // Set click listener for the entire item view
        holder.itemContainer.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);  // Pass position to the listener
            }
        });
    }

    @Override
    public int getItemCount() {
        return workshopList.size();
    }

    public static class WorkshopViewHolder extends RecyclerView.ViewHolder {
        public TextView workshopName, date, location, maxParticipants, price;
        public ImageView workshopImage;  // ImageView for the image
        public View itemContainer; // The clickable container (e.g., CardView or LinearLayout)

        public WorkshopViewHolder(View itemView) {
            super(itemView);
            workshopName = itemView.findViewById(R.id.workshopTitle);
            date = itemView.findViewById(R.id.workshopDate);
            location = itemView.findViewById(R.id.workshopLocation);
            price = itemView.findViewById(R.id.priceWorkshop);
            maxParticipants = itemView.findViewById(R.id.maxParticipants);
            workshopImage = itemView.findViewById(R.id.workshopImage); // ImageView for the image
            itemContainer = itemView.findViewById(R.id.workshopTitle); // The clickable container
 }
}
}
