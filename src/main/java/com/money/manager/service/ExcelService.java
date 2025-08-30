package com.money.manager.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.money.manager.dto.IncomeDto;
import com.money.manager.dto.ExpenseDto;

@Service
public class ExcelService {

    public void writeIncomesToExcel(OutputStream os, List<IncomeDto> incomes) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Incomes");

            // Header row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("S.No");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Category");
            header.createCell(3).setCellValue("Amount");
            header.createCell(4).setCellValue("Date");

            // Fill data rows
            IntStream.range(0, incomes.size())
                    .forEach(i -> {
                        IncomeDto dto = incomes.get(i);
                        Row row = sheet.createRow(i + 1);
                        row.createCell(0).setCellValue(i + 1);
                        row.createCell(1).setCellValue(dto.getName());
                        row.createCell(2).setCellValue(dto.getCategoryName());
                        row.createCell(3).setCellValue(dto.getAmount().toString());
                        row.createCell(4).setCellValue(dto.getDate().toString());
                    });

            for (int col = 0; col < 5; col++) {
                sheet.autoSizeColumn(col);
            }

            workbook.write(os);
        }
    }

    public void writeExpensesToExcel(OutputStream os, List<ExpenseDto> expenses) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Expenses");

            // Header row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("S.No");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Category");
            header.createCell(3).setCellValue("Amount");
            header.createCell(4).setCellValue("Date");

            // Fill data rows
            IntStream.range(0, expenses.size())
                    .forEach(i -> {
                        ExpenseDto dto = expenses.get(i);
                        Row row = sheet.createRow(i + 1);
                        row.createCell(0).setCellValue(i + 1);
                        row.createCell(1).setCellValue(dto.getName());
                        row.createCell(2).setCellValue(dto.getCategoryName());
                        row.createCell(3).setCellValue(dto.getAmount().toString());
                        row.createCell(4).setCellValue(dto.getDate().toString());
                    });

            for (int col = 0; col < 5; col++) {
                sheet.autoSizeColumn(col);
            }

            workbook.write(os);
        }
    }
}
