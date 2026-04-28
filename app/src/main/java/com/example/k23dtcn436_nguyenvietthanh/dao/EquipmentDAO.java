package com.example.k23dtcn436_nguyenvietthanh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.k23dtcn436_nguyenvietthanh.database.DatabaseHelper;
import com.example.k23dtcn436_nguyenvietthanh.model.Equipment;
import java.util.ArrayList;

public class EquipmentDAO {
    private static final int REPORT_YEAR_THRESHOLD = 2020;

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public EquipmentDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

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

    public boolean deleteEquipment(String equipmentId) {
        int result = db.delete(DatabaseHelper.TABLE_EQUIPMENT,
                DatabaseHelper.COLUMN_EQUIPMENT_ID + " = ?",
                new String[]{equipmentId});
        return result > 0;
    }

    public ArrayList<Equipment> getAllEquipment() {
        return getEquipmentFromCursor(db.query(DatabaseHelper.TABLE_EQUIPMENT, null, null, null, null, null, null));
    }

    public Equipment getEquipmentById(String equipmentId) {
        ArrayList<Equipment> list = getEquipmentFromCursor(db.query(DatabaseHelper.TABLE_EQUIPMENT, null,
                DatabaseHelper.COLUMN_EQUIPMENT_ID + " = ?",
                new String[]{equipmentId}, null, null, null));
        return list.isEmpty() ? null : list.get(0);
    }

    public ArrayList<Equipment> getEquipmentByCategory(String categoryId) {
        return getEquipmentFromCursor(db.query(DatabaseHelper.TABLE_EQUIPMENT, null,
                DatabaseHelper.COLUMN_EQUIPMENT_CATEGORY_ID + " = ?",
                new String[]{categoryId}, null, null, null));
    }

    // Report requirement: equipment made after 2020 and currently active.
    public ArrayList<Equipment> getActiveEquipmentAfter2020() {
        String selection = DatabaseHelper.COLUMN_MANUFACTURE_YEAR + " > ? AND " + DatabaseHelper.COLUMN_STATUS + " = ?";
        String[] selectionArgs = {String.valueOf(REPORT_YEAR_THRESHOLD), Equipment.STATUS_ACTIVE};
        return getEquipmentFromCursor(db.query(DatabaseHelper.TABLE_EQUIPMENT, null, selection, selectionArgs, null, null, null));
    }

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
