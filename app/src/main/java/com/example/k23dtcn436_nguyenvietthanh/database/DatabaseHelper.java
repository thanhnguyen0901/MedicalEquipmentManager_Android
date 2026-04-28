package com.example.k23dtcn436_nguyenvietthanh.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.k23dtcn436_nguyenvietthanh.model.Equipment;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "medical_equipment.db";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    public static final String TABLE_CATEGORY = "Category";
    public static final String TABLE_EQUIPMENT = "Equipment";

    // Category Table Columns
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_CATEGORY_NAME = "category_name";

    // Equipment Table Columns
    public static final String COLUMN_EQUIPMENT_ID = "equipment_id";
    public static final String COLUMN_EQUIPMENT_NAME = "equipment_name";
    public static final String COLUMN_BRAND = "brand";
    public static final String COLUMN_MANUFACTURE_YEAR = "manufacture_year";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_EQUIPMENT_CATEGORY_ID = "category_id";

    // Create Category Table Query
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + " ("
            + COLUMN_CATEGORY_ID + " TEXT PRIMARY KEY, "
            + COLUMN_CATEGORY_NAME + " TEXT NOT NULL"
            + ");";

    // Create Equipment Table Query
    private static final String CREATE_TABLE_EQUIPMENT = "CREATE TABLE " + TABLE_EQUIPMENT + " ("
            + COLUMN_EQUIPMENT_ID + " TEXT PRIMARY KEY, "
            + COLUMN_EQUIPMENT_NAME + " TEXT NOT NULL, "
            + COLUMN_BRAND + " TEXT NOT NULL, "
            + COLUMN_MANUFACTURE_YEAR + " INTEGER NOT NULL, "
            + COLUMN_STATUS + " TEXT NOT NULL, "
            + COLUMN_EQUIPMENT_CATEGORY_ID + " TEXT NOT NULL, "
            + "FOREIGN KEY(" + COLUMN_EQUIPMENT_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + ")"
            + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_EQUIPMENT);
        seedDefaultData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Demo upgrade strategy: recreate the seeded database.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQUIPMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);
    }

    // Seed data for the sample medical equipment catalog.
    private void seedDefaultData(SQLiteDatabase db) {
        insertCategory(db, "CAT001", "Máy chẩn đoán hình ảnh");
        insertCategory(db, "CAT002", "Thiết bị xét nghiệm");
        insertCategory(db, "CAT003", "Thiết bị theo dõi bệnh nhân");
        insertCategory(db, "CAT004", "Thiết bị phẫu thuật");
        insertCategory(db, "CAT005", "Thiết bị cấp cứu");

        insertEquipment(db, "EQ001", "Máy siêu âm Doppler", "Samsung Medison", 2022, Equipment.STATUS_ACTIVE, "CAT001");
        insertEquipment(db, "EQ002", "Máy X-quang kỹ thuật số", "Siemens Healthineers", 2021, Equipment.STATUS_ACTIVE, "CAT001");
        insertEquipment(db, "EQ003", "Máy CT Scanner 64 lát cắt", "GE Healthcare", 2019, Equipment.STATUS_MAINTENANCE, "CAT001");

        insertEquipment(db, "EQ004", "Máy xét nghiệm huyết học", "Sysmex", 2023, Equipment.STATUS_ACTIVE, "CAT002");
        insertEquipment(db, "EQ005", "Máy xét nghiệm sinh hóa", "Roche", 2020, Equipment.STATUS_ACTIVE, "CAT002");

        insertEquipment(db, "EQ006", "Máy đo điện tim ECG", "Nihon Kohden", 2022, Equipment.STATUS_ACTIVE, "CAT003");
        insertEquipment(db, "EQ007", "Máy monitor theo dõi bệnh nhân", "Philips", 2018, Equipment.STATUS_BROKEN, "CAT003");

        insertEquipment(db, "EQ008", "Dao mổ điện", "Medtronic", 2021, Equipment.STATUS_ACTIVE, "CAT004");
        insertEquipment(db, "EQ009", "Đèn mổ LED treo trần", "Dräger", 2017, Equipment.STATUS_MAINTENANCE, "CAT004");

        insertEquipment(db, "EQ010", "Máy sốc tim", "Zoll", 2024, Equipment.STATUS_ACTIVE, "CAT005");
        insertEquipment(db, "EQ011", "Máy thở cấp cứu", "Hamilton Medical", 2021, Equipment.STATUS_ACTIVE, "CAT005");
        insertEquipment(db, "EQ012", "Bơm tiêm điện", "B. Braun", 2019, Equipment.STATUS_BROKEN, "CAT005");
    }

    private void insertCategory(SQLiteDatabase db, String id, String name) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_ID, id);
        values.put(COLUMN_CATEGORY_NAME, name);
        db.insertWithOnConflict(TABLE_CATEGORY, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    private void insertEquipment(SQLiteDatabase db, String id, String name, String brand, int year, String status, String catId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EQUIPMENT_ID, id);
        values.put(COLUMN_EQUIPMENT_NAME, name);
        values.put(COLUMN_BRAND, brand);
        values.put(COLUMN_MANUFACTURE_YEAR, year);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_EQUIPMENT_CATEGORY_ID, catId);
        db.insertWithOnConflict(TABLE_EQUIPMENT, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }
}
