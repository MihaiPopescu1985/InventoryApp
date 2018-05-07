package com.example.android.inventoryapp.product;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryapp.MainActivity;
import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.database.ProductContract;

public class ProductAdder extends AppCompatActivity {

    private EditText mProductName;
    private EditText mProductPrice;
    private EditText mProductQuantity;
    private EditText mSupplierName;
    private EditText mSupplierPhone;

    private Button mSaveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add a product");
        setContentView(R.layout.add_product_layout);

        mProductName = findViewById(R.id.product_name_edit);
        mProductPrice = findViewById(R.id.product_price_edit);
        mProductQuantity = findViewById(R.id.product_quantity_edit);
        mSupplierName = findViewById(R.id.supplier_name_edit);
        mSupplierPhone = findViewById(R.id.supplier_phone_edit);

        mSaveButton = findViewById(R.id.save_product_button);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editTextValue;
                ContentValues values = new ContentValues();
                Uri contentUri = Uri.parse("content://com.example.android.inventoryapp/products");

                editTextValue = mProductName.getText().toString();
                if (verifyEntry(editTextValue)) {
                    values.put(ProductContract.Product.COLUMN_PRODUCT_NAME, editTextValue);
                } else {
                    Toast.makeText(getBaseContext(), R.string.insert_product_name, Toast.LENGTH_SHORT).show();
                    return;
                }

                editTextValue = mProductPrice.getText().toString();
                if (verifyEntry(editTextValue)) {
                    values.put(ProductContract.Product.COLUMN_PRICE, editTextValue);
                } else {
                    Toast.makeText(getBaseContext(), R.string.insert_product_price, Toast.LENGTH_SHORT).show();
                    return;
                }
                editTextValue = mProductQuantity.getText().toString();
                if (verifyEntry(editTextValue)) {
                    values.put(ProductContract.Product.COLUMN_QUANTITY, editTextValue);
                } else {
                    Toast.makeText(getBaseContext(), R.string.insert_product_quantity, Toast.LENGTH_SHORT).show();
                    return;
                }

                editTextValue = mSupplierName.getText().toString();
                if (verifyEntry(editTextValue)) {
                    values.put(ProductContract.Product.COLUMN_SUPPLIER_NAME, editTextValue);
                } else {
                    Toast.makeText(getBaseContext(), R.string.insert_supplier_name, Toast.LENGTH_SHORT).show();
                    return;
                }

                editTextValue = mSupplierPhone.getText().toString();
                if (verifyEntry(editTextValue))
                    values.put(ProductContract.Product.COLUMN_SUPPLIER_PHONE_NUMBER, editTextValue);
                else {
                    Toast.makeText(getBaseContext(), R.string.insert_supplier_phone, Toast.LENGTH_SHORT).show();
                    return;
                }

                Uri mNewUri = null;
                if (values.size() == 5) {
                    mNewUri = getContentResolver().insert(contentUri, values);
                }
                if (mNewUri != null) {
                    Toast.makeText(getBaseContext(), R.string.product_added, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                } else
                    Toast.makeText(getBaseContext(), R.string.error_inserting_product, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean verifyEntry(String value) {
        return !value.isEmpty();
    }
}

