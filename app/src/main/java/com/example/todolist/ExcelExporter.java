package com.example.todolist;

import com.example.todolist.Model.ToDoModel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class ExcelExporter {

    public static void exportTasksToExcel(List<ToDoModel> tasks, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Tasks");

        // Создаем заголовок
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Description");
        headerRow.createCell(2).setCellValue("Start");
        headerRow.createCell(3).setCellValue("End");
        headerRow.createCell(4).setCellValue("Status");

        // Заполняем данные
        int rowNum = 1;
        for (ToDoModel task : tasks) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(task.getId());
            row.createCell(1).setCellValue(task.getTask());
            row.createCell(2).setCellValue(task.getStart());
            row.createCell(3).setCellValue(task.getEnd());
            row.createCell(4).setCellValue(task.getStatus());
        }

        // Сохраняем в файл
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}