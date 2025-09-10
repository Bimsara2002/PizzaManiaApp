package com.example.pizzamaniaapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    RecyclerView menuRecyclerView;
    MenuAdaptor adapter;
    DBHelper dbHelper;

    ArrayList<Integer> itemIds;
    ArrayList<String> foodNames;
    ArrayList<String> foodPrices;
    ArrayList<String> foodImages;

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

        // Branch options
        String[] branches = {"Colombo", "Galle", "Kandy", "Jaffna"};
        final String[] selectedBranch = {branches[0]}; // track selected branch

        Spinner branch = findViewById(R.id.spinnerBranch);
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, branches);
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(branchAdapter);

        // load all items at first (Colombo + All)
        loadMenu("All", selectedBranch[0]);

        //hook up category buttons
        Button allBtn = findViewById(R.id.allBtn);
        Button pizzaBtn = findViewById(R.id.pizzaBtn);
        Button drinksBtn = findViewById(R.id.drinksBtn);
        Button sidesBtn = findViewById(R.id.sidesBtn);
        Button orderStatusBtn = findViewById(R.id.viewOrderStatus);

        allBtn.setOnClickListener(v -> loadMenu("All", selectedBranch[0]));
        pizzaBtn.setOnClickListener(v -> loadMenu("Pizza", selectedBranch[0]));
        drinksBtn.setOnClickListener(v -> loadMenu("Drinks", selectedBranch[0]));
        sidesBtn.setOnClickListener(v -> loadMenu("Sides", selectedBranch[0]));

        FloatingActionButton cartFab = findViewById(R.id.cartFab);
        cartFab.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, CartActivity.class);
            startActivity(intent);
        });

        orderStatusBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, OrderTrackingActivity.class);
            startActivity(intent);
        });

        // Handle branch change
        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBranch[0] = branches[position];
                loadMenu("All", selectedBranch[0]); // reload menu for branch
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadMenuFromDB() {
        Cursor cursor = dbHelper.getAllMenuItems();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("item_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
            String imageName = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));

            itemIds.add(id);
            foodNames.add(name);
            foodPrices.add(price);
            foodImages.add(imageName);
        }
        cursor.close();
    }

    // Reusable method to load menu by category + branch
    private void loadMenu(String category, String branch) {
        itemIds.clear();
        foodNames.clear();
        foodPrices.clear();
        foodImages.clear();

        Cursor cursor = dbHelper.getMenuByCategoryAndBranch(category, branch);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("item_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
            String imageName = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));

            itemIds.add(id);
            foodNames.add(name);
            foodPrices.add(price);
            foodImages.add(imageName);
        }
        cursor.close();

        adapter.notifyDataSetChanged(); // refresh RecyclerView
    }
}
