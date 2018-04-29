package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.inventoryapp.database.ProductContract;
import com.example.android.inventoryapp.database.ProductDbHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertData();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Cursor readDatabase = queryData();
        int productIdColumnIndex = readDatabase.getColumnIndex(ProductContract.Product.COLUMN_ID);
        int productNameColumnindex = readDatabase.getColumnIndex(ProductContract.Product.COLUMN_PRODUCT_NAME);
        int productPriceColumnIndex = readDatabase.getColumnIndex(ProductContract.Product.COLUMN_PRICE);
        int productQuantityColumnIndex = readDatabase.getColumnIndex(ProductContract.Product.COLUMN_QUANTITY);
        int supplierNameColumnIndex = readDatabase.getColumnIndex(ProductContract.Product.COLUMN_SUPPLIER_NAME);
        int supplierPhoneColumnIndex = readDatabase.getColumnIndex(ProductContract.Product.COLUMN_SUPPLIER_PHONE_NUMBER);
    }

    private void insertData() {
        // Insert into database.
        ProductDbHelper dbHelper = new ProductDbHelper(this.getBaseContext());
        SQLiteDatabase writeProductDatabase = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductContract.Product.COLUMN_PRODUCT_NAME, "first product");
        values.put(ProductContract.Product.COLUMN_PRICE, 1);
        values.put(ProductContract.Product.COLUMN_QUANTITY, 21);
        values.put(ProductContract.Product.COLUMN_SUPPLIER_NAME, "First supplier");
        values.put(ProductContract.Product.COLUMN_SUPPLIER_PHONE_NUMBER, "123456789");

        writeProductDatabase.insert(ProductContract.Product.TABLE_NAME, null, values);
        Log.i("Mihai's database", "Filled with dummy data.");
    }

    private Cursor queryData() {
        ProductDbHelper dbHelper = new ProductDbHelper(this.getBaseContext());
        SQLiteDatabase readProductDatabase = dbHelper.getReadableDatabase();

        String[] projections = {ProductContract.Product.COLUMN_ID,
                ProductContract.Product.COLUMN_PRODUCT_NAME,
                ProductContract.Product.COLUMN_PRICE,
                ProductContract.Product.COLUMN_QUANTITY,
                ProductContract.Product.COLUMN_SUPPLIER_NAME,
                ProductContract.Product.COLUMN_SUPPLIER_PHONE_NUMBER};

        Cursor cursor = readProductDatabase.query(
                ProductContract.Product.TABLE_NAME,
                projections,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }
}
