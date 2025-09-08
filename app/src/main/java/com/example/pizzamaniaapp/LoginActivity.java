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

        etUsername=findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);

        DB = new DBHelper(this); // initialize database

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String pass = etPassword.getText().toString();

            if (username.equals("") || pass.equals("")) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else {
                Boolean checkUserPass = DB.checkUsernamePassword(username, pass);
                if (checkUserPass) {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MenuActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGoRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}
