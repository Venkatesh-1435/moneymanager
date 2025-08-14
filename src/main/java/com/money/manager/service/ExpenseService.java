package com.money.manager.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.money.manager.dto.ExpenseDto;
import com.money.manager.entity.CategoryEntity;
import com.money.manager.entity.ExpenseEntity;
import com.money.manager.entity.ProfileEntity;
import com.money.manager.repository.CategoryRepository;
import com.money.manager.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {
	
	private final CategoryRepository categoryRepository;
	private final ExpenseRepository expenseRepository;
	private final ProfileService profileService;
	
	public ExpenseDto addExpense(ExpenseDto dto) {
		System.out.println(dto.getCategoryId());
		ProfileEntity profile=profileService.getCurrProfileEntity();
		CategoryEntity category=categoryRepository.findById(dto.getCategoryId()).orElseThrow(() ->new RuntimeException("Category Not Found"));
		ExpenseEntity expenseEntity=toEntity(dto, profile, category);
		
		expenseEntity=expenseRepository.save(expenseEntity);
		
		return toDto(expenseEntity);
		
	}
	
	public List<ExpenseDto> getCurrentExpensesForCurrentUser(){
		ProfileEntity profile=profileService.getCurrProfileEntity();
		LocalDate now=LocalDate.now();
		LocalDate startDate=now.withDayOfMonth(1);
		
		LocalDate endDate=now.withDayOfMonth(now.lengthOfMonth());
		
		List<ExpenseEntity> entities=expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
		return entities.stream().map(this::toDto).toList();
		
	}
	
	public void deleteExpense(Long expenseId) {
		
		ProfileEntity profile=profileService.getCurrProfileEntity();
		ExpenseEntity entity=expenseRepository.findById(expenseId).orElseThrow(
				()-> new RuntimeException("Id not found"));
		if(!entity.getProfile().getId().equals(profile.getId())) {
			throw new RuntimeException("Unauthorozed to delete this expense");
		}
		expenseRepository.delete(entity);
		
		
	}
	
	public List<ExpenseDto>getLatest5Expenses(){
		ProfileEntity profileEntity=profileService.getCurrProfileEntity();
		return expenseRepository.findTop5ByProfileIdOrderByDateDesc(profileEntity.getId())
		.stream().map(this::toDto).toList();
	}
	
	//Get total Expenses of current user
	public BigDecimal getTotalExpense() {
		ProfileEntity profileEntity=profileService.getCurrProfileEntity();
		
		BigDecimal total=expenseRepository.findTotalByExpenseByProfileId(profileEntity.getId());
		return total!=null ? total:BigDecimal.ZERO;
	}
	//Filter expenses
	
	public List<ExpenseDto> filterExpenses(LocalDate startDate ,LocalDate endDate,String keyword, Sort sort){
		ProfileEntity profileEntity=profileService.getCurrProfileEntity();
		List<ExpenseEntity> list=expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profileEntity.getId(), startDate, endDate, keyword, sort);
		
		return list.stream().map(this::toDto).toList();
	}
	
	//Notifications
	public List<ExpenseDto> getExpensesUserOnDate(Long profileId,LocalDate date){
		List<ExpenseEntity> list=expenseRepository.findByProfileIdAndDate(profileId, date);
		return list.stream().map(this::toDto).toList();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private ExpenseEntity toEntity(ExpenseDto dto,ProfileEntity profile,CategoryEntity category) {
		return ExpenseEntity.builder()
				.name(dto.getName())
				.icon(dto.getIcon())
				.amount(dto.getAmount())
				.date(dto.getDate())
				.profile(profile)
				.category(category)
				.build();
	}
	
	private ExpenseDto toDto(ExpenseEntity entity) {
		return ExpenseDto.builder()
				.id(entity.getId())
				.name(entity.getName())
				.icon(entity.getIcon())
				.amount(entity.getAmount())
				.categoryId(entity.getCategory()!=null ? entity.getCategory().getId():null)
				.categoryName(entity.getCategory()!=null? entity.getCategory().getName():"N/A")
				.date(entity.getDate())
				.createdAt(entity.getCreatedAt())
				.updateAt(entity.getUpdateAt())
				.build();
	}
	
	
	
	

}
