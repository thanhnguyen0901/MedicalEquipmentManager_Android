package com.example.k23dtcn436_nguyenvietthanh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.k23dtcn436_nguyenvietthanh.R;
import com.example.k23dtcn436_nguyenvietthanh.model.Category;
import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private List<Category> categoryList;
    private OnCategoryActionListener listener;

    public interface OnCategoryActionListener {
        void onDelete(Category category);
        void onEdit(Category category);
    }

    public CategoryAdapter(Context context, List<Category> categoryList, OnCategoryActionListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        }

        Category category = categoryList.get(position);
        TextView tvName = convertView.findViewById(R.id.tvCategoryName);
        TextView tvId = convertView.findViewById(R.id.tvCategoryId);
        ImageView ivEdit = convertView.findViewById(R.id.ivEditCategory);
        ImageView ivDelete = convertView.findViewById(R.id.ivDeleteCategory);

        tvName.setText(category.getCategoryName());
        tvId.setText(context.getString(R.string.label_category_id, category.getCategoryId()));

        ivEdit.setOnClickListener(v -> listener.onEdit(category));
        ivDelete.setOnClickListener(v -> listener.onDelete(category));

        return convertView;
    }
}
