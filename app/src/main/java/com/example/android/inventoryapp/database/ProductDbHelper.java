package com.example.android.inventoryapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDbHelper extends SQLiteOpenHelper {

    // Static constants
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "products.db";

    private static final String SPACE = " ";
    private static final String COMMA = ",";
    private static final String NOT_NULL = "NOT NULL";
    private static final String TEXT = "TEXT";
    private static final String INTEGER = "INTEGER";

    // CREATE TABLE product.db (_id INTEGER PRIMARY KEY AUTOINCREMENT,
    // product_name TEXT, price INTEGER, quantity INTEGER, supplier_name TEXT,
    // supplier_phone_number TEXT );
    private static final String CREATE_DATABASE =
            new StringBuilder().append("CREATE TABLE").append(SPACE)
                    .append(ProductContract.Product.TABLE_NAME).append(SPACE).append("(")
                    .append(ProductContract.Product.COLUMN_ID).append(SPACE).append(INTEGER).append(" PRIMARY KEY AUTOINCREMENT").append(COMMA)
                    .append(SPACE).append(ProductContract.Product.COLUMN_PRODUCT_NAME).append(SPACE).append(TEXT).append(SPACE).append(NOT_NULL).append(COMMA)
                    .append(SPACE).append(ProductContract.Product.COLUMN_PRICE).append(SPACE).append(INTEGER).append(SPACE).append(NOT_NULL).append(COMMA)
                    .append(SPACE).append(ProductContract.Product.COLUMN_QUANTITY).append(SPACE).append(INTEGER).append(SPACE).append(NOT_NULL).append(COMMA)
                    .append(SPACE).append(ProductContract.Product.COLUMN_SUPPLIER_NAME).append(SPACE).append(TEXT).append(SPACE).append(NOT_NULL).append(COMMA)
                    .append(SPACE).append(ProductContract.Product.COLUMN_SUPPLIER_PHONE_NUMBER).append(SPACE).append(SPACE).append(NOT_NULL).append(TEXT)
                    .append(SPACE).append(");").toString();

    // Constructor
    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
