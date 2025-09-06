package com.example.pizzamaniaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    Button btnAddProduct, btnUpdateProduct, btnDeleteProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize buttons
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);

        // Set click listeners
        btnAddProduct.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, AddProduct.class));
        });

        btnUpdateProduct.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, UpdateProduct.class));
        });

        btnDeleteProduct.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, DeleteProduct.class));
        });
    }
}