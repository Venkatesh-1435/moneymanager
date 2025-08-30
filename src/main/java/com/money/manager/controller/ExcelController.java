package com.money.manager.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.money.manager.service.ExcelService;
import com.money.manager.service.ExpenseService;
import com.money.manager.service.IncomeService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ExcelController {

    private final ExcelService excelService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @GetMapping("/download/income")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=income.xlsx");

        excelService.writeIncomesToExcel(
                response.getOutputStream(),
                incomeService.getCurrentIncomesForCurrentUser()
        );
    }

    @GetMapping("/download/expense")
    public void downloadExpense(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=expense.xlsx");

        excelService.writeExpensesToExcel(
                response.getOutputStream(),
                expenseService.getCurrentExpensesForCurrentUser()
        );
    }
}
