package com.example.pizzamaniaapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    RecyclerView cartRecyclerView;
    CartAdapter adapter;
    TextView totalPrice;
    Button checkoutBtn;
    DBHelper DBHelper;

    ArrayList<String> cartNames, cartPrices, cartQuantities;
    ArrayList<Integer> cartImages;

    String currentUser = "Pehsara"; // replace with logged-in username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalPrice = findViewById(R.id.totalPrice);
        checkoutBtn = findViewById(R.id.checkoutBtn);
        DBHelper = new DBHelper(this);

        cartNames = new ArrayList<>();
        cartPrices = new ArrayList<>();
        cartQuantities = new ArrayList<>();
        cartImages = new ArrayList<>();

        loadCartFromDB();

        adapter = new CartAdapter(this, cartNames, cartPrices, cartQuantities, cartImages);
        cartRecyclerView.setAdapter(adapter);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        checkoutBtn.setOnClickListener(v -> {
            DBHelper.clearCart(currentUser);
            Toast.makeText(this, "Checkout complete! Order placed.", Toast.LENGTH_SHORT).show();
            finish(); // close cart
        });
    }

    private void loadCartFromDB() {
        Cursor cursor = DBHelper.getCartItems(currentUser);

        double total = 0;

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            String qty = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));
            String imageName = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));

            int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());

            cartNames.add(name);
            cartPrices.add(String.valueOf(price));
            cartQuantities.add(qty);
            cartImages.add(resId);

            total += price * Integer.parseInt(qty);
        }
        cursor.close();

        totalPrice.setText("Total: Rs. " + total);
    }
}
