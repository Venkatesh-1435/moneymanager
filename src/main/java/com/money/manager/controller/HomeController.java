package com.money.manager.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	
	
	@GetMapping("/status")
	public String healthCheck() {
		return "OK Application is running ";
	}

}
