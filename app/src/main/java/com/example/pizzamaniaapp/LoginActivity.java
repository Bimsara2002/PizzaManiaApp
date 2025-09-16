package com.example.pizzamaniaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnGoRegister;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);

        // Initialize DBHelper
        DB = new DBHelper(this);

        // Login button logic
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (username.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean checkUserPass = DB.checkUsernamePassword(username, pass);
                if (checkUserPass) {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    // ✅ Pass username to MenuActivity
                    Intent intent = new Intent(this, MenuActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Go to Register screen
        btnGoRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}
