package com.example.pizzamaniaapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerManageAccount extends AppCompatActivity {

    EditText etAddress, etPassword, etMobile;
    Button btnUpdateAccount, btnDeleteAccount;
    DBHelper DB;

    String currentUser = "Pehsara";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_manage_account);

        etAddress = findViewById(R.id.etAddress);
        etPassword = findViewById(R.id.etPassword);
        etMobile = findViewById(R.id.etMobile);
        btnUpdateAccount = findViewById(R.id.btnUpdateAccount);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

        DB = new DBHelper(this);

        Cursor cursor = DB.getUserDetails(currentUser);
        if (cursor != null && cursor.moveToFirst()) {
            String address = cursor.getString(0);
            String password = cursor.getString(1);
            String mobile = cursor.getString(2);

            etAddress.setText(address);
            etPassword.setText(password);
            etMobile.setText(mobile);

            cursor.close();
        }


        btnUpdateAccount.setOnClickListener(v -> {
            String address = etAddress.getText().toString();
            String password = etPassword.getText().toString();
            String mobile = etMobile.getText().toString();

            if (address.isEmpty() || password.isEmpty() || mobile.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = DB.updateUser(currentUser, address, password, mobile);
            if (updated) {
                Toast.makeText(this, "Account updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show();
            }
        });


        btnDeleteAccount.setOnClickListener(v -> {
            boolean deleted = DB.deleteUser(currentUser);
            if (deleted) {
                Toast.makeText(this, "Account deleted successfully!", Toast.LENGTH_SHORT).show();
                // Redirect to Login
                startActivity(new Intent(CustomerManageAccount.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
