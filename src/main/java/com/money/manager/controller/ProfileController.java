package com.money.manager.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.money.manager.dto.AuthDto;
import com.money.manager.dto.ProfileDto;
import com.money.manager.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProfileController {
	private final ProfileService service;
	
	@PostMapping("/register")
	public ResponseEntity<ProfileDto> register(@RequestBody ProfileDto dto){
		System.out.println(dto.getProfileImageUrl());
		return new ResponseEntity<>(service.registerProfile(dto),HttpStatus.CREATED);
	}
	
	@GetMapping("/activate")
	public ResponseEntity<String> activate( @RequestParam("token") String activationToken){
		boolean isActivate=service.activateProfile(activationToken);
		if(isActivate) {
			return ResponseEntity.ok("Profile activated succesfully");
		}else {
			return new ResponseEntity<>("Activation token not founnd or already used ",HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDto dto){
		try {
			if(!service.isAccountActive(dto.getEmail())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","Account is not active please active account"));
			}
			Map<String, Object>response=service.authenticationAndGenerateToken(dto);
			return ResponseEntity.ok(response);
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message",ex.getMessage()));
		}
		
	}
	
	@GetMapping("/test")
	public String check() {
		return "your jwt work succesfully";
	}
	
	@GetMapping("/profile")
	public ResponseEntity<?> getPublicProfile(){
		ProfileDto dto=service.getPublicProfileDto(null);
		return ResponseEntity.ok(dto);
	}
	
	
	
	
	
	
	
	
	
	
}
