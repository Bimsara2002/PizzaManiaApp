package com.example.pizzamaniaapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;
import lk.payhere.androidsdk.model.StatusResponse;
import lk.payhere.androidsdk.PHMainActivity;

public class Customer_Checkout_Activity extends AppCompatActivity {

    private static final String TAG = "PayHereDemo";
    private static final int PAYHERE_REQUEST = 11001;

    TextView orderSummary, totalAmount, textView;
    Button payNowBtn, codBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_checkout);

        orderSummary = findViewById(R.id.orderSummary);
        totalAmount = findViewById(R.id.totalAmount);
        payNowBtn = findViewById(R.id.payNowBtn);
        codBtn = findViewById(R.id.codBtn);
        textView = findViewById(R.id.textView);

        // Receive total amount from previous activity
        double total = getIntent().getDoubleExtra("TOTAL_AMOUNT", 0);
        totalAmount.setText("Total: Rs. " + total);

        // Handle Pay Now (Online Payment)
        payNowBtn.setOnClickListener(view -> {
            Log.d("Checkout", "Total amount: " + total);
            initiatePayment(total);
        });

        // Handle Cash on Delivery
        codBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Order placed with Cash on Delivery!", Toast.LENGTH_LONG).show();
            textView.setText("Order placed successfully with Cash on Delivery!");
            // TODO: Save order in DB here
        });
    }

    private void initiatePayment(double total) {
        InitRequest req = new InitRequest();
        req.setMerchantId("1232011");
        req.setCurrency("LKR");
        req.setAmount(total);
        req.setOrderId("230000123");
        req.setItemsDescription("Pizza Order");
        req.setCustom1("Custom message 1");
        req.setCustom2("Custom message 2");

        // Customer details
        req.getCustomer().setFirstName("Pasindu");
        req.getCustomer().setLastName("Manahara");
        req.getCustomer().setEmail("testuser@gmail.com");
        req.getCustomer().setPhone("+94771234567");
        req.getCustomer().getAddress().setAddress("No.1, Galle Road");
        req.getCustomer().getAddress().setCity("Colombo");
        req.getCustomer().getAddress().setCountry("Sri Lanka");

        // Delivery details
        req.getCustomer().getDeliveryAddress().setAddress("No.2, Horana Road");
        req.getCustomer().getDeliveryAddress().setCity("Bulathsinhala");
        req.getCustomer().getDeliveryAddress().setCountry("Sri Lanka");

        // Add item
        req.getItems().add(new Item(null, "Pizza", 1, total));

        // Optional: Add notify URL from PayHere Merchant Account
        req.setNotifyUrl("https://yourbackend.com/notify");

        // Set PayHere Sandbox environment
        PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);

        Intent intent = new Intent(this, PHMainActivity.class);
        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
        startActivityForResult(intent, PAYHERE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
            Serializable serializable = data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
            if (serializable instanceof PHResponse) {
                PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) serializable;
                if (response.isSuccess()) {
                    String msg = "Payment Success ";
                    Log.d(TAG, msg);
                    textView.setText(msg);
                } else {
                    String msg = "Payment Failed ";
                    Log.d(TAG, msg);
                    textView.setText(msg);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            textView.setText("User Cancelled the request ");
        }
    }
}
