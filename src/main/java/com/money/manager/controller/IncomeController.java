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


import com.money.manager.dto.IncomeDto;
import com.money.manager.service.IncomeService;


import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("incomes")
public class IncomeController {
	
	private final IncomeService incomeService;
	
	@PostMapping
	public ResponseEntity<IncomeDto>addExpense(@RequestBody IncomeDto dto){
		IncomeDto adDto=incomeService.addExpense(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(adDto);
	}
	
	@GetMapping
	public ResponseEntity<List<IncomeDto>> getIncomes(){
		List<IncomeDto> incomes=incomeService.getCurrentIncomesForCurrentUser();
		return ResponseEntity.ok(incomes);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteExpense(@PathVariable Long id){
		incomeService.deleteIncome(id);
		return ResponseEntity.noContent().build();
	}

	

}
