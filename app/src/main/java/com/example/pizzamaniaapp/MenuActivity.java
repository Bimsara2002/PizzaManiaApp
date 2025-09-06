package com.example.pizzamaniaapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {


    RecyclerView menuRecyclerView;
    @SuppressLint("RestrictedApi")
    MenuAdaptor adapter;
    DBHelper dbHelper;


    ArrayList<Integer> itemIds;
    ArrayList<String> foodNames;
    ArrayList<String> foodPrices;
    ArrayList<Integer> foodImages;

    String currentUser = "Pehsara"; // replace with logged-in username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuRecyclerView = findViewById(R.id.menuRecyclerView);
        dbHelper = new DBHelper(this);

        itemIds = new ArrayList<>();
        foodNames = new ArrayList<>();
        foodPrices = new ArrayList<>();
        foodImages = new ArrayList<>();

        loadMenuFromDB();

        adapter = new MenuAdaptor(this, itemIds, foodNames, foodPrices, foodImages, currentUser);
        menuRecyclerView.setAdapter(adapter);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // load all items at first
        loadMenu("All");

        //hook up category buttons
        Button allBtn = findViewById(R.id.allBtn);
        Button pizzaBtn = findViewById(R.id.pizzaBtn);
        Button drinksBtn = findViewById(R.id.drinksBtn);
        Button sidesBtn = findViewById(R.id.sidesBtn);

        allBtn.setOnClickListener(v -> loadMenu("All"));
        pizzaBtn.setOnClickListener(v -> loadMenu("Pizza"));
        drinksBtn.setOnClickListener(v -> loadMenu("Drinks"));
        sidesBtn.setOnClickListener(v -> loadMenu("Sides"));


        FloatingActionButton cartFab= findViewById(R.id.cartFab);
        cartFab.setOnClickListener(v->{
            Intent intent=new Intent(MenuActivity.this,CartActivity.class);
            startActivity(intent);
        });
    }

    private void loadMenuFromDB() {
        Cursor cursor = dbHelper.getAllMenuItems();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("item_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
            String imageName = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));

            int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());

            itemIds.add(id);
            foodNames.add(name);
            foodPrices.add(price);
            foodImages.add(resId);
        }
        cursor.close();
    }

    //Reusable method to load menu by category
    private void loadMenu(String category) {
        itemIds.clear();
        foodNames.clear();
        foodPrices.clear();
        foodImages.clear();

        Cursor cursor = dbHelper.getMenuByCategory(category);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("item_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
            String imageName = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));

            int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());

            itemIds.add(id);
            foodNames.add(name);
            foodPrices.add(price);
            foodImages.add(resId);
        }
        cursor.close();

        adapter.notifyDataSetChanged(); // refresh RecyclerView
    }
}
