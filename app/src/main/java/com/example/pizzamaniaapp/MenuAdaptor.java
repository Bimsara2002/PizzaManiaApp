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
    private ArrayList<Integer> quantities; // Track quantities

    public MenuAdaptor(Context context, ArrayList<Integer> itemIds,
                       ArrayList<String> foodNames, ArrayList<String> foodPrices,
                       ArrayList<String> foodImages, String username) {
        this.context = context;
        this.itemIds = itemIds;
        this.foodNames = foodNames;
        this.foodPrices = foodPrices;
        this.foodImages = foodImages;
        this.username = username;
        this.quantities = new ArrayList<>();

        initQuantities();
    }

    private void initQuantities() {
        quantities.clear();
        if (foodNames != null) {
            for (int i = 0; i < foodNames.size(); i++) {
                quantities.add(1);
            }
        }
    }

    // Allow updating data safely
    public void updateData(ArrayList<Integer> itemIds,
                           ArrayList<String> foodNames,
                           ArrayList<String> foodPrices,
                           ArrayList<String> foodImages) {
        this.itemIds = new ArrayList<>(itemIds);
        this.foodNames = new ArrayList<>(foodNames);
        this.foodPrices = new ArrayList<>(foodPrices);
        this.foodImages = new ArrayList<>(foodImages);
        initQuantities();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        if (position >= foodNames.size()) return; // safety guard

        holder.foodName.setText(foodNames.get(position));
        holder.foodPrice.setText("Rs. " + foodPrices.get(position));
        holder.tvQuantity.setText(String.valueOf(quantities.get(position)));

        String imagePath = foodImages.get(position);

        if (imagePath != null) {
            File file = new File(imagePath);
            if (file.exists()) {
                Glide.with(context)
                        .load(file)
                        .placeholder(R.drawable.ic_placeholder)
                        .circleCrop()
                        .into(holder.foodImage);
            } else {
                int resId = context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
                if (resId != 0) {
                    Glide.with(context).load(resId).circleCrop().into(holder.foodImage);
                } else {
                    Glide.with(context).load(R.drawable.ic_placeholder).circleCrop().into(holder.foodImage);
                }
            }
        }

        // Increase quantity
        holder.btnIncrease.setOnClickListener(v -> {
            int qty = quantities.get(position) + 1;
            quantities.set(position, qty);
            holder.tvQuantity.setText(String.valueOf(qty));
        });

        // Decrease quantity
        holder.btnDecrease.setOnClickListener(v -> {
            int qty = quantities.get(position);
            if (qty > 1) {
                qty--;
                quantities.set(position, qty);
                holder.tvQuantity.setText(String.valueOf(qty));
            }
        });

        // Add to Cart button logic
        holder.addToCartBtn.setOnClickListener(v -> {
            DBHelper dbHelper = new DBHelper(context);
            int itemId = itemIds.get(position);
            int qty = quantities.get(position);
            boolean inserted = dbHelper.addToCart(username, itemId, qty);

            if (inserted) {
                Toast.makeText(context, foodNames.get(position) + " x" + qty + " added to cart!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to add item.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodNames != null ? foodNames.size() : 0;
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodPrice, tvQuantity;
        ImageView foodImage;
        Button addToCartBtn, btnIncrease, btnDecrease;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodImage = itemView.findViewById(R.id.foodImage);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}
