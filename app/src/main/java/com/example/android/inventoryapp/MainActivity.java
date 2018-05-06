package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.inventoryapp.database.ProductContract;
import com.example.android.inventoryapp.product.ProductAdder;
import com.example.android.inventoryapp.product.ProductCursorAdapter;
import com.example.android.inventoryapp.product.ProductDetail;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private ProductCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.list_of_products);
        View emptyView = findViewById(R.id.empty_list_text_view);

        listView.setEmptyView(emptyView);
        mCursorAdapter = new ProductCursorAdapter(this, null);
        listView.setAdapter(mCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ProductDetail.class);

                Uri currentItem = ContentUris.withAppendedId(
                        Uri.parse("content://com.example.android.inventoryapp/products"),
                        id);
                intent.setData(currentItem);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_a_product_menu:
                startActivity(new Intent(this, ProductAdder.class));
                break;
            case R.id.about_menu:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                ProductContract.Product.COLUMN_ID,
                ProductContract.Product.COLUMN_PRODUCT_NAME,
                ProductContract.Product.COLUMN_QUANTITY,
                ProductContract.Product.COLUMN_PRICE,
                ProductContract.Product.COLUMN_SUPPLIER_NAME,
                ProductContract.Product.COLUMN_SUPPLIER_PHONE_NUMBER};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,
                Uri.parse("content://com.example.android.inventoryapp/products"),
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
