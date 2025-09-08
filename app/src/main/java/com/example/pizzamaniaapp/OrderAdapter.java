package com.example.pizzamaniaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private ArrayList<Integer> orderIds;
    private ArrayList<String> statuses;
    private ArrayList<String> totals;
    private ArrayList<String> times;

    public OrderAdapter(Context context, ArrayList<Integer> orderIds,
                        ArrayList<String> statuses, ArrayList<String> totals,
                        ArrayList<String> times) {
        this.context = context;
        this.orderIds = orderIds;
        this.statuses = statuses;
        this.totals = totals;
        this.times = times;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ordered_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.tvOrderId.setText("Order #" + orderIds.get(position));
        holder.tvOrderStatus.setText("Status: " + statuses.get(position));
        holder.tvOrderTotal.setText("Total: Rs. " + totals.get(position));
        holder.tvOrderTime.setText("Ordered at: " + times.get(position));
    }

    @Override
    public int getItemCount() {
        return orderIds.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderStatus, tvOrderTotal, tvOrderTime;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvOrderTime = itemView.findViewById(R.id.tvOrderTime);
        }
    }
}
