package com.example.pizzamaniaapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    RecyclerView menuRecyclerView;
    MenuAdaptor adapter;
    DBHelper dbHelper;

    ArrayList<Integer> itemIds;
    ArrayList<String> foodNames;
    ArrayList<String> foodPrices;
    ArrayList<String> foodImages;

    String currentUser = "Pehesara"; // replace with logged-in username

    EditText searchBar;
    ImageButton searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Drawer + toolbar setup
        DrawerLayout drawerLayout = findViewById(R.id.mainDrawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Already here
            } else if (id == R.id.nav_orders) {
                startActivity(new Intent(MenuActivity.this, OrderTrackingActivity.class));
            } else if (id == R.id.nav_branches) {
                startActivity(new Intent(MenuActivity.this, BranchActivity.class));
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(MenuActivity.this, CustomerManageAccount.class));
            } else if (id == R.id.nav_logout) {
                finish();
            }
            drawerLayout.closeDrawers();
            return true;
        });

        // Initialize RecyclerView + DB
        menuRecyclerView = findViewById(R.id.menuRecyclerView);
        dbHelper = new DBHelper(this);

        itemIds = new ArrayList<>();
        foodNames = new ArrayList<>();
        foodPrices = new ArrayList<>();
        foodImages = new ArrayList<>();

        adapter = new MenuAdaptor(this, itemIds, foodNames, foodPrices, foodImages, currentUser);
        menuRecyclerView.setAdapter(adapter);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Category buttons
        Button allBtn = findViewById(R.id.allBtn);
        Button pizzaBtn = findViewById(R.id.pizzaBtn);
        Button drinksBtn = findViewById(R.id.drinksBtn);
        Button sidesBtn = findViewById(R.id.sidesBtn);

        allBtn.setOnClickListener(v -> loadMenu("All"));
        pizzaBtn.setOnClickListener(v -> loadMenu("Pizza"));
        drinksBtn.setOnClickListener(v -> loadMenu("Drinks"));
        sidesBtn.setOnClickListener(v -> loadMenu("Sides"));

        // Floating buttons
        FloatingActionButton cartFab = findViewById(R.id.cartFab);
        FloatingActionButton orderStatusBtn = findViewById(R.id.orderStatusBtn);

        cartFab.setOnClickListener(v ->
                startActivity(new Intent(MenuActivity.this, CartActivity.class))
        );
        orderStatusBtn.setOnClickListener(v ->
                startActivity(new Intent(MenuActivity.this, OrderTrackingActivity.class))
        );

        // Search
        searchBar = findViewById(R.id.searchBar);
        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(v -> {
            String query = searchBar.getText().toString().trim();
            if (!query.isEmpty()) {
                loadSearchResults(query);
            } else {
                loadMenu("All");
            }
        });

        // Load initial data
        loadMenu("All");
    }

    private void loadMenu(String category) {
        ArrayList<Integer> newIds = new ArrayList<>();
        ArrayList<String> newNames = new ArrayList<>();
        ArrayList<String> newPrices = new ArrayList<>();
        ArrayList<String> newImages = new ArrayList<>();

        Cursor cursor = dbHelper.getMenuByCategory(category);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                newIds.add(cursor.getInt(cursor.getColumnIndexOrThrow("item_id")));
                newNames.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                newPrices.add(cursor.getString(cursor.getColumnIndexOrThrow("price")));
                newImages.add(cursor.getString(cursor.getColumnIndexOrThrow("image_url")));
            }
            cursor.close();
        }

        adapter.updateData(newIds, newNames, newPrices, newImages);
    }

    private void loadSearchResults(String query) {
        ArrayList<Integer> newIds = new ArrayList<>();
        ArrayList<String> newNames = new ArrayList<>();
        ArrayList<String> newPrices = new ArrayList<>();
        ArrayList<String> newImages = new ArrayList<>();

        Cursor cursor = dbHelper.searchMenu(query);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                newIds.add(cursor.getInt(cursor.getColumnIndexOrThrow("item_id")));
                newNames.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                newPrices.add(cursor.getString(cursor.getColumnIndexOrThrow("price")));
                newImages.add(cursor.getString(cursor.getColumnIndexOrThrow("image_url")));
            }
            cursor.close();
        }

        adapter.updateData(newIds, newNames, newPrices, newImages);
    }
}
