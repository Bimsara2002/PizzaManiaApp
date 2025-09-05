package com.example.pizzamaniaapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
        btnViewUsers = findViewById(R.id.btnViewUsers);

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

        // View registered users
        btnViewUsers.setOnClickListener(v -> {
            Cursor cursor = DB.getAllUsers();
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

            new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("Registered Users")
                    .setMessage(builder.toString())
                    .setPositiveButton("OK", null)
                    .show();
        });

        // Go to Login screen
        btnGoLogin.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });
    }
}
