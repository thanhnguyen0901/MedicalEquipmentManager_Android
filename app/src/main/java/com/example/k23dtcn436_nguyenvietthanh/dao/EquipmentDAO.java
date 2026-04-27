package com.example.k23dtcn436_nguyenvietthanh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.k23dtcn436_nguyenvietthanh.database.DatabaseHelper;
import com.example.k23dtcn436_nguyenvietthanh.model.Equipment;
import java.util.ArrayList;

/**
 * Data Access Object (DAO) for Equipment table.
 * Handles all database operations related to medical equipment.
 */
public class EquipmentDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public EquipmentDAO(Context context) {
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
     * Insert a new Equipment record.
     */
    public long addEquipment(Equipment equipment) {
        ContentValues values = new ContentValues();
        if (equipment.getEquipmentId() == null) {
            equipment.setEquipmentId(java.util.UUID.randomUUID().toString());
        }
        values.put(DatabaseHelper.COLUMN_EQUIPMENT_ID, equipment.getEquipmentId());
        values.put(DatabaseHelper.COLUMN_EQUIPMENT_NAME, equipment.getEquipmentName());
        values.put(DatabaseHelper.COLUMN_BRAND, equipment.getBrand());
        values.put(DatabaseHelper.COLUMN_MANUFACTURE_YEAR, equipment.getManufactureYear());
        values.put(DatabaseHelper.COLUMN_STATUS, equipment.getStatus());
        values.put(DatabaseHelper.COLUMN_EQUIPMENT_CATEGORY_ID, equipment.getCategoryId());

        return db.insert(DatabaseHelper.TABLE_EQUIPMENT, null, values);
    }

    public boolean insertEquipment(Equipment equipment) {
        return addEquipment(equipment) != -1;
    }

    /**
     * Update an existing Equipment record.
     */
    public boolean updateEquipment(Equipment equipment) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_EQUIPMENT_NAME, equipment.getEquipmentName());
        values.put(DatabaseHelper.COLUMN_BRAND, equipment.getBrand());
        values.put(DatabaseHelper.COLUMN_MANUFACTURE_YEAR, equipment.getManufactureYear());
        values.put(DatabaseHelper.COLUMN_STATUS, equipment.getStatus());
        values.put(DatabaseHelper.COLUMN_EQUIPMENT_CATEGORY_ID, equipment.getCategoryId());

        int result = db.update(DatabaseHelper.TABLE_EQUIPMENT, values,
                DatabaseHelper.COLUMN_EQUIPMENT_ID + " = ?",
                new String[]{equipment.getEquipmentId()});
        return result > 0;
    }

    /**
     * Delete an Equipment record by ID.
     */
    public boolean deleteEquipment(String equipmentId) {
        int result = db.delete(DatabaseHelper.TABLE_EQUIPMENT,
                DatabaseHelper.COLUMN_EQUIPMENT_ID + " = ?",
                new String[]{equipmentId});
        return result > 0;
    }

    /**
     * Retrieve all equipment.
     */
    public ArrayList<Equipment> getAllEquipment() {
        return getEquipmentFromCursor(db.query(DatabaseHelper.TABLE_EQUIPMENT, null, null, null, null, null, null));
    }

    /**
     * Retrieve a single Equipment record by ID.
     */
    public Equipment getEquipmentById(String equipmentId) {
        ArrayList<Equipment> list = getEquipmentFromCursor(db.query(DatabaseHelper.TABLE_EQUIPMENT, null,
                DatabaseHelper.COLUMN_EQUIPMENT_ID + " = ?",
                new String[]{equipmentId}, null, null, null));
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Retrieve equipment by Category ID.
     */
    public ArrayList<Equipment> getEquipmentByCategory(String categoryId) {
        return getEquipmentFromCursor(db.query(DatabaseHelper.TABLE_EQUIPMENT, null,
                DatabaseHelper.COLUMN_EQUIPMENT_CATEGORY_ID + " = ?",
                new String[]{categoryId}, null, null, null));
    }

    /**
     * Find equipment with manufacture_year > 2020 and status = 'Active'.
     */
    public ArrayList<Equipment> getActiveEquipmentAfter2020() {
        String selection = DatabaseHelper.COLUMN_MANUFACTURE_YEAR + " > ? AND " + DatabaseHelper.COLUMN_STATUS + " = ?";
        String[] selectionArgs = {"2020", "Active"};
        return getEquipmentFromCursor(db.query(DatabaseHelper.TABLE_EQUIPMENT, null, selection, selectionArgs, null, null, null));
    }

    /**
     * Helper method to parse Cursor into ArrayList.
     */
    private ArrayList<Equipment> getEquipmentFromCursor(Cursor cursor) {
        ArrayList<Equipment> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Equipment equipment = new Equipment();
                equipment.setEquipmentId(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EQUIPMENT_ID)));
                equipment.setEquipmentName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EQUIPMENT_NAME)));
                equipment.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BRAND)));
                equipment.setManufactureYear(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MANUFACTURE_YEAR)));
                equipment.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STATUS)));
                equipment.setCategoryId(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EQUIPMENT_CATEGORY_ID)));
                list.add(equipment);
            }
            cursor.close();
        }
        return list;
    }
}
