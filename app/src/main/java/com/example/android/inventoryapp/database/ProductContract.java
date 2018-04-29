package com.example.android.inventoryapp.database;

import android.provider.BaseColumns;

public final class ProductContract {

    // Private constructor
    private ProductContract() {
    }

    // Inner class for table
    public final class Product implements BaseColumns {

        public final static String TABLE_NAME = "products";

        public final static String COLUMN_ID = "_id";
        public final static String COLUMN_PRODUCT_NAME = "Product_name";
        public final static String COLUMN_PRICE = "Price";
        public final static String COLUMN_QUANTITY = "Quantity";
        public final static String COLUMN_SUPPLIER_NAME = "Supplier_name";
        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "Supplier_phone_number";
    }
}
