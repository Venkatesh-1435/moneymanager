package com.money.manager.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

import com.money.manager.dto.ExpenseDto;
import com.money.manager.dto.IncomeDto;
import com.money.manager.dto.RecentTransactionDto;
import com.money.manager.entity.ProfileEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashBoardService {
	
	private final IncomeService incomeService;
	private final ExpenseService expenseService;
	private final ProfileService profileService;
	
	public Map<String, Object>getDashBoard(){
		ProfileEntity profileEntity=profileService.getCurrProfileEntity();
		Map<String, Object> data=new LinkedHashMap<>();
		List<IncomeDto> latestIncomes=incomeService.getLatest5Incomes();
		List<ExpenseDto>latestExpenses=expenseService.getLatest5Expenses();
		List<RecentTransactionDto> allTransactions = Stream.concat(
	            latestIncomes.stream().map(dto -> incomesToRecentTransactionDto(dto, profileEntity)),
	            latestExpenses.stream().map(dto -> expenseToRecentTransactionDto(dto, profileEntity))
	    )
	    .sorted((t1, t2) -> {
	        int dateCompare = t2.getDate().compareTo(t1.getDate()); // sort by date DESC
	        if (dateCompare == 0) {
	            return t2.getCreatedAt().compareTo(t1.getCreatedAt()); // then createdAt DESC
	        }
	        return dateCompare;
	    })
	    .toList();
		 data.put("totalBalance", incomeService.getTotalExpense().subtract(expenseService.getTotalExpense()));
		 data.put("totalIncome", incomeService.getTotalExpense());
		 data.put("totalExpenses", expenseService.getTotalExpense());
		 data.put("recent5Expense", latestExpenses);
		 data.put("recent5Incomes", latestIncomes);
		 data.put("recentTransactions", allTransactions);
		 return data;
	}
	
	public RecentTransactionDto incomesToRecentTransactionDto(IncomeDto dto,ProfileEntity profileEntity) {
		return RecentTransactionDto.builder()
				.id(dto.getId())
				.profileId(profileEntity.getId())
				.icon(dto.getIcon())
				.name(dto.getName())
				.amount(dto.getAmount())
				.date(dto.getDate())
				.createdAt(dto.getCreatedAt())
				.updatedAt(dto.getUpdateAt())
				.type("income")
				.build();
	}
	public RecentTransactionDto expenseToRecentTransactionDto(ExpenseDto dto,ProfileEntity profileEntity) {
		return RecentTransactionDto.builder()
				.id(dto.getId())
				.profileId(profileEntity.getId())
				.icon(dto.getIcon())
				.name(dto.getName())
				.amount(dto.getAmount())
				.date(dto.getDate())
				.createdAt(dto.getCreatedAt())
				.updatedAt(dto.getUpdateAt())
				.type("expense")
				.build();
	}
	
	

}
