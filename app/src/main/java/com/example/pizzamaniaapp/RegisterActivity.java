package com.example.pizzamaniaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etMobile;
    Button btnRegister, btnGoLogin, btnViewUsers;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etMobile = findViewById(R.id.etMobile);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoLogin = findViewById(R.id.btnGoLogin);


        DB = new DBHelper(this);

        // Register user
        btnRegister.setOnClickListener(v -> {
            String user = etUsername.getText().toString();
            String pass = etPassword.getText().toString();
            String mob = etMobile.getText().toString();

            if (user.equals("") || pass.equals("") || mob.equals("")) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                Boolean checkUser = DB.checkUsername(user);
                if (!checkUser) {
                    Boolean insert = DB.insertData(user, pass, mob);
                    if (insert) {
                        Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    } else {
                        Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "User already exists! Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        });



        // Go to Login screen
        btnGoLogin.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });
    }
}
