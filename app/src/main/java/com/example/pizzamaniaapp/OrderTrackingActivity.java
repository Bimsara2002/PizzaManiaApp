package com.example.pizzamaniaapp;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderTrackingActivity extends AppCompatActivity {

    RecyclerView ordersRecyclerView;
    OrderAdapter orderAdapter;
    DBHelper dbHelper;
    String currentUser = "Pehsara";

    ArrayList<Integer> orderIds;
    ArrayList<String> orderStatuses;
    ArrayList<String> orderTotals;
    ArrayList<String> orderTimes;
    Handler handler = new Handler(); // for auto-refresh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        dbHelper = new DBHelper(this);

        orderIds = new ArrayList<>();
        orderStatuses = new ArrayList<>();
        orderTotals = new ArrayList<>();
        orderTimes = new ArrayList<>();

        loadOrders();

        // auto refresh every 5 seconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadOrders();
                handler.postDelayed(this, 5000);
            }
        }, 5000);

        orderAdapter = new OrderAdapter(this, orderIds, orderStatuses, orderTotals, orderTimes);
        ordersRecyclerView.setAdapter(orderAdapter);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadOrders() {
        Cursor cursor = dbHelper.getOrdersByUser(currentUser);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No orders found!", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            orderIds.add(cursor.getInt(cursor.getColumnIndexOrThrow("order_id")));
            orderStatuses.add(cursor.getString(cursor.getColumnIndexOrThrow("status")));
            orderTotals.add(cursor.getString(cursor.getColumnIndexOrThrow("total_price")));
            orderTimes.add(cursor.getString(cursor.getColumnIndexOrThrow("order_time")));
        }
        cursor.close();
    }
}
