package com.money.manager.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.money.manager.service.DashBoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DashBoardController {
	private final DashBoardService dashBoardService;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>>getDashborad(){
		Map<String, Object>dataMap=dashBoardService.getDashBoard();
		
		return ResponseEntity.ok(dataMap);
	}
}
