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
    String currentUser = "Pehesara";

    ArrayList<Integer> orderIds = new ArrayList<>();
    ArrayList<String> orderStatuses = new ArrayList<>();
    ArrayList<String> orderTotals = new ArrayList<>();
    ArrayList<String> orderTimes = new ArrayList<>();

    Handler handler = new Handler(); // for auto-refresh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        dbHelper = new DBHelper(this);

        orderAdapter = new OrderAdapter(this, orderIds, orderStatuses, orderTotals, orderTimes);
        ordersRecyclerView.setAdapter(orderAdapter);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadOrders();

        // auto-refresh every 5 seconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadOrders();
                handler.postDelayed(this, 5000);
            }
        }, 5000);
    }

    private void loadOrders() {
        Cursor cursor = dbHelper.getOrdersByUser(currentUser);

        // Clear previous data
        orderIds.clear();
        orderStatuses.clear();
        orderTotals.clear();
        orderTimes.clear();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                orderIds.add(cursor.getInt(cursor.getColumnIndexOrThrow("order_id")));
                orderStatuses.add(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                orderTotals.add(cursor.getString(cursor.getColumnIndexOrThrow("total_price")));
                orderTimes.add(cursor.getString(cursor.getColumnIndexOrThrow("order_time")));
            }
        }
        if(cursor != null) cursor.close();

        orderAdapter.notifyDataSetChanged();
    }
}
