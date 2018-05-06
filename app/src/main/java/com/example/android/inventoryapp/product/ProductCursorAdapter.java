package com.example.android.inventoryapp.product;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.database.ProductContract;

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_products, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {

        TextView quantityView;
        TextView nameView;
        TextView priceView;
        Button sellButton;
        final Uri uri;

        quantityView = view.findViewById(R.id.quantity_product_text_view);
        nameView = view.findViewById(R.id.name_product_text_view);
        priceView = view.findViewById(R.id.price_product_text_view);
        sellButton = view.findViewById(R.id.sell_button);

        final Integer quantity = cursor.getInt(cursor.getColumnIndex(ProductContract.Product.COLUMN_QUANTITY));
        final String productName = cursor.getString(cursor.getColumnIndex(ProductContract.Product.COLUMN_PRODUCT_NAME));
        final double productPrice = cursor.getDouble(cursor.getColumnIndex(ProductContract.Product.COLUMN_PRICE));
        final String supplierName = cursor.getString(cursor.getColumnIndex(ProductContract.Product.COLUMN_SUPPLIER_NAME));
        final String supplierPhone = cursor.getString(cursor.getColumnIndex(ProductContract.Product.COLUMN_SUPPLIER_PHONE_NUMBER));
        final long currentId = cursor.getInt(cursor.getColumnIndex(ProductContract.Product.COLUMN_ID));

        uri = ContentUris.withAppendedId(Uri.parse("content://com.example.android.inventoryapp/products"), currentId);

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                int newQuantity = quantity;

                if (newQuantity > 0)
                    newQuantity--;
                values.put(ProductContract.Product.COLUMN_QUANTITY, newQuantity);
                values.put(ProductContract.Product.COLUMN_PRODUCT_NAME, productName);
                values.put(ProductContract.Product.COLUMN_PRICE, productPrice);
                values.put(ProductContract.Product.COLUMN_SUPPLIER_NAME, supplierName);
                values.put(ProductContract.Product.COLUMN_SUPPLIER_PHONE_NUMBER, supplierPhone);

                context.getContentResolver().update(uri, values, null, null);
            }
        });

        quantityView.setText(String.valueOf(quantity));
        nameView.setText(productName);
        priceView.setText(String.valueOf(productPrice));

        if (cursor.getPosition() % 2 != 0)
            view.setBackgroundResource(R.color.backgoundListItem);
    }
}
