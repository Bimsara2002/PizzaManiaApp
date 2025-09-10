package com.example.pizzamaniaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerManageAccount extends AppCompatActivity {

    EditText etEmail, etPassword, etMobile;
    Button btnUpdateAccount, btnDeleteAccount;
    DBHelper DB;
    String currentUser = "Pehsara"; // Replace with logged-in username (pass via Intent or SharedPreferences)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_manage_account);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etMobile = findViewById(R.id.etMobile);
        btnUpdateAccount = findViewById(R.id.btnUpdateAccount);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

        DB = new DBHelper(this);

        //  Update user
        btnUpdateAccount.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String mobile = etMobile.getText().toString();

            if (email.isEmpty() || password.isEmpty() || mobile.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = DB.updateUser(currentUser, email, password, mobile);
            if (updated) {
                Toast.makeText(this, "Account updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show();
            }
        });

        //  Delete user
        btnDeleteAccount.setOnClickListener(v -> {
            boolean deleted = DB.deleteUser(currentUser);
            if (deleted) {
                Toast.makeText(this, "Account deleted successfully!", Toast.LENGTH_SHORT).show();
                // Redirect to Login after account deletion
                startActivity(new Intent(CustomerManageAccount.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
