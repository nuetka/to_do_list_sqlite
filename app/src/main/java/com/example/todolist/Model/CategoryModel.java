package com.example.todolist.Model;

public class CategoryModel {
    private int id;
    private String name;
    private boolean isSelected; // Добавлено новое поле для отслеживания выбранного состояния

    public CategoryModel() {
    }

    // Конструктор с параметрами (если необходим)
    public CategoryModel(int id, String name, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.isSelected = isSelected;
    }

    // Getters и Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return name;
    }
}


