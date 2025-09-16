package com.example.pizzamaniaapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzamaniaapp.DBHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class DeliveryMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DBHelper dbHelper;
    private ArrayList<LatLng> deliveryLocations = new ArrayList<>();
    private ArrayList<String> orderInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_map);

        dbHelper = new DBHelper(this);
        loadDeliveryLocations();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void loadDeliveryLocations() {
        Cursor cursor = dbHelper.getDeliveryOrders();
        
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No delivery orders found", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            int orderId = cursor.getInt(cursor.getColumnIndexOrThrow("order_id"));
            double lat = cursor.getDouble(cursor.getColumnIndexOrThrow("delivery_lat"));
            double lng = cursor.getDouble(cursor.getColumnIndexOrThrow("delivery_lng"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            
if (lat != 0 && lng != 0) {
                deliveryLocations.add(new LatLng(lat, lng));
                orderInfos.add("Order #" + orderId + " - " + status);
            }
        }
        cursor.close();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (!deliveryLocations.isEmpty()) {
            for (int i = 0; i < deliveryLocations.size(); i++) {
                LatLng location = deliveryLocations.get(i);
                mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(orderInfos.get(i)));
          // Move camera to first location
                if (i == 0) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
                }
            }
        } else {
            Toast.makeText(this, "No delivery locations available", Toast.LENGTH_SHORT).show();
        }
    }
}
  