package com.example.k23dtcn436_nguyenvietthanh.model;

import android.content.Context;

import com.example.k23dtcn436_nguyenvietthanh.R;

public class Equipment {
    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_BROKEN = "Broken";
    public static final String STATUS_MAINTENANCE = "Maintenance";

    private String equipmentId;
    private String equipmentName;
    private String brand;
    private int manufactureYear;
    private String status;
    private String categoryId;

    public Equipment() {
    }

    public Equipment(String equipmentName, String brand, int manufactureYear, String status, String categoryId) {
        this.equipmentName = equipmentName;
        this.brand = brand;
        this.manufactureYear = manufactureYear;
        this.status = status;
        this.categoryId = categoryId;
    }

    public Equipment(String equipmentId, String equipmentName, String brand, int manufactureYear, String status, String categoryId) {
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.brand = brand;
        this.manufactureYear = manufactureYear;
        this.status = status;
        this.categoryId = categoryId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(int manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public String getStatus() {
        return status;
    }

    public static String getDisplayStatus(Context context, String dbStatus) {
        if (dbStatus == null) return "";

        if (STATUS_ACTIVE.equalsIgnoreCase(dbStatus)) {
            return context.getString(R.string.status_active);
        } else if (STATUS_BROKEN.equalsIgnoreCase(dbStatus)) {
            return context.getString(R.string.status_broken);
        } else if (STATUS_MAINTENANCE.equalsIgnoreCase(dbStatus)) {
            return context.getString(R.string.status_maintenance);
        }
        return dbStatus;
    }

    public static int getStatusSelectionIndex(Context context, String dbStatus) {
        if (dbStatus == null) return -1;

        String[] dbStatuses = context.getResources().getStringArray(R.array.status_array_db);
        for (int i = 0; i < dbStatuses.length; i++) {
            if (dbStatuses[i].equalsIgnoreCase(dbStatus)) {
                return i;
            }
        }
        return -1;
    }

    public String getDisplayStatus(Context context) {
        return getDisplayStatus(context, status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
