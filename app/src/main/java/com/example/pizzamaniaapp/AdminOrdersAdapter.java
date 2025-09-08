package com.example.pizzamaniaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminOrdersAdapter extends RecyclerView.Adapter<AdminOrdersAdapter.OrderViewHolder> {

    private Context context;
    private ArrayList<Integer> orderIds;
    private ArrayList<String> usernames, statuses, totals;
    private DBHelper dbHelper;

    public AdminOrdersAdapter(Context context, ArrayList<Integer> orderIds,
                              ArrayList<String> usernames, ArrayList<String> statuses,
                              ArrayList<String> totals, DBHelper dbHelper) {
        this.context = context;
        this.orderIds = orderIds;
        this.usernames = usernames;
        this.statuses = statuses;
        this.totals = totals;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_admin, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.username.setText("User: " + usernames.get(position));
        holder.total.setText(totals.get(position));
        holder.status.setText("Status: " + statuses.get(position));

        int orderId = orderIds.get(position);

        holder.pendingBtn.setOnClickListener(v -> updateStatus(orderId, "Pending"));
        holder.preparingBtn.setOnClickListener(v -> updateStatus(orderId, "Preparing"));
        holder.outForDeliveryBtn.setOnClickListener(v -> updateStatus(orderId, "Out for Delivery"));
        holder.deliveredBtn.setOnClickListener(v -> updateStatus(orderId, "Delivered"));
    }

    private void updateStatus(int orderId, String newStatus) {
        boolean updated = dbHelper.updateOrderStatus(orderId, newStatus);
        if (updated) {
            Toast.makeText(context, "Order status updated to " + newStatus, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to update order", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return orderIds.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView username, total, status;
        Button pendingBtn, preparingBtn, outForDeliveryBtn, deliveredBtn;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.orderUser);
            total = itemView.findViewById(R.id.orderTotal);
            status = itemView.findViewById(R.id.orderStatus);
            pendingBtn = itemView.findViewById(R.id.btnPending);
            preparingBtn = itemView.findViewById(R.id.btnPreparing);
            outForDeliveryBtn = itemView.findViewById(R.id.btnOutForDelivery);
            deliveredBtn = itemView.findViewById(R.id.btnDelivered);
        }
    }
}
