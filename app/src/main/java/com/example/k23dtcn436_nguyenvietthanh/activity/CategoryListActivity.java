package com.example.k23dtcn436_nguyenvietthanh.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.k23dtcn436_nguyenvietthanh.R;
import com.example.k23dtcn436_nguyenvietthanh.adapter.CategoryAdapter;
import com.example.k23dtcn436_nguyenvietthanh.dao.CategoryDAO;
import com.example.k23dtcn436_nguyenvietthanh.model.Category;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

/**
 * Activity to display and manage the list of Categories.
 */
public class CategoryListActivity extends AppCompatActivity {

    private ListView lvCategories;
    private ExtendedFloatingActionButton fabAdd;
    private CategoryDAO categoryDAO;
    private ArrayList<Category> categoryList;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        initViews();
        setupEvents();
    }

    private void initViews() {
        lvCategories = findViewById(R.id.lvCategories);
        fabAdd = findViewById(R.id.fabAddCategory);
        categoryDAO = new CategoryDAO(this);
    }

    private void setupEvents() {
        // Open form to add new category
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryListActivity.this, CategoryActivity.class);
            startActivity(intent);
        });

        // Long click or Click to manage item
        lvCategories.setOnItemClickListener((parent, view, position, id) -> {
            showActionDialog(categoryList.get(position));
        });
    }

    private void loadData() {
        categoryDAO.open();
        categoryList = categoryDAO.getAllCategories();
        adapter = new CategoryAdapter(this, categoryList, new CategoryAdapter.OnCategoryActionListener() {
            @Override
            public void onDelete(Category category) {
                showDeleteConfirmDialog(category);
            }

            @Override
            public void onEdit(Category category) {
                Intent intent = new Intent(CategoryListActivity.this, CategoryActivity.class);
                intent.putExtra("CATEGORY_ID", category.getCategoryId());
                startActivity(intent);
            }
        });
        lvCategories.setAdapter(adapter);
    }

    private void showActionDialog(Category category) {
        String[] options = {getString(R.string.btn_edit_cat), getString(R.string.dialog_delete)};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(String.format(getString(R.string.dialog_manage_format), category.getCategoryName()));
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Edit
                Intent intent = new Intent(CategoryListActivity.this, CategoryActivity.class);
                intent.putExtra("CATEGORY_ID", category.getCategoryId());
                startActivity(intent);
            } else if (which == 1) {
                // Delete
                showDeleteConfirmDialog(category);
            }
        });
        builder.show();
    }

    private void showDeleteConfirmDialog(Category category) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_cat_title)
                .setMessage(String.format(getString(R.string.msg_confirm_delete_format), category.getCategoryName()))
                .setPositiveButton(R.string.dialog_delete, (dialog, which) -> {
                    categoryDAO.open();
                    if (categoryDAO.deleteCategory(category.getCategoryId())) {
                        Toast.makeText(this, R.string.msg_delete_success, Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(this, R.string.err_db_delete, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // Refresh list whenever returning to this screen
    }
}
