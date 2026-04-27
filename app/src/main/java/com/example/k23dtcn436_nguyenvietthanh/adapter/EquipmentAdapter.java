package com.example.k23dtcn436_nguyenvietthanh.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.k23dtcn436_nguyenvietthanh.R;
import com.example.k23dtcn436_nguyenvietthanh.model.Equipment;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Custom Adapter to display Medical Equipment in a ListView.
 */
public class EquipmentAdapter extends BaseAdapter {

    private Context context;
    private List<Equipment> equipmentList;
    private OnEquipmentActionListener listener;

    public interface OnEquipmentActionListener {
        void onDelete(Equipment equipment);
        void onEdit(Equipment equipment);
    }

    public EquipmentAdapter(Context context, List<Equipment> equipmentList, OnEquipmentActionListener listener) {
        this.context = context;
        this.equipmentList = equipmentList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return equipmentList != null ? equipmentList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return equipmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_equipment, parent, false);
        }

        Equipment equipment = (Equipment) getItem(position);

        TextView tvName = convertView.findViewById(R.id.tvEquipName);
        TextView tvBrand = convertView.findViewById(R.id.tvEquipBrand);
        TextView tvYear = convertView.findViewById(R.id.tvEquipYear);
        TextView tvStatus = convertView.findViewById(R.id.tvEquipStatus);
        View ivEdit = convertView.findViewById(R.id.ivEditEquipment);
        View ivDelete = convertView.findViewById(R.id.ivDeleteEquipment);

        if (equipment != null) {
            tvName.setText(equipment.getEquipmentName());
            tvBrand.setText(equipment.getBrand());
            tvYear.setText(String.valueOf(equipment.getManufactureYear()));
            
            // Map status from DB to UI
            String status = equipment.getStatus();
            String[] dbStatuses = context.getResources().getStringArray(R.array.status_array_db);
            String[] uiStatuses = context.getResources().getStringArray(R.array.status_array_ui);
            for (int i = 0; i < dbStatuses.length; i++) {
                if (dbStatuses[i].equalsIgnoreCase(status)) {
                    status = uiStatuses[i];
                    break;
                }
            }
            tvStatus.setText(status);

            // Simple status styling logic
            if ("Active".equalsIgnoreCase(equipment.getStatus())) {
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_active));
                tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.status_active_bg));
            } else if ("Broken".equalsIgnoreCase(equipment.getStatus())) {
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_broken));
                tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_field));
            } else if ("Maintenance".equalsIgnoreCase(equipment.getStatus())) {
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_maintenance));
                tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_field));
            } else {
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
                tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_field));
            }

            if (ivEdit != null) {
                if (listener != null) {
                    ivEdit.setVisibility(View.VISIBLE);
                    ivEdit.setOnClickListener(v -> listener.onEdit(equipment));
                } else {
                    ivEdit.setVisibility(View.GONE);
                }
            }
            if (ivDelete != null) {
                if (listener != null) {
                    ivDelete.setVisibility(View.VISIBLE);
                    ivDelete.setOnClickListener(v -> listener.onDelete(equipment));
                } else {
                    ivDelete.setVisibility(View.GONE);
                }
            }
        }

        return convertView;
    }
}
