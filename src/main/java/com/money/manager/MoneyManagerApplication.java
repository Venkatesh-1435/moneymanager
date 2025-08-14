package com.money.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoneyManagerApplication {

	public static void main(String[] args) {
		
		
		
		SpringApplication.run(MoneyManagerApplication.class, args);
	}
}
