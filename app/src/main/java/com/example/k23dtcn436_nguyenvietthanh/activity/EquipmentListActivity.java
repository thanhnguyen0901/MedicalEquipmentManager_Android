package com.example.k23dtcn436_nguyenvietthanh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.k23dtcn436_nguyenvietthanh.R;
import com.example.k23dtcn436_nguyenvietthanh.adapter.EquipmentAdapter;
import com.example.k23dtcn436_nguyenvietthanh.dao.EquipmentDAO;
import com.example.k23dtcn436_nguyenvietthanh.model.Equipment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class EquipmentListActivity extends AppCompatActivity {

    private ListView lvEquipment;
    private ExtendedFloatingActionButton fabAdd;
    private ImageButton btnBack;
    private EquipmentDAO equipmentDAO;
    private ArrayList<Equipment> equipmentList = new ArrayList<>();
    private EquipmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_list);

        initViews();
        setupEvents();
    }

    private void initViews() {
        lvEquipment = findViewById(R.id.lvEquipment);
        fabAdd = findViewById(R.id.fabAddEquipment);
        btnBack = findViewById(R.id.btnBack);
        equipmentDAO = new EquipmentDAO(this);
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(EquipmentListActivity.this, EquipmentFormActivity.class);
            startActivity(intent);
        });

        lvEquipment.setOnItemClickListener((parent, view, position, id) -> {
            Equipment selected = equipmentList.get(position);
            Intent intent = new Intent(EquipmentListActivity.this, EquipmentDetailActivity.class);
            intent.putExtra(EquipmentFormActivity.EXTRA_EQUIPMENT_ID, selected.getEquipmentId());
            startActivity(intent);
        });

        lvEquipment.setOnItemLongClickListener((parent, view, position, id) -> {
            showActionDialog(equipmentList.get(position));
            return true; // Consume long click
        });
    }

    private void loadData() {
        equipmentDAO.open();
        equipmentList = equipmentDAO.getAllEquipment();
        adapter = new EquipmentAdapter(this, equipmentList, new EquipmentAdapter.OnEquipmentActionListener() {
            @Override
            public void onDelete(Equipment equipment) {
                showDeleteConfirmDialog(equipment);
            }

            @Override
            public void onEdit(Equipment equipment) {
                Intent intent = new Intent(EquipmentListActivity.this, EquipmentFormActivity.class);
                intent.putExtra(EquipmentFormActivity.EXTRA_EQUIPMENT_ID, equipment.getEquipmentId());
                startActivity(intent);
            }
        });
        lvEquipment.setAdapter(adapter);
    }

    private void showActionDialog(Equipment equipment) {
        String[] options = {getString(R.string.form_title_edit), getString(R.string.dialog_delete)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(String.format(getString(R.string.dialog_options_format), equipment.getEquipmentName()));
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                Intent intent = new Intent(EquipmentListActivity.this, EquipmentFormActivity.class);
                intent.putExtra(EquipmentFormActivity.EXTRA_EQUIPMENT_ID, equipment.getEquipmentId());
                startActivity(intent);
            } else if (which == 1) {
                showDeleteConfirmDialog(equipment);
            }
        });
        builder.show();
    }

    private void showDeleteConfirmDialog(Equipment equipment) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete)
                .setMessage(String.format(getString(R.string.msg_confirm_delete_format), equipment.getEquipmentName()))
                .setPositiveButton(R.string.dialog_delete, (dialog, which) -> {
                    equipmentDAO.open();
                    if (equipmentDAO.deleteEquipment(equipment.getEquipmentId())) {
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
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (equipmentDAO != null) {
            equipmentDAO.close();
        }
    }
}
