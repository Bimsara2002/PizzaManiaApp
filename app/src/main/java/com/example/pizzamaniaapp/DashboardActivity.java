package com.example.pizzamaniaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DashboardActivity extends AppCompatActivity {

    CardView adminCard, customerCard;
    ImageButton toggleAdminButton;

    boolean isAdminVisible = false; // track visibility state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Link XML views
        adminCard = findViewById(R.id.adminCard);
        customerCard = findViewById(R.id.customerCard);
        toggleAdminButton = findViewById(R.id.toggleAdminButton);

        // Start with admin hidden
        adminCard.setVisibility(View.GONE);

        // Toggle button click
        toggleAdminButton.setOnClickListener(v -> {
            if (isAdminVisible) {
                // Hide admin card with animation
                adminCard.animate()
                        .translationY(adminCard.getHeight())
                        .alpha(0f)
                        .setDuration(300)
                        .withEndAction(() -> adminCard.setVisibility(View.GONE))
                        .start();

                toggleAdminButton.setImageResource(android.R.drawable.arrow_up_float);
                isAdminVisible = false;
            } else {
                // Show admin card with animation
                adminCard.setVisibility(View.VISIBLE);
                adminCard.setAlpha(0f);
                adminCard.setTranslationY(adminCard.getHeight());
                adminCard.animate()
                        .translationY(0)
                        .alpha(1f)
                        .setDuration(300)
                        .start();

                toggleAdminButton.setImageResource(android.R.drawable.arrow_down_float);
                isAdminVisible = true;
            }
        });

        // Admin card click → AdminLogin
        adminCard.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, AdminLogin.class)));

        // Customer card click → LoginActivity
        customerCard.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class)));
    }
}
