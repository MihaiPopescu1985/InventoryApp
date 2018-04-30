package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

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

        Cursor readDatabaseCursor = queryData();
        int productIdColumnIndex = readDatabaseCursor.getColumnIndex(ProductContract.Product.COLUMN_ID);
        int productNameColumnIndex = readDatabaseCursor.getColumnIndex(ProductContract.Product.COLUMN_PRODUCT_NAME);
        int productPriceColumnIndex = readDatabaseCursor.getColumnIndex(ProductContract.Product.COLUMN_PRICE);
        int productQuantityColumnIndex = readDatabaseCursor.getColumnIndex(ProductContract.Product.COLUMN_QUANTITY);
        int supplierNameColumnIndex = readDatabaseCursor.getColumnIndex(ProductContract.Product.COLUMN_SUPPLIER_NAME);
        int supplierPhoneColumnIndex = readDatabaseCursor.getColumnIndex(ProductContract.Product.COLUMN_SUPPLIER_PHONE_NUMBER);

        Log.i("Mihai's database", "Database content :");
        while (readDatabaseCursor.moveToNext()) {
            Log.i(String.valueOf(productIdColumnIndex), String.valueOf(readDatabaseCursor.getInt(productIdColumnIndex)));
            Log.i(String.valueOf(productNameColumnIndex), readDatabaseCursor.getString(productNameColumnIndex));
            Log.i(String.valueOf(productPriceColumnIndex), String.valueOf(readDatabaseCursor.getInt(productPriceColumnIndex)));
            Log.i(String.valueOf(productQuantityColumnIndex), String.valueOf(readDatabaseCursor.getInt(productQuantityColumnIndex)));
            Log.i(String.valueOf(supplierNameColumnIndex), readDatabaseCursor.getString(supplierNameColumnIndex));
            Log.i(String.valueOf(supplierPhoneColumnIndex), readDatabaseCursor.getString(supplierPhoneColumnIndex));
        }
        readDatabaseCursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
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
