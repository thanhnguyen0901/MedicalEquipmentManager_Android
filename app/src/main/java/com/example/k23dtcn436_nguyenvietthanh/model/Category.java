package com.example.k23dtcn436_nguyenvietthanh.model;

/**
 * Model class representing a Category of Medical Equipment.
 */
public class Category {
    private String categoryId;
    private String categoryName;

    // No-arg constructor
    public Category() {
    }

    // Constructor with name only
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    // Full constructor
    public Category(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // Alias methods for compatibility
    public String getId() {
        return categoryId;
    }

    public void setId(String id) {
        this.categoryId = id;
    }

    public String getName() {
        return categoryName;
    }

    public void setName(String name) {
        this.categoryName = name;
    }

    // Getters and Setters
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return categoryName; // Useful for Spinner display
    }
}
