package com.example.k23dtcn436_nguyenvietthanh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.k23dtcn436_nguyenvietthanh.R;
import com.example.k23dtcn436_nguyenvietthanh.dao.CategoryDAO;
import com.example.k23dtcn436_nguyenvietthanh.dao.EquipmentDAO;
import com.example.k23dtcn436_nguyenvietthanh.model.Category;
import com.example.k23dtcn436_nguyenvietthanh.model.Equipment;
import com.google.android.material.button.MaterialButton;

public class EquipmentDetailActivity extends AppCompatActivity {

    private TextView tvId, tvName, tvBrand, tvYear, tvStatus, tvCategory;
    private MaterialButton btnEdit;
    private ImageButton btnBack;
    private String equipmentId;
    private EquipmentDAO equipmentDAO;
    private CategoryDAO categoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_detail);

        initViews();
        equipmentId = getIntent().getStringExtra(EquipmentFormActivity.EXTRA_EQUIPMENT_ID);
        
        if (equipmentId == null) {
            Toast.makeText(this, R.string.err_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupEvents();
    }

    private void initViews() {
        tvId = findViewById(R.id.tvDetailId);
        tvName = findViewById(R.id.tvDetailName);
        tvBrand = findViewById(R.id.tvDetailBrand);
        tvYear = findViewById(R.id.tvDetailYear);
        tvStatus = findViewById(R.id.tvDetailStatus);
        tvCategory = findViewById(R.id.tvDetailCategory);
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEditDetail);

        equipmentDAO = new EquipmentDAO(this);
        categoryDAO = new CategoryDAO(this);
    }

    private void loadData() {
        equipmentDAO.open();
        Equipment e = equipmentDAO.getEquipmentById(equipmentId);
        
        if (e != null) {
            tvId.setText(e.getEquipmentId());
            tvName.setText(e.getEquipmentName());
            tvBrand.setText(e.getBrand());
            tvYear.setText(String.valueOf(e.getManufactureYear()));
            
            tvStatus.setText(e.getDisplayStatus(this));

            // Prefer the category name, but fall back to the stored ID.
            categoryDAO.open();
            Category cat = categoryDAO.getCategoryById(e.getCategoryId());
            if (cat != null) {
                tvCategory.setText(cat.getCategoryName());
            } else {
                tvCategory.setText(e.getCategoryId());
            }
        }
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(EquipmentDetailActivity.this, EquipmentFormActivity.class);
            intent.putExtra(EquipmentFormActivity.EXTRA_EQUIPMENT_ID, equipmentId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh after returning from the edit screen.
        loadData();
    }
}
