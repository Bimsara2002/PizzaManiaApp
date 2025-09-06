package com.example.pizzamaniaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class MenuAdaptor extends RecyclerView.Adapter<MenuAdaptor.MenuViewHolder> {

    private Context context;
    private ArrayList<Integer> itemIds;
    private ArrayList<String> foodNames;
    private ArrayList<String> foodPrices;
    private ArrayList<String> foodImages;
    private String username;

    public MenuAdaptor(Context context, ArrayList<Integer> itemIds,
                       ArrayList<String> foodNames, ArrayList<String> foodPrices,
                       ArrayList<String> foodImages, String username) {
        this.context = context;
        this.itemIds = itemIds;
        this.foodNames = foodNames;
        this.foodPrices = foodPrices;
        this.foodImages = foodImages;
        this.username = username;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.foodName.setText(foodNames.get(position));
        holder.foodPrice.setText("Rs. " + foodPrices.get(position));

        String imagePath = foodImages.get(position);

        if (imagePath != null) {
            File file = new File(imagePath);
            if (file.exists()) {
                Glide.with(context)
                        .load(file) //loads copied file
                        .placeholder(R.drawable.ic_placeholder)
                        .into(holder.foodImage);
            } else {
                // fallback to drawable name
                int resId = context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
                if (resId != 0) {
                    Glide.with(context).load(resId).into(holder.foodImage);
                } else {
                    holder.foodImage.setImageResource(R.drawable.ic_placeholder);
                }
            }
        }



        holder.addToCartBtn.setOnClickListener(v -> {
            DBHelper dbHelper = new DBHelper(context);
            int itemId = itemIds.get(position);  // get menu item ID from DB
            boolean inserted = dbHelper.addToCart(username, itemId, 1);

            if (inserted) {
                Toast.makeText(context, foodNames.get(position) + " added to cart!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to add item.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodNames.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodPrice;
        ImageView foodImage;
        Button addToCartBtn;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodImage = itemView.findViewById(R.id.foodImage);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
        }
    }
}
