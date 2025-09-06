package com.example.pizzamaniaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private ArrayList<String> cartNames, cartPrices, cartQuantities;
    private ArrayList<Integer> cartImages;

    public CartAdapter(Context context, ArrayList<String> cartNames, ArrayList<String> cartPrices,
                       ArrayList<String> cartQuantities, ArrayList<Integer> cartImages) {
        this.context = context;
        this.cartNames = cartNames;
        this.cartPrices = cartPrices;
        this.cartQuantities = cartQuantities;
        this.cartImages = cartImages;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.cartItemName.setText(cartNames.get(position));
        holder.cartItemPrice.setText("Rs. " + cartPrices.get(position));
        holder.cartItemQuantity.setText("Qty: " + cartQuantities.get(position));
        holder.cartItemImage.setImageResource(cartImages.get(position));
    }

    @Override
    public int getItemCount() {
        return cartNames.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView cartItemName, cartItemPrice, cartItemQuantity;
        ImageView cartItemImage;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartItemName = itemView.findViewById(R.id.cartItemName);
            cartItemPrice = itemView.findViewById(R.id.cartItemPrice);
            cartItemQuantity = itemView.findViewById(R.id.cartItemQuantity);
            cartItemImage = itemView.findViewById(R.id.cartItemImage);
        }
    }
}

