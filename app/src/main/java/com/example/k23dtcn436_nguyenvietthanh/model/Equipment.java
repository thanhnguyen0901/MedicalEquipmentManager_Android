package com.example.k23dtcn436_nguyenvietthanh.model;

/**
 * Model class representing Medical Equipment.
 */
public class Equipment {
    private String equipmentId;
    private String equipmentName;
    private String brand;
    private int manufactureYear;
    private String status;
    private String categoryId;

    // No-arg constructor
    public Equipment() {
    }

    // Constructor without ID
    public Equipment(String equipmentName, String brand, int manufactureYear, String status, String categoryId) {
        this.equipmentName = equipmentName;
        this.brand = brand;
        this.manufactureYear = manufactureYear;
        this.status = status;
        this.categoryId = categoryId;
    }

    // Full constructor
    public Equipment(String equipmentId, String equipmentName, String brand, int manufactureYear, String status, String categoryId) {
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.brand = brand;
        this.manufactureYear = manufactureYear;
        this.status = status;
        this.categoryId = categoryId;
    }

    // Getters and Setters
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

    /**
     * Trả về chuỗi hiển thị tiếng Việt dựa trên trạng thái nội bộ.
     */
    public String getDisplayStatus(android.content.Context context) {
        if (status == null) return "";
        
        switch (status.toLowerCase()) {
            case "active":
                return context.getString(com.example.k23dtcn436_nguyenvietthanh.R.string.status_active);
            case "broken":
                return context.getString(com.example.k23dtcn436_nguyenvietthanh.R.string.status_broken);
            case "maintenance":
            case "under maintenance":
                return context.getString(com.example.k23dtcn436_nguyenvietthanh.R.string.status_maintenance);
            default:
                return status;
        }
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
