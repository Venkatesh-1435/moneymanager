package com.money.manager.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CategoryDto {
	
	private Long id;
	
	private Long profileId;
	
	private String name;
	
	private String icon;
	
	private String type;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;

}
