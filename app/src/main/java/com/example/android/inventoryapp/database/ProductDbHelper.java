package com.example.android.inventoryapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDbHelper extends SQLiteOpenHelper {

    // Static constants
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "products.db";

    // CREATE TABLE product.db (_id INTEGER PRIMARY KEY AUTOINCREMENT,
    // product_name TEXT, price INTEGER, quantity INTEGER, supplier_name TEXT,
    // supplier_phone_number TEXT );

    // Constructor
    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE products (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " Product_name TEXT NOT NULL," +
                " Price REAL NOT NULL," +
                " Quantity INTEGER NOT NULL," +
                " Supplier_name TEXT NOT NULL," +
                " Supplier_phone_number TEXT NOT NULL );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
