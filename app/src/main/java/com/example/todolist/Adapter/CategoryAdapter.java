package com.example.todolist.Adapter;

import com.example.todolist.Model.CategoryModel;
import com.example.todolist.R;
import com.example.todolist.Utils.DataBaseHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<CategoryModel> categories;
    private DataBaseHelper dbHelper;

    public CategoryAdapter(List<CategoryModel> categories, DataBaseHelper dbHelper) {
        this.categories = categories;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private CheckBox categoryCheckBox;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryCheckBox = itemView.findViewById(R.id.categoryCheckBox);
        }

        void bind(CategoryModel category) {
            categoryCheckBox.setText(category.getName());
            categoryCheckBox.setChecked(category.isSelected());

            categoryCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Обновляем состояние в базе данных
                category.setSelected(isChecked); // Обновляем модель
                dbHelper.updateCategorySelectedStatus(category.getId(), isChecked);
            });
        }
    }
}