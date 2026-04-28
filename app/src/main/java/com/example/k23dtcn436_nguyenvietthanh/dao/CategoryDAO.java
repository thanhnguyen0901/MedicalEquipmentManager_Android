package com.example.k23dtcn436_nguyenvietthanh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.k23dtcn436_nguyenvietthanh.database.DatabaseHelper;
import com.example.k23dtcn436_nguyenvietthanh.model.Category;
import java.util.ArrayList;

public class CategoryDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public CategoryDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addCategory(Category category) {
        ContentValues values = new ContentValues();
        // Generate an ID when the caller creates a category by name only.
        if (category.getCategoryId() == null) {
            category.setCategoryId(java.util.UUID.randomUUID().toString());
        }
        values.put(DatabaseHelper.COLUMN_CATEGORY_ID, category.getCategoryId());
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, category.getCategoryName());

        return db.insert(DatabaseHelper.TABLE_CATEGORY, null, values);
    }

    public boolean updateCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, category.getCategoryName());

        int result = db.update(DatabaseHelper.TABLE_CATEGORY, values,
                DatabaseHelper.COLUMN_CATEGORY_ID + " = ?",
                new String[]{category.getCategoryId()});
        return result > 0;
    }

    public boolean deleteCategory(String categoryId) {
        int result = db.delete(DatabaseHelper.TABLE_CATEGORY,
                DatabaseHelper.COLUMN_CATEGORY_ID + " = ?",
                new String[]{categoryId});
        return result > 0;
    }

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
