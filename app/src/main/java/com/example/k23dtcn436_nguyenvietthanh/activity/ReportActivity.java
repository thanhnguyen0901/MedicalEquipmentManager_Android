package com.example.k23dtcn436_nguyenvietthanh.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.k23dtcn436_nguyenvietthanh.R;
import com.example.k23dtcn436_nguyenvietthanh.adapter.EquipmentAdapter;
import com.example.k23dtcn436_nguyenvietthanh.dao.CategoryDAO;
import com.example.k23dtcn436_nguyenvietthanh.dao.EquipmentDAO;
import com.example.k23dtcn436_nguyenvietthanh.model.Category;
import com.example.k23dtcn436_nguyenvietthanh.model.Equipment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

/**
 * Activity to generate reports and filter equipment.
 */
public class ReportActivity extends AppCompatActivity {

    private Spinner spnCategory;
    private MaterialButton btnFilter, btnSpecial;
    private ImageButton btnBack;
    private ListView lvResults;
    private TextView tvCount;

    private EquipmentDAO equipmentDAO;
    private CategoryDAO categoryDAO;
    private ArrayList<Category> categoryList;
    private EquipmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        initViews();
        loadCategories();
        setupEvents();
    }

    private void initViews() {
        spnCategory = findViewById(R.id.spnReportCategory);
        btnFilter = findViewById(R.id.btnFilterByCategory);
        btnSpecial = findViewById(R.id.btnSpecialReport);
        btnBack = findViewById(R.id.btnBack);
        lvResults = findViewById(R.id.lvReportResults);
        tvCount = findViewById(R.id.tvReportCount);

        equipmentDAO = new EquipmentDAO(this);
        categoryDAO = new CategoryDAO(this);
    }

    private void loadCategories() {
        categoryDAO.open();
        categoryList = categoryDAO.getAllCategories();
        
        ArrayList<String> names = new ArrayList<>();
        for (Category c : categoryList) {
            names.add(c.getCategoryName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        // Query 1: Filter by Category
        btnFilter.setOnClickListener(v -> {
            if (categoryList.isEmpty()) {
                Toast.makeText(this, R.string.err_no_category_available, Toast.LENGTH_SHORT).show();
                return;
            }
            
            int pos = spnCategory.getSelectedItemPosition();
            String catId = categoryList.get(pos).getCategoryId();
            
            equipmentDAO.open();
            ArrayList<Equipment> results = equipmentDAO.getEquipmentByCategory(catId);
            updateList(results);
        });

        // Query 2: Active & After 2020
        btnSpecial.setOnClickListener(v -> {
            equipmentDAO.open();
            ArrayList<Equipment> results = equipmentDAO.getActiveEquipmentAfter2020();
            updateList(results);
        });
    }

    private void updateList(ArrayList<Equipment> list) {
        // Pass null for the listener to make the adapter Read-Only in Report screen
        adapter = new EquipmentAdapter(this, list, null);
        lvResults.setAdapter(adapter);
        tvCount.setText(String.format(getString(R.string.results_count), list.size()));
        
        if (list.isEmpty()) {
            Toast.makeText(this, R.string.err_no_items_found, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (equipmentDAO != null) equipmentDAO.close();
        if (categoryDAO != null) categoryDAO.close();
    }
}
