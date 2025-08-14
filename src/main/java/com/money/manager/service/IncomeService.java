package com.money.manager.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.money.manager.dto.ExpenseDto;
import com.money.manager.dto.IncomeDto;
import com.money.manager.entity.CategoryEntity;
import com.money.manager.entity.ExpenseEntity;
import com.money.manager.entity.IncomeEntity;
import com.money.manager.entity.ProfileEntity;
import com.money.manager.repository.CategoryRepository;
import com.money.manager.repository.IncomeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncomeService {

	private final CategoryRepository categoryRepository;

	private final IncomeRepository incomeRepository;

	private final ProfileService profileService;

	public IncomeDto addExpense(IncomeDto dto) {
		ProfileEntity profile = profileService.getCurrProfileEntity();
		CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Category Not Found"));
		IncomeEntity incomeEntity = toEntity(dto, profile, category);

		incomeEntity = incomeRepository.save(incomeEntity);

		return toDto(incomeEntity);

	}

	public List<IncomeDto> getCurrentIncomesForCurrentUser() {
		ProfileEntity profile = profileService.getCurrProfileEntity();
		LocalDate now = LocalDate.now();
		LocalDate startDate = now.withDayOfMonth(1);

		LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

		List<IncomeEntity> entities = incomeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate,
				endDate);
		return entities.stream().map(this::toDto).toList();

	}

	public void deleteIncome(Long incomeId) {

		ProfileEntity profile = profileService.getCurrProfileEntity();
		IncomeEntity entity = incomeRepository.findById(incomeId)
				.orElseThrow(() -> new RuntimeException("Id not found"));
		if (!entity.getProfile().getId().equals(profile.getId())) {
			throw new RuntimeException("Unauthorozed to delete this expense");
		}
		incomeRepository.delete(entity);

	}

	private IncomeEntity toEntity(IncomeDto dto, ProfileEntity profile, CategoryEntity category) {
		return IncomeEntity.builder().name(dto.getName()).icon(dto.getIcon()).amount(dto.getAmount())
				.date(dto.getDate()).profile(profile).category(category).build();
	}

	private IncomeDto toDto(IncomeEntity entity) {
		return IncomeDto.builder().id(entity.getId()).name(entity.getName()).icon(entity.getIcon())
				.categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
				.categoryName(entity.getCategory() != null ? entity.getCategory().getName() : "N/A")
				.date(entity.getDate()).amount(entity.getAmount())

				.createdAt(entity.getCreatedAt()).updateAt(entity.getUpdateAt()).build();
	}
	public List<IncomeDto>getLatest5Incomes(){
		ProfileEntity profileEntity=profileService.getCurrProfileEntity();
		List<IncomeDto> dtos= incomeRepository.findTop5ByProfileIdOrderByDateDesc(profileEntity.getId())
		.stream().map(this::toDto).toList();
		return dtos;
	}
	
	//Get total Expenses of current user
	public BigDecimal getTotalExpense() {
		ProfileEntity profileEntity=profileService.getCurrProfileEntity();
		
		BigDecimal total=incomeRepository.findTotalByIncomesByProfileId(profileEntity.getId());
		return total!=null ? total:BigDecimal.ZERO;
	}
	
	public List<IncomeDto> filterIncomes(LocalDate startDate ,LocalDate endDate,String keyword, Sort sort){
		ProfileEntity profileEntity=profileService.getCurrProfileEntity();
		List<IncomeEntity> list=incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profileEntity.getId(), startDate, endDate, keyword, sort);
		
		return list.stream().map(this::toDto).toList();
	}
	

}
