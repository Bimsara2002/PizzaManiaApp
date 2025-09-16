package com.example.pizzamaniaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DashboardActivity extends AppCompatActivity {

    CardView btnAdmin, btnCustomer;
    ImageButton toggleAdminButton;
    boolean isAdminVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnAdmin = findViewById(R.id.adminCard);
        btnCustomer = findViewById(R.id.customerCard);
        toggleAdminButton = findViewById(R.id.toggleAdminButton);

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, AdminLogin.class));
            }
        });

        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            }
        });

        toggleAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdminVisible) {
                    btnAdmin.setVisibility(View.GONE);
                    toggleAdminButton.setImageResource(android.R.drawable.arrow_up_float);
                    isAdminVisible = false;
                } else {
                    btnAdmin.setVisibility(View.VISIBLE);
                    toggleAdminButton.setImageResource(android.R.drawable.arrow_down_float);
                    isAdminVisible = true;
                }
            }
        });
    }
}
