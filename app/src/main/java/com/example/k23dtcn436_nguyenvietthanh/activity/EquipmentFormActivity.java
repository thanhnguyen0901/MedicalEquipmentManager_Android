package com.example.k23dtcn436_nguyenvietthanh.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.k23dtcn436_nguyenvietthanh.R;
import com.example.k23dtcn436_nguyenvietthanh.dao.CategoryDAO;
import com.example.k23dtcn436_nguyenvietthanh.dao.EquipmentDAO;
import com.example.k23dtcn436_nguyenvietthanh.model.Category;
import com.example.k23dtcn436_nguyenvietthanh.model.Equipment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Form Activity for adding or editing Medical Equipment.
 */
public class EquipmentFormActivity extends AppCompatActivity {

    private TextInputEditText etId, etName, etBrand, etYear;
    private Spinner spnStatus, spnCategory;
    private MaterialButton btnSave;
    private ImageButton btnBack;
    private TextView tvTitle;

    private EquipmentDAO equipmentDAO;
    private CategoryDAO categoryDAO;
    private ArrayList<Category> categoryList;
    private String editEquipmentId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_form);

        initViews();
        loadSpinners();
        checkEditMode();
        setupEvents();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvFormTitle);
        etId = findViewById(R.id.etEquipId);
        etName = findViewById(R.id.etEquipName);
        etBrand = findViewById(R.id.etEquipBrand);
        etYear = findViewById(R.id.etEquipYear);
        spnStatus = findViewById(R.id.spnStatus);
        spnCategory = findViewById(R.id.spnCategory);
        btnSave = findViewById(R.id.btnSaveEquipment);
        btnBack = findViewById(R.id.btnBack);

        equipmentDAO = new EquipmentDAO(this);
        categoryDAO = new CategoryDAO(this);
    }

    private void loadSpinners() {
        // 1. Status Spinner
        String[] statusOptions = getResources().getStringArray(R.array.status_array_ui);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statusOptions);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(statusAdapter);

        // 2. Category Spinner
        categoryDAO.open();
        categoryList = categoryDAO.getAllCategories();
        if (categoryList == null) {
            categoryList = new ArrayList<>();
        }
        
        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category c : categoryList) {
            categoryNames.add(c.getCategoryName());
        }

        if (categoryNames.isEmpty()) {
            categoryNames.add(getString(R.string.err_no_category_available));
        }

        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoryNames);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(catAdapter);
    }

    private void checkEditMode() {
        if (getIntent().hasExtra("EQUIPMENT_ID")) {
            editEquipmentId = getIntent().getStringExtra("EQUIPMENT_ID");
            tvTitle.setText(R.string.form_title_edit);
            etId.setEnabled(false); // ID cannot be changed in Edit mode

            equipmentDAO.open();
            Equipment e = equipmentDAO.getEquipmentById(editEquipmentId);
            if (e != null) {
                etId.setText(e.getEquipmentId());
                etName.setText(e.getEquipmentName());
                etBrand.setText(e.getBrand());
                etYear.setText(String.valueOf(e.getManufactureYear()));
                
                // Set Status Spinner (Mapping DB value to UI value)
                String[] dbStatuses = getResources().getStringArray(R.array.status_array_db);
                String[] uiStatuses = getResources().getStringArray(R.array.status_array_ui);
                for (int i = 0; i < dbStatuses.length; i++) {
                    if (dbStatuses[i].equalsIgnoreCase(e.getStatus())) {
                        spnStatus.setSelection(i);
                        break;
                    }
                }
                
                // Set Category Spinner
                if (categoryList != null && !categoryList.isEmpty()) {
                    for (int i = 0; i < categoryList.size(); i++) {
                        if (categoryList.get(i).getCategoryId().equals(e.getCategoryId())) {
                            spnCategory.setSelection(i);
                            break;
                        }
                    }
                }
            } else {
                Toast.makeText(this, R.string.err_not_found, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        if (value == null) return;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveEquipment());
    }

    private void saveEquipment() {
        String id = etId.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String brand = etBrand.getText().toString().trim();
        String yearStr = etYear.getText().toString().trim();

        // 1. Basic Field Validation
        if (id.isEmpty() || name.isEmpty() || brand.isEmpty() || yearStr.isEmpty()) {
            Toast.makeText(this, R.string.err_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Category Validation
        if (categoryList == null || categoryList.isEmpty()) {
            Toast.makeText(this, R.string.err_no_category, Toast.LENGTH_LONG).show();
            return;
        }

        int selectedCatPos = spnCategory.getSelectedItemPosition();
        if (selectedCatPos < 0 || selectedCatPos >= categoryList.size()) {
            Toast.makeText(this, R.string.err_invalid_cat, Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Status Validation
        int selectedStatusPos = spnStatus.getSelectedItemPosition();
        if (selectedStatusPos < 0) {
            Toast.makeText(this, R.string.err_invalid_status, Toast.LENGTH_SHORT).show();
            return;
        }
        String status = getResources().getStringArray(R.array.status_array_db)[selectedStatusPos];

        // 4. Year Validation
        int year;
        try {
            year = Integer.parseInt(yearStr);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (year < 1900 || year > currentYear + 1) { // Allowing +1 for new arrivals
                String errMsg = String.format(getString(R.string.err_invalid_year_format), currentYear + 1);
                Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.err_numeric_year, Toast.LENGTH_SHORT).show();
            return;
        }

        String catId = categoryList.get(selectedCatPos).getCategoryId();
        Equipment equipment = new Equipment(id, name, brand, year, status, catId);
        
        equipmentDAO.open();
        boolean success;
        if (editEquipmentId == null) {
            // Add mode: Check for duplicate ID
            if (equipmentDAO.getEquipmentById(id) != null) {
                Toast.makeText(this, R.string.err_duplicate_id, Toast.LENGTH_LONG).show();
                return;
            }
            success = equipmentDAO.insertEquipment(equipment);
        } else {
            // Edit mode
            success = equipmentDAO.updateEquipment(equipment);
        }

        if (success) {
            Toast.makeText(this, R.string.msg_save_success, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.err_db_save, Toast.LENGTH_SHORT).show();
        }
    }

}
