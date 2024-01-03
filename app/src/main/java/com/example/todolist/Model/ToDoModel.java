package com.example.todolist.Model;

public class ToDoModel {

    private String task;
    private String date;
    private int id , status, categoryId, priority, snoty, enoty ;

    public int getSnoty() {
        return snoty;
    }

    public void setSnoty(int snoty) {
        this.snoty = snoty;
    }

    public int getEnoty() {
        return enoty;
    }

    public void setEnoty(int enoty) {
        this.enoty = enoty;
    }

    public int getPriority() {
        return priority;
    } // 5 цифр. 1-самый топ если 6-я цифра - 0 то не выбрана

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}