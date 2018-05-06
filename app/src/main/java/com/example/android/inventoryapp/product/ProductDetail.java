package com.example.android.inventoryapp.product;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
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

public class ProductDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mCurrentUri;
    private int mProductId;

    private EditText mProductName;
    private EditText mProductPrice;
    private EditText mProductQuantity;
    private EditText mSupplierName;
    private EditText mSupplierPhone;

    private Button mPlusQuantity;
    private Button mMinusQuantity;
    private Button mDelete;
    private Button mSave;
    private Button mOrder;

    private boolean mIsDeleting = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.product_detail_layout);
        mCurrentUri = getIntent().getData();

        mProductName = findViewById(R.id.product_name_update);
        mProductPrice = findViewById(R.id.product_price_update);
        mProductQuantity = findViewById(R.id.product_quantity_update);
        mSupplierName = findViewById(R.id.supplier_name_update);
        mSupplierPhone = findViewById(R.id.supplier_phone_update);

        mPlusQuantity = findViewById(R.id.increase_quantity_button);
        mMinusQuantity = findViewById(R.id.decrease_quantity_button);
        mDelete = findViewById(R.id.delete_button_update);
        mSave = findViewById(R.id.save_button_update);
        mOrder = findViewById(R.id.order_button);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mPlusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityField = mProductQuantity.getText().toString();
                Integer quantity = 0;

                if (!quantityField.matches(""))
                    quantity = Integer.valueOf(quantityField);

                if (quantity < Integer.MAX_VALUE)
                    quantity++;
                mProductQuantity.setText(String.valueOf(quantity));
            }
        });
        mMinusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityField = mProductQuantity.getText().toString();
                Integer quantity = 0;

                if (!quantityField.matches(""))
                    quantity = Integer.valueOf(quantityField);

                if (quantity > 0)
                    quantity--;
                mProductQuantity.setText(String.valueOf(quantity));
            }
        });
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsDeleting) {
                    getContentResolver().delete(mCurrentUri, null, null);
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    mIsDeleting = false;
                } else {
                    Toast.makeText(getBaseContext(),
                            "Press again to delete this entry.",
                            Toast.LENGTH_SHORT).show();
                    mIsDeleting = true;
                }
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editTextValue;
                ContentValues values = new ContentValues();

                editTextValue = mProductName.getText().toString();
                if (verifyEntry(editTextValue)) {
                    values.put(ProductContract.Product.COLUMN_PRODUCT_NAME, editTextValue);

                    editTextValue = mProductPrice.getText().toString();
                    if (verifyEntry(editTextValue)) {
                        values.put(ProductContract.Product.COLUMN_PRICE, editTextValue);

                        editTextValue = mProductQuantity.getText().toString();
                        if (verifyEntry(editTextValue)) {
                            values.put(ProductContract.Product.COLUMN_QUANTITY, editTextValue);

                            editTextValue = mSupplierName.getText().toString();
                            if (verifyEntry(editTextValue)) {
                                values.put(ProductContract.Product.COLUMN_SUPPLIER_NAME, editTextValue);

                                editTextValue = mSupplierPhone.getText().toString();
                                if (verifyEntry(editTextValue))
                                    values.put(ProductContract.Product.COLUMN_SUPPLIER_PHONE_NUMBER, editTextValue);
                                else
                                    Toast.makeText(getBaseContext(), "Please insert suppliers phone number", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getBaseContext(), "Please insert suppliers name", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getBaseContext(), "Please insert product quantity", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getBaseContext(), "Please insert product price", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getBaseContext(), "Please insert product name", Toast.LENGTH_SHORT).show();

                int result = 0;
                if (values.size() == 5) {
                    result = getContentResolver().update(mCurrentUri, values, null, null);
                }
                if (result != 0) {
                    Toast.makeText(getBaseContext(), "Product updated.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                } else
                    Toast.makeText(getBaseContext(), "Error updating product", Toast.LENGTH_SHORT).show();
            }
        });
        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "tel:" + mSupplierPhone.getText().toString();

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(phoneNumber));

                startActivity(intent);
            }
        });
    }

    private boolean verifyEntry(String value) {
        return !value.isEmpty();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductContract.Product.COLUMN_ID,
                ProductContract.Product.COLUMN_PRODUCT_NAME,
                ProductContract.Product.COLUMN_PRICE,
                ProductContract.Product.COLUMN_QUANTITY,
                ProductContract.Product.COLUMN_SUPPLIER_NAME,
                ProductContract.Product.COLUMN_SUPPLIER_PHONE_NUMBER
        };
        return new CursorLoader(this,
                mCurrentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            String productName = data.getString(data.getColumnIndex(ProductContract.Product.COLUMN_PRODUCT_NAME));
            float productPrice = data.getFloat(data.getColumnIndex(ProductContract.Product.COLUMN_PRICE));
            int productQuantity = data.getInt(data.getColumnIndex(ProductContract.Product.COLUMN_QUANTITY));
            String supplierName = data.getString(data.getColumnIndex(ProductContract.Product.COLUMN_SUPPLIER_NAME));
            String supplierPhone = data.getString(data.getColumnIndex(ProductContract.Product.COLUMN_SUPPLIER_PHONE_NUMBER));

            mProductName.setText(productName);
            mProductPrice.setText(String.valueOf(productPrice));
            mProductQuantity.setText(String.valueOf(productQuantity));
            mSupplierName.setText(supplierName);
            mSupplierPhone.setText(supplierPhone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductName.setText("");
        mProductPrice.setText("");
        mProductQuantity.setText("");
        mSupplierName.setText("");
        mSupplierPhone.setText("");
    }
}
