package com.money.manager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.money.manager.dto.ExpenseDto;
import com.money.manager.service.ExpenseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
@CrossOrigin("*")
public class ExpenseController {

	private final ExpenseService expenseService;
	
	@PostMapping
	public ResponseEntity<ExpenseDto>addExpense(@RequestBody ExpenseDto dto){
		System.out.println(dto);
		ExpenseDto adDto=expenseService.addExpense(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(adDto);
	}
	
	@GetMapping
	public ResponseEntity<List<ExpenseDto>> getExpenses(){
		List<ExpenseDto> expenses=expenseService.getCurrentExpensesForCurrentUser();
		return ResponseEntity.ok(expenses);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteExpense(@PathVariable Long id){
		expenseService.deleteExpense(id);
		return ResponseEntity.noContent().build();
	}
}
