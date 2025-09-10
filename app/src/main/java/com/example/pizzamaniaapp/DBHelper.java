package com.example.pizzamaniaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                "category TEXT, " +
                "branch TEXT)");

        // Cart Table
        MyDB.execSQL("CREATE TABLE cart(" +
                "cart_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "item_id INTEGER, " +
                "quantity INTEGER, " +
                "FOREIGN KEY(username) REFERENCES users(username), " +
                "FOREIGN KEY(item_id) REFERENCES menu(item_id))");

        // Orders
        MyDB.execSQL("CREATE TABLE orders(" +
                "order_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "total_price REAL NOT NULL, " +
                "status TEXT DEFAULT 'Pending', " +
                "order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(username) REFERENCES users(username))");

        // Order items
        MyDB.execSQL("CREATE TABLE order_items(" +
                "order_item_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_id INTEGER NOT NULL, " +
                "item_id INTEGER NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "FOREIGN KEY(order_id) REFERENCES orders(order_id), " +
                "FOREIGN KEY(item_id) REFERENCES menu(item_id))");

        //Insert Data
        MyDB.execSQL("INSERT INTO menu (name, description, price, image_url, category, branch) VALUES " +
                "('Chicken Pizza', 'Spicy chicken with cheese', 1200, 'sample_pizza', 'Pizza','Colombo')," +
                "('Veggie Delight', 'Fresh vegetables and cheese', 950, 'veggie_pizza', 'Pizza','Galle')," +
                "('Coca-Cola 1L', 'Chilled soft drink', 350, 'coke', 'Drinks','Colombo')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop table if exists users");
        MyDB.execSQL("DROP TABLE IF EXISTS menu");
        MyDB.execSQL("DROP TABLE IF EXISTS cart");
        MyDB.execSQL("DROP TABLE IF EXISTS orders");
        MyDB.execSQL("DROP TABLE IF EXISTS order_items");
        onCreate(MyDB);
    }
       //Insert User
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
    public boolean addProduct(String name, String description, double price, String imageUrl,String category,String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        values.put("image_url", imageUrl);
        values.put("category",category);
        values.put("location", location);

        long result = db.insert("menu", null, values);
        return result != -1;
    }

    public boolean updateProduct(int id, String name, String description, double price, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        values.put("image_url", imageUrl);

        int result = db.update("menu", values, "item_id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }
    public boolean deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("menu", "item_id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    /*
    //create order
    public long createOrder(String username, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues orderValues = new ContentValues();
        orderValues.put("username", username);
        orderValues.put("total_price", totalPrice);
        return db.insert("orders", null, orderValues);
    }

    // 2. Add items to order
    public boolean addOrderItem(long orderId, int itemId, int qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("order_id", orderId);
        values.put("item_id", itemId);
        values.put("quantity", qty);
        long result = db.insert("order_items", null, values);
        return result != -1;
    }

    // 3. Get all orders of a user
    public Cursor getUserOrders(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM orders WHERE username=? ORDER BY order_time DESC",
                new String[]{username});
    }

    // 4. Get all items for a specific order
    public Cursor getOrderItems(long orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT oi.quantity, m.name, m.price, m.image_url " +
                        "FROM order_items oi INNER JOIN menu m ON oi.item_id=m.item_id " +
                        "WHERE oi.order_id=?",
                new String[]{String.valueOf(orderId)});
    }
    */

    // Get all orders for a specific user
    public Cursor getOrdersByUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM orders WHERE username=? ORDER BY order_time DESC", new String[]{username});
    }

    // Insert new order
    public boolean insertOrder(String username, double totalPrice, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("total_price", totalPrice);
        values.put("status", status);
        values.put("order_time", System.currentTimeMillis()); // save timestamp

        long result = db.insert("orders", null, values);
        return result != -1;
    }

    // Get all orders (for Admin)
    public Cursor getAllOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM orders ORDER BY order_time DESC", null);
    }

    // Update order status
    public boolean updateOrderStatus(int orderId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);

        int result = db.update("orders", values, "order_id=?", new String[]{String.valueOf(orderId)});
        return result > 0;
    }

    // Get menu items by category + branch
    public Cursor getMenuByCategoryAndBranch(String category, String branch) {
        SQLiteDatabase db = this.getReadableDatabase();

        if (category.equals("All")) {
            return db.rawQuery("SELECT * FROM menu WHERE branch=?", new String[]{branch});
        } else {
            return db.rawQuery("SELECT * FROM menu WHERE category=? AND branch=?", new String[]{category, branch});
        }
    }


}
