package com.example.pizzamaniaapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    Button btnAddProduct, btnUpdateProduct, btnDeleteProduct, btnViewUsers;
    DBHelper dbHelper;  // <-- add this

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        dbHelper = new DBHelper(this); // <-- initialize the DBHelper


        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
        btnViewUsers = findViewById(R.id.btnViewUsers);


        btnAddProduct.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, AddProduct.class));
        });

        btnUpdateProduct.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, UpdateProduct.class));
        });

        btnDeleteProduct.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, DeleteProduct.class));
        });


        btnViewUsers.setOnClickListener(v -> {
            Cursor cursor = dbHelper.getAllUsers(); // <-- use dbHelper, not DB
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No users found", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder builder = new StringBuilder();
            while (cursor.moveToNext()) {
                builder.append("Username: ").append(cursor.getString(0)).append("\n");
                builder.append("Password: ").append(cursor.getString(1)).append("\n");
                builder.append("Mobile: ").append(cursor.getString(2)).append("\n\n");
            }

            new AlertDialog.Builder(AdminActivity.this)
                    .setTitle("Registered Users")
                    .setMessage(builder.toString())
                    .setPositiveButton("OK", null)
                    .show();
        });
    }
}
