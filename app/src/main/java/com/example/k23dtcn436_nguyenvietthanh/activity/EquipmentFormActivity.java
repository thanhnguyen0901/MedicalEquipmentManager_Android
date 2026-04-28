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

public class EquipmentFormActivity extends AppCompatActivity {
    public static final String EXTRA_EQUIPMENT_ID = "EQUIPMENT_ID";
    private static final int MIN_MANUFACTURE_YEAR = 1900;
    private static final int MAX_FUTURE_YEAR_OFFSET = 1;

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
        loadStatusOptions();
        loadCategories();
        loadEditDataIfNeeded();
        setupBackButton();
        setupListeners();
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

    private void loadStatusOptions() {
        String[] statusOptions = getResources().getStringArray(R.array.status_array_ui);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statusOptions);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(statusAdapter);
    }

    private void loadCategories() {
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

    private void loadEditDataIfNeeded() {
        if (!getIntent().hasExtra(EXTRA_EQUIPMENT_ID)) {
            return;
        }

        editEquipmentId = getIntent().getStringExtra(EXTRA_EQUIPMENT_ID);
        tvTitle.setText(R.string.form_title_edit);
        etId.setEnabled(false); // ID cannot be changed in Edit mode

        equipmentDAO.open();
        Equipment equipment = equipmentDAO.getEquipmentById(editEquipmentId);
        if (equipment == null) {
            Toast.makeText(this, R.string.err_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bindEquipmentForEdit(equipment);
    }

    private void bindEquipmentForEdit(Equipment equipment) {
        etId.setText(equipment.getEquipmentId());
        etName.setText(equipment.getEquipmentName());
        etBrand.setText(equipment.getBrand());
        etYear.setText(String.valueOf(equipment.getManufactureYear()));

        int statusIndex = Equipment.getStatusSelectionIndex(this, equipment.getStatus());
        if (statusIndex >= 0) {
            spnStatus.setSelection(statusIndex);
        }

        selectCategory(equipment.getCategoryId());
    }

    private void selectCategory(String categoryId) {
        if (categoryList == null || categoryList.isEmpty()) {
            return;
        }

        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getCategoryId().equals(categoryId)) {
                spnCategory.setSelection(i);
                break;
            }
        }
    }

    private void setupBackButton() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveEquipment());
    }

    private void saveEquipment() {
        String id = etId.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String brand = etBrand.getText().toString().trim();
        String yearStr = etYear.getText().toString().trim();

        if (!validateInputs(id, name, brand, yearStr)) {
            return;
        }

        int selectedCatPos = spnCategory.getSelectedItemPosition();
        if (!validateSelectedCategory(selectedCatPos)) {
            return;
        }

        int selectedStatusPos = spnStatus.getSelectedItemPosition();
        if (!validateSelectedStatus(selectedStatusPos)) {
            return;
        }

        Integer year = parseManufactureYear(yearStr);
        if (year == null) {
            return;
        }

        Equipment equipment = buildEquipmentFromInputs(id, name, brand, year,
                getSelectedStatusDbValue(selectedStatusPos),
                getSelectedCategoryId(selectedCatPos));

        equipmentDAO.open();
        boolean success;
        if (editEquipmentId == null) {
            if (equipmentDAO.getEquipmentById(id) != null) {
                Toast.makeText(this, R.string.err_duplicate_id, Toast.LENGTH_LONG).show();
                return;
            }
            success = saveNewEquipment(equipment);
        } else {
            success = updateExistingEquipment(equipment);
        }

        handleSaveResult(success);
    }

    private boolean validateInputs(String id, String name, String brand, String yearStr) {
        if (id.isEmpty() || name.isEmpty() || brand.isEmpty() || yearStr.isEmpty()) {
            Toast.makeText(this, R.string.err_empty_fields, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateSelectedCategory(int selectedCatPos) {
        if (categoryList == null || categoryList.isEmpty()) {
            Toast.makeText(this, R.string.err_no_category, Toast.LENGTH_LONG).show();
            return false;
        }
        if (selectedCatPos < 0 || selectedCatPos >= categoryList.size()) {
            Toast.makeText(this, R.string.err_invalid_cat, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateSelectedStatus(int selectedStatusPos) {
        if (selectedStatusPos < 0) {
            Toast.makeText(this, R.string.err_invalid_status, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Integer parseManufactureYear(String yearStr) {
        int year;
        try {
            year = Integer.parseInt(yearStr);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            // Allow one future year for newly ordered equipment.
            int maxYear = currentYear + MAX_FUTURE_YEAR_OFFSET;
            if (year < MIN_MANUFACTURE_YEAR || year > maxYear) {
                String errMsg = String.format(getString(R.string.err_invalid_year_format), maxYear);
                Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.err_numeric_year, Toast.LENGTH_SHORT).show();
            return null;
        }
        return year;
    }

    private String getSelectedCategoryId(int selectedCatPos) {
        return categoryList.get(selectedCatPos).getCategoryId();
    }

    private String getSelectedStatusDbValue(int selectedStatusPos) {
        return getResources().getStringArray(R.array.status_array_db)[selectedStatusPos];
    }

    private Equipment buildEquipmentFromInputs(String id, String name, String brand, int year, String status, String categoryId) {
        return new Equipment(id, name, brand, year, status, categoryId);
    }

    private boolean saveNewEquipment(Equipment equipment) {
        return equipmentDAO.insertEquipment(equipment);
    }

    private boolean updateExistingEquipment(Equipment equipment) {
        return equipmentDAO.updateEquipment(equipment);
    }

    private void handleSaveResult(boolean success) {
        if (success) {
            Toast.makeText(this, R.string.msg_save_success, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.err_db_save, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (equipmentDAO != null) equipmentDAO.close();
        if (categoryDAO != null) categoryDAO.close();
    }
}
