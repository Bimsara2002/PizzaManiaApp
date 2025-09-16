package com.example.pizzamaniaapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewUsersActivity extends AppCompatActivity {

    DBHelper dbHelper;
    TextView tvUsers;

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

        if (cursor == null || cursor.getCount() == 0) {
            tvUsers.setText("No users found");
            return;
        }

        StringBuilder builder = new StringBuilder();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));   // make sure DB has column `id`
            String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String mobile = cursor.getString(cursor.getColumnIndexOrThrow("mobile"));

            builder.append("ID: ").append(id).append("\n")
                    .append("Username: ").append(username).append("\n")
                    .append("Email: ").append(email).append("\n")
                    .append("Mobile: ").append(mobile).append("\n\n");
        }
        cursor.close();

        tvUsers.setText(builder.toString());
    }
}
