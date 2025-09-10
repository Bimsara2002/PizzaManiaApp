package com.example.pizzamaniaapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteProduct extends AppCompatActivity {

    EditText etId;
    Button btnDelete;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        etId = findViewById(R.id.etId);
        btnDelete = findViewById(R.id.btnDelete);

        DB = new DBHelper(this);

        btnDelete.setOnClickListener(v -> {
            String idStr = etId.getText().toString();
            if (idStr.isEmpty()) {
                Toast.makeText(this, "Enter product ID", Toast.LENGTH_SHORT).show();
                return;
            }

            int id = Integer.parseInt(idStr);
            boolean success = DB.deleteProduct(id);

            if (success) {
                Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
