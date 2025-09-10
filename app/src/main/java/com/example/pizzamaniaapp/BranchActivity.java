package com.example.pizzamaniaapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class BranchActivity extends AppCompatActivity {

    Button btnColomboMap, btnGalleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);

        btnColomboMap = findViewById(R.id.btnColomboMap);
        btnGalleMap = findViewById(R.id.btnGalleMap);

        // Colombo Branch Location (Google Maps link)
        btnColomboMap.setOnClickListener(v -> {
            String mapUrl = "geo:0,0?q=No.123,+Galle+Road,+Colombo";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });

        // Galle Branch Location
        btnGalleMap.setOnClickListener(v -> {
            String mapUrl = "geo:0,0?q=No.45,+Main+Street,+Galle";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });
    }
}