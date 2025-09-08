package com.example.pizzamaniaapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminOrdersActivity extends AppCompatActivity {

    RecyclerView ordersRecyclerView;
    AdminOrdersAdapter adapter;
    DBHelper dbHelper;

    ArrayList<Integer> orderIds;
    ArrayList<String> usernames, orderStatuses, orderTotals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        dbHelper = new DBHelper(this);

        orderIds = new ArrayList<>();
        usernames = new ArrayList<>();
        orderStatuses = new ArrayList<>();
        orderTotals = new ArrayList<>();

        loadOrders();

        adapter = new AdminOrdersAdapter(this, orderIds, usernames, orderStatuses, orderTotals, dbHelper);
        ordersRecyclerView.setAdapter(adapter);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadOrders() {
        Cursor cursor = dbHelper.getAllOrders();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No orders found", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("order_id"));
            String user = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total_price"));

            orderIds.add(id);
            usernames.add(user);
            orderStatuses.add(status);
            orderTotals.add("Rs. " + total);
        }
        cursor.close();
    }
}
