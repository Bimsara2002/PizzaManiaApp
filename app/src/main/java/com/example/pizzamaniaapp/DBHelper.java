package com.example.pizzamaniaapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "PizzaMania.db";


    public DBHelper(Context context) {
        super(context, DBNAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create table users(username TEXT primary key, password TEXT, mobile TEXT)");

        //Create menu Table
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

        //Insert Data
        MyDB.execSQL("INSERT INTO menu (name, description, price, image_url, category) VALUES " +
                "('Chicken Pizza', 'Spicy chicken with cheese', 1200, 'sample_pizza', 'Pizza')," +
                "('Veggie Delight', 'Fresh vegetables and cheese', 950, 'veggie_pizza', 'Pizza')," +
                "('Coca-Cola 1L', 'Chilled soft drink', 350, 'coke', 'Drinks')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop table if exists users");
        MyDB.execSQL("DROP TABLE IF EXISTS menu");
        MyDB.execSQL("DROP TABLE IF EXISTS cart");
        onCreate(MyDB);
    }

    public Boolean insertData(String username, String password, String mobile) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("mobile", mobile);
        long result = MyDB.insert("users", null, values);
        return result != -1;
    }

    public boolean addToCart(String username, int itemId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("item_id", itemId);
        cv.put("quantity", quantity);
        long result = db.insert("cart", null, cv);
        return result != -1;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users", null);
    }

    public Cursor getAllMenuItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM menu", null);
    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery(" Select * from users where username = ?", new String[]{username});
        return cursor.getCount() > 0;
    }

    // Get Cart Items for a User
    public Cursor getCartItems(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT c.cart_id, m.name, m.price, m.image_url, c.quantity " +
                "FROM cart c INNER JOIN menu m ON c.item_id = m.item_id " +
                "WHERE c.username = ?", new String[]{username});
    }

    // Clear Cart
    public void clearCart(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cart", "username=?", new String[]{username});
    }

    public Boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[]{username, password});
        return cursor.getCount() > 0;
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
}
