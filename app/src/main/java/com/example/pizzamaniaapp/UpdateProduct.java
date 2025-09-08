package com.example.pizzamaniaapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateProduct extends AppCompatActivity {

    EditText etId, etName, etDescription, etPrice, etImage;
    Button btnUpdate;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        etId = findViewById(R.id.etId);
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        etImage = findViewById(R.id.etImage);
        btnUpdate = findViewById(R.id.btnUpdate);

        DB = new DBHelper(this);

        btnUpdate.setOnClickListener(v -> {
            String idStr = etId.getText().toString();
            String name = etName.getText().toString();
            String description = etDescription.getText().toString();
            String priceStr = etPrice.getText().toString();
            String image = etImage.getText().toString();

            if (idStr.isEmpty() || name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "ID, Name, and Price are required", Toast.LENGTH_SHORT).show();
                return;
            }

            int id = Integer.parseInt(idStr);
            double price = Double.parseDouble(priceStr);

            boolean success = DB.updateProduct(id, name, description, price, image);
            if (success) {
                Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
