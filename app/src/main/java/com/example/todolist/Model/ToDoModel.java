package com.example.todolist.Model;

import java.util.List;

public class ToDoModel {

    private int isRoutine;
    private String repeatEndDate;
    private String repeatDays; // строка в формате 1010010 из 7 символов

    private String task, duration;
    private String date, start, end;// дата время начала и конца
    private int id , status, categoryId, priority, snoty, enoty ;

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(String repeatDays) {
        this.repeatDays = repeatDays;
    }

    public String getRepeatEndDate() {
        return repeatEndDate;
    }

    public void setRepeatEndDate(String repeatEndDate) {
        this.repeatEndDate = repeatEndDate;
    }

    public int getIsRoutine() {
        return isRoutine;
    }

    public void setIsRoutine(int isRoutine) {
        this.isRoutine = isRoutine;
    }

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