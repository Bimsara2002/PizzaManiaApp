package com.example.pizzamaniaapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "PizzaMania.db";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 2); // upgraded to version 2 (to support menu + cart tables)
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        // Users Table
        MyDB.execSQL("CREATE TABLE users(" +
                "username TEXT PRIMARY KEY, " +
                "password TEXT, " +
                "mobile TEXT)");

        // Menu Table
        MyDB.execSQL("CREATE TABLE menu(" +
                "item_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "price REAL NOT NULL, " +
                "image_url TEXT, " +
                "category TEXT)");

        // Cart Table
        MyDB.execSQL("CREATE TABLE cart(" +
                "cart_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "item_id INTEGER, " +
                "quantity INTEGER, " +
                "FOREIGN KEY(username) REFERENCES users(username), " +
                "FOREIGN KEY(item_id) REFERENCES menu(item_id))");

        // Insert sample data into Menu
        MyDB.execSQL("INSERT INTO menu (name, description, price, image_url, category) VALUES " +
                "('Chicken Pizza', 'Spicy chicken with cheese', 1200, 'sample_pizza', 'Pizza')," +
                "('Veggie Delight', 'Fresh vegetables and cheese', 950, 'veggie_pizza', 'Pizza')," +
                "('Coca-Cola 1L', 'Chilled soft drink', 350, 'coke', 'Drinks')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("DROP TABLE IF EXISTS users");
        MyDB.execSQL("DROP TABLE IF EXISTS menu");
        MyDB.execSQL("DROP TABLE IF EXISTS cart");
        onCreate(MyDB);
    }

    // Insert new user
    public Boolean insertData(String username, String password, String mobile) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("mobile", mobile);
        long result = MyDB.insert("users", null, values);
        return result != -1;
    }

    // Check if username exists
    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
        return cursor.getCount() > 0;
    }

    // Check username + password
    public Boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});
        return cursor.getCount() > 0;
    }

    // Get all users
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users", null);
    }

    // Insert into cart
    public boolean addToCart(String username, int itemId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("item_id", itemId);
        cv.put("quantity", quantity);
        long result = db.insert("cart", null, cv);
        return result != -1;
    }

    // Get all menu items
    public Cursor getAllMenuItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM menu", null);
    }

    // Get cart items for a user
    public Cursor getCartItems(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT c.cart_id, m.name, m.price, m.image_url, c.quantity " +
                "FROM cart c INNER JOIN menu m ON c.item_id = m.item_id " +
                "WHERE c.username = ?", new String[]{username});
    }

    // Clear cart for a user
    public void clearCart(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cart", "username=?", new String[]{username});
    }

    // Get menu items by category
    public Cursor getMenuByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (category.equals("All")) {
            return db.rawQuery("SELECT * FROM menu", null);
        } else {
            return db.rawQuery("SELECT * FROM menu WHERE category = ?", new String[]{category});
        }
    }

    // ✅ Insert new product into Menu table
    public boolean addProduct(String name, String description, double price, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("description", description);
        cv.put("price", price);
        cv.put("image_url", imageUrl);
        cv.put("category", "Custom"); // default category

        long result = db.insert("menu", null, cv);
        return result != -1;
    }
}
