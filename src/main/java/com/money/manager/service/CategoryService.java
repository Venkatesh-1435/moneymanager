package com.money.manager.service;

import java.util.List;

import org.springframework.stereotype.Service;


import com.money.manager.dto.CategoryDto;
import com.money.manager.entity.CategoryEntity;
import com.money.manager.entity.ProfileEntity;
import com.money.manager.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	
	private final CategoryRepository categoryRepository;
	
	private final ProfileService profileService;
	
	public CategoryDto saveCategory(CategoryDto dto) {
		ProfileEntity profileEntity=profileService.getCurrProfileEntity();
		if(categoryRepository.existsByNameAndProfileId(dto.getName(), profileEntity.getId())) {
			throw new RuntimeException("Category name already exists");
		}
		
		CategoryEntity entity=toEntity(dto, profileEntity);
		
		return toDto(categoryRepository.save(entity));
	}
	
	public List<CategoryDto> getCategories(){
		ProfileEntity entity=profileService.getCurrProfileEntity();
		List<CategoryEntity> categories=categoryRepository.findByProfileId(entity.getId());
		
		return categories.stream().map(data->toDto(data)).toList();
		
	}
	
	public List<CategoryDto>getCategoriesByType(String type){
		ProfileEntity entity=profileService.getCurrProfileEntity();
		System.out.println(entity);
		List<CategoryEntity> category=categoryRepository.findByTypeAndProfileId(type, entity.getId());
		System.out.println(category);
		return category.stream().map(data->toDto(data)).toList();
	}
	
	public CategoryDto updateCategoryDto(Long categoryId,CategoryDto dto) {
		ProfileEntity entity=profileService.getCurrProfileEntity();
		CategoryEntity categoryEntity=categoryRepository.findByIdAndProfileId(categoryId, entity.getId()).orElseThrow(()-> new RuntimeException("Category not found"));
		categoryEntity.setName(dto.getName());
		categoryEntity.setIcon(dto.getIcon());
		categoryEntity.setType(dto.getType());
		return toDto(categoryRepository.save(categoryEntity));
	}
	
	
	private CategoryEntity toEntity(CategoryDto category,ProfileEntity profile) {
		return CategoryEntity.builder()
				.name(category.getName())
				.icon(category.getIcon())
				.type(category.getType())
				.profile(profile)
				.build();
	}
	
	private CategoryDto toDto(CategoryEntity entity) {
		return CategoryDto.builder()
				.id(entity.getId())
				.profileId(entity.getProfile() !=null ? entity.getProfile().getId():null)
				.name(entity.getName())
				.icon(entity.getIcon())
				.createdAt(entity.getCreateAt())
				.updatedAt(entity.getUpdatedAt())
				.type(entity.getType())
				.build();
	}

}
