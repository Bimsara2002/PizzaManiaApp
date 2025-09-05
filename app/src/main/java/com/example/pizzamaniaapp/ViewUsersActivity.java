package com.example.pizzamaniaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class ViewUsersActivity extends AppCompatActivity {

    DBHelper dbHelper;  // <-- Reference to your DB helper
    TextView tvUsers;         // <-- To show results

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        dbHelper = new DBHelper(this);
        tvUsers = findViewById(R.id.tvUsers);

        displayUsers();
    }

    private void displayUsers() {
        Cursor cursor = dbHelper.getAllUsers();

        if (cursor.getCount() == 0) {
            tvUsers.setText("No users found");
            return;
        }

        StringBuilder builder = new StringBuilder();
        while (cursor.moveToNext()) {
            builder.append("ID: ").append(cursor.getInt(0)).append("\n")
                    .append("Username: ").append(cursor.getString(1)).append("\n")
                    .append("Email: ").append(cursor.getString(2)).append("\n")
                    .append("Mobile: ").append(cursor.getString(3)).append("\n\n");
        }
        tvUsers.setText(builder.toString());
    }
}
