package com.money.manager.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.money.manager.dto.CategoryDto;
import com.money.manager.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
@CrossOrigin("*")
public class CategoryController {
	
	private final CategoryService categoryService;
	
	@PostMapping
	public ResponseEntity<CategoryDto> saveCategory(@RequestBody CategoryDto dto){
		return new ResponseEntity<>(categoryService.saveCategory(dto),HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<CategoryDto>>getCategories(){
		return new ResponseEntity<>(categoryService.getCategories(),HttpStatus.OK);
	}
	@GetMapping("/{type}")
	public ResponseEntity<List<CategoryDto>>getCategoriesByType(@PathVariable String type){
		System.out.println(type);
		return new ResponseEntity<>(categoryService.getCategoriesByType(type),HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id,@RequestBody CategoryDto dto){
		
		return new ResponseEntity<>(categoryService.updateCategoryDto(id,dto),HttpStatus.OK);
	}
}
