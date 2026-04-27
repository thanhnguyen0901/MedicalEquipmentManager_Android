package com.example.k23dtcn436_nguyenvietthanh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.k23dtcn436_nguyenvietthanh.database.DatabaseHelper;
import com.example.k23dtcn436_nguyenvietthanh.model.Category;
import java.util.ArrayList;

/**
 * Data Access Object (DAO) for Category table.
 * Handles all database operations related to categories.
 */
public class CategoryDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public CategoryDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Open the database for writing
    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    // Close the database
    public void close() {
        dbHelper.close();
    }

    /**
     * Insert a new Category into the database.
     */
    public long addCategory(Category category) {
        ContentValues values = new ContentValues();
        // If ID is null, we might want to generate one or let it be null if DB allows it.
        // But it's TEXT PRIMARY KEY, so it should probably be unique.
        if (category.getCategoryId() == null) {
            category.setCategoryId(java.util.UUID.randomUUID().toString());
        }
        values.put(DatabaseHelper.COLUMN_CATEGORY_ID, category.getCategoryId());
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, category.getCategoryName());

        return db.insert(DatabaseHelper.TABLE_CATEGORY, null, values);
    }

    /**
     * Update an existing Category in the database.
     */
    public boolean updateCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, category.getCategoryName());

        int result = db.update(DatabaseHelper.TABLE_CATEGORY, values,
                DatabaseHelper.COLUMN_CATEGORY_ID + " = ?",
                new String[]{category.getCategoryId()});
        return result > 0;
    }

    /**
     * Delete a Category by its ID.
     */
    public boolean deleteCategory(String categoryId) {
        int result = db.delete(DatabaseHelper.TABLE_CATEGORY,
                DatabaseHelper.COLUMN_CATEGORY_ID + " = ?",
                new String[]{categoryId});
        return result > 0;
    }

    /**
     * Retrieve all categories from the database.
     */
    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> list = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORY, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Category category = new Category();
                category.setCategoryId(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ID)));
                category.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NAME)));
                list.add(category);
            }
            cursor.close();
        }
        return list;
    }

    /**
     * Retrieve a single Category by its ID.
     */
    public Category getCategoryById(String categoryId) {
        Category category = null;
        Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORY, null,
                DatabaseHelper.COLUMN_CATEGORY_ID + " = ?",
                new String[]{categoryId}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                category = new Category();
                category.setCategoryId(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ID)));
                category.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NAME)));
            }
            cursor.close();
        }
        return category;
    }
}
