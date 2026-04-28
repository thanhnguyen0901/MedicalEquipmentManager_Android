package com.example.k23dtcn436_nguyenvietthanh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.List;

/**
 * Activity to display and manage the list of Categories.
 */
public class CategoryListActivity extends AppCompatActivity {

    private ListView lvCategories;
    private ExtendedFloatingActionButton fabAdd;
    private ImageButton btnBack;
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
        btnBack = findViewById(R.id.btnBack);
        categoryDAO = new CategoryDAO(this);
    }

    private void setupEvents() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Open dialog to add new category
        fabAdd.setOnClickListener(v -> {
            showAddCategoryDialog();
        });

        // Click to manage item
        lvCategories.setOnItemClickListener((parent, view, position, id) -> {
            showEditDialog(categoryList.get(position));
        });
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.btn_add_category);

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_category_edit, null);
        EditText input = view.findViewById(R.id.etEditCategoryName);
        input.setHint(R.string.label_category_name);
        builder.setView(view);

        builder.setPositiveButton(R.string.btn_add_category, (dialog, which) -> {
            String name = input.getText().toString().trim();
            if (!name.isEmpty()) {
                categoryDAO.open();
                if (categoryDAO.addCategory(new Category(name)) != -1) {
                    Toast.makeText(this, R.string.msg_add_cat_success, Toast.LENGTH_SHORT).show();
                    loadData();
                } else {
                    Toast.makeText(this, R.string.err_add_cat_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.show();
    }

    private void showEditDialog(Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.btn_edit_cat);

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_category_edit, null);
        EditText input = view.findViewById(R.id.etEditCategoryName);
        input.setText(category.getCategoryName());
        builder.setView(view);

        builder.setPositiveButton(R.string.btn_update, (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty()) {
                category.setCategoryName(newName);
                categoryDAO.open();
                if (categoryDAO.updateCategory(category)) {
                    Toast.makeText(this, R.string.msg_update_success, Toast.LENGTH_SHORT).show();
                    loadData();
                }
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.show();
    }

    private void loadData() {
        categoryDAO.open();
        List<Category> list = categoryDAO.getAllCategories();
        categoryList = new ArrayList<>(list);
        adapter = new CategoryAdapter(this, categoryList, new CategoryAdapter.OnCategoryActionListener() {
            @Override
            public void onDelete(Category category) {
                showDeleteConfirmDialog(category);
            }

            @Override
            public void onEdit(Category category) {
                showEditDialog(category);
            }
        });
        lvCategories.setAdapter(adapter);
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
