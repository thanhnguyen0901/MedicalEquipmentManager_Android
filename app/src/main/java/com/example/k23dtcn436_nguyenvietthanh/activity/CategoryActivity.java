package com.example.k23dtcn436_nguyenvietthanh.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.k23dtcn436_nguyenvietthanh.R;
import com.example.k23dtcn436_nguyenvietthanh.adapter.CategoryAdapter;
import com.example.k23dtcn436_nguyenvietthanh.dao.CategoryDAO;
import com.example.k23dtcn436_nguyenvietthanh.model.Category;
import java.util.List;

/**
 * Consolidated Activity for Managing Categories (List + Add/Edit).
 * Replaced CategoryListActivity to simplify flow and avoid crashes.
 */
public class CategoryActivity extends AppCompatActivity {

    private EditText etCategoryName;
    private Button btnAddCategory;
    private ImageButton btnBack;
    private ListView lvCategories;
    private CategoryDAO categoryDAO;
    private List<Category> categoryList;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initViews();
        initData();
        setupEvents();
    }

    private void initViews() {
        etCategoryName = findViewById(R.id.etCategoryName);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        btnBack = findViewById(R.id.btnBack);
        lvCategories = findViewById(R.id.lvCategories);
    }

    private void initData() {
        categoryDAO = new CategoryDAO(this);
        categoryDAO.open();
        loadCategories();
    }

    private void setupEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etCategoryName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    Category category = new Category(name);
                    categoryDAO.open(); // Ensure DB is open
                    if (categoryDAO.addCategory(category) != -1) {
                        Toast.makeText(CategoryActivity.this, R.string.msg_add_cat_success, Toast.LENGTH_SHORT).show();
                        etCategoryName.setText("");
                        loadCategories();
                    } else {
                        Toast.makeText(CategoryActivity.this, R.string.err_add_cat_fail, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CategoryActivity.this, R.string.err_empty_cat_name, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadCategories() {
        categoryDAO.open();
        categoryList = categoryDAO.getAllCategories();
        adapter = new CategoryAdapter(this, categoryList, new CategoryAdapter.OnCategoryActionListener() {
            @Override
            public void onDelete(Category category) {
                new AlertDialog.Builder(CategoryActivity.this)
                        .setTitle(R.string.dialog_confirm_delete)
                        .setMessage(String.format(getString(R.string.msg_confirm_delete_format), category.getCategoryName()))
                        .setPositiveButton(R.string.dialog_delete, (dialog, which) -> {
                            categoryDAO.open();
                            categoryDAO.deleteCategory(category.getCategoryId());
                            loadCategories();
                        })
                        .setNegativeButton(R.string.dialog_cancel, null)
                        .show();
            }

            @Override
            public void onEdit(Category category) {
                showEditDialog(category);
            }
        });
        lvCategories.setAdapter(adapter);
    }

    private void showEditDialog(Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.btn_edit_cat);
        
        View view = getLayoutInflater().inflate(R.layout.dialog_category_edit, null);
        EditText input = view.findViewById(R.id.etEditCategoryName);
        input.setText(category.getCategoryName());
        builder.setView(view);

        builder.setPositiveButton(R.string.btn_update, (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!TextUtils.isEmpty(newName)) {
                category.setCategoryName(newName);
                categoryDAO.open();
                categoryDAO.updateCategory(category);
                loadCategories();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (categoryDAO != null) {
            categoryDAO.open();
            loadCategories();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (categoryDAO != null) {
            categoryDAO.close();
        }
    }
}
