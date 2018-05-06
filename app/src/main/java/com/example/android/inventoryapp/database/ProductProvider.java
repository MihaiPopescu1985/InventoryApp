package com.example.android.inventoryapp.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ProductProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static ProductDbHelper mProdDbHelper;

    static {
        sUriMatcher.addURI("com.example.android.inventoryapp", "/products", 100);
        sUriMatcher.addURI("com.example.android.inventoryapp", "/products/#", 101);
    }

    @Override
    public boolean onCreate() {

        mProdDbHelper = new ProductDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        // Returning cursor
        Cursor cursor = null;

        // Get database in readable mode
        SQLiteDatabase database = mProdDbHelper.getReadableDatabase();

        // Get the code from received uri
        int matchUri = sUriMatcher.match(uri);

        switch (matchUri) {
            case 100:
                cursor = database.query(ProductContract.Product.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case 101:
                selection = ProductContract.Product.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(ProductContract.Product.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        String path = "content://com.example.android.inventoryapp/products";

        int matchUri = sUriMatcher.match(uri);
        switch (matchUri) {
            case 100:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + path;
            case 101:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + path;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + matchUri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase database = mProdDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        switch (match) {
            case 100:
                if (verifyInsertedValues(values)) {
                    long id = database.insert(ProductContract.Product.TABLE_NAME, null, values);
                    if (id == -1)
                        throw new IllegalArgumentException("Insertion failed, please retry");
                    else {
                        getContext().getContentResolver().notifyChange(uri, null);
                        return ContentUris.withAppendedId(uri, id);
                    }
                } else
                    throw new IllegalArgumentException("Insertion not supported for the specified values.");
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private boolean verifyInsertedValues(ContentValues values) {
        String stringVerifier = values.getAsString(ProductContract.Product.COLUMN_PRODUCT_NAME);
        if (stringVerifier == null)
            return false;
        stringVerifier = values.getAsString(ProductContract.Product.COLUMN_SUPPLIER_NAME);
        if (stringVerifier == null)
            return false;
        stringVerifier = values.getAsString(ProductContract.Product.COLUMN_SUPPLIER_PHONE_NUMBER);
        if (stringVerifier == null)
            return false;
        double intVerifier = values.getAsDouble(ProductContract.Product.COLUMN_PRICE);
        if (intVerifier <= 0)
            return false;
        intVerifier = values.getAsInteger(ProductContract.Product.COLUMN_QUANTITY);
        if (intVerifier <= 0)
            return false;

        return true;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mProdDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int matchUri = sUriMatcher.match(uri);
        switch (matchUri) {
            case 100:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(ProductContract.Product.TABLE_NAME, selection, selectionArgs);
                break;
            case 101:
                // Delete a single row given by the ID in the URI
                selection = ProductContract.Product._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ProductContract.Product.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case 100:
                return updateProducts(uri, values, selection, selectionArgs);

            case 101:
                selection = ProductContract.Product._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProducts(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateProducts(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(ProductContract.Product.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(ProductContract.Product.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product must have a name !");
            }
        }

        if (values.containsKey(ProductContract.Product.COLUMN_PRICE)) {
            Float price = values.getAsFloat(ProductContract.Product.COLUMN_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("Product must have a price !");
            }
        }

        if (values.containsKey(ProductContract.Product.COLUMN_QUANTITY)) {
            Integer quantity = values.getAsInteger(ProductContract.Product.COLUMN_QUANTITY);
            if (quantity == null) {
                throw new IllegalArgumentException("Product must have a quantity !");
            }
        }

        if (values.containsKey(ProductContract.Product.COLUMN_SUPPLIER_NAME)) {
            String supplierName = values.getAsString(ProductContract.Product.COLUMN_SUPPLIER_NAME);
            if (supplierName == null) {
                throw new IllegalArgumentException("Supplier must have a name !");
            }
        }

        if (values.containsKey(ProductContract.Product.COLUMN_SUPPLIER_PHONE_NUMBER)) {
            String supplierPhoneNumber = values.getAsString(ProductContract.Product.COLUMN_SUPPLIER_PHONE_NUMBER);
            if (supplierPhoneNumber == null) {
                throw new IllegalArgumentException("Supplier must have a phone number !");
            }
        }
        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mProdDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(ProductContract.Product.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }
}
