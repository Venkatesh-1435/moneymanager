package com.money.manager.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.money.manager.dto.FilterDto;
import com.money.manager.dto.IncomeDto;
import com.money.manager.service.ExpenseService;
import com.money.manager.service.IncomeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
@CrossOrigin("*")
public class FilterController {
	private final ExpenseService expenseService;
	private final IncomeService incomeService;
	
	@PostMapping
	public ResponseEntity<?> filterTransaction(@RequestBody FilterDto filter){
		LocalDate startDate=filter.getStartDate()!=null?filter.getStartDate():LocalDate.MIN;
		LocalDate endDate=filter.getEndDate()!=null?filter.getEndDate():LocalDate.now();
		String keyword=filter.getKeyword()!=null?filter.getKeyword():"";
		String sortField=filter.getSortField()!=null?filter.getSortField():"date";
		Sort.Direction direction="desc".equalsIgnoreCase(filter.getSortOrder())? Sort.Direction.DESC:Sort.Direction.ASC;
		Sort sort=Sort.by(direction,sortField);
		if("income".equalsIgnoreCase(filter.getType())) {
			List<IncomeDto> incomes=incomeService.filterIncomes(startDate, endDate, keyword, sort);
			return ResponseEntity.ok(incomes);
		}else if("expense".equalsIgnoreCase(filter.getType())) {
			return ResponseEntity.ok(expenseService.filterExpenses(startDate, endDate, keyword, sort));
		}else {
			return ResponseEntity.badRequest().body("Invalid type Must be 'income' or 'expense'");
		}
	}
	
	
}
