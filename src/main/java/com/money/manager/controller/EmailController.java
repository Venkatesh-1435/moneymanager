package com.money.manager.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.money.manager.entity.ProfileEntity;
import com.money.manager.service.EmailService;
import com.money.manager.service.ExcelService;
import com.money.manager.service.ExpenseService;
import com.money.manager.service.IncomeService;
import com.money.manager.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmailController {
	
	private final ExcelService excelService;
	private final IncomeService incomeService;
	private final ExpenseService expenseService;
	private final EmailService emailService;
	private final ProfileService profileService;
	
	@GetMapping("/income-excel")
	public ResponseEntity<Void> emailExcel() throws IOException{
		ProfileEntity profile=profileService.getCurrProfileEntity();
		ByteArrayOutputStream os=new ByteArrayOutputStream();
		excelService.writeIncomesToExcel(os, incomeService.getCurrentIncomesForCurrentUser());
		emailService.sendMailWithAttachment(profile.getEmail(),
					"Your Income Report",
					"Please find attached your income report",
					os.toByteArray(),
					"income.xlsx");
		return ResponseEntity.ok(null);
	}
	@GetMapping("/expense-excel")
	public ResponseEntity<Void> emailExpenseExcel() throws IOException{
		ProfileEntity profile=profileService.getCurrProfileEntity();
		ByteArrayOutputStream os=new ByteArrayOutputStream();
		excelService.writeExpensesToExcel(os, expenseService.getCurrentExpensesForCurrentUser());
		emailService.sendMailWithAttachment(profile.getEmail(),
					"Your Expense Report",
					"Please find attached your Expense report",
					os.toByteArray(),
					"expense.xlsx");
		return ResponseEntity.ok(null);
	}

}
