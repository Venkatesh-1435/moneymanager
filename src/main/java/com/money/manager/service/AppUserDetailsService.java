package com.money.manager.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.money.manager.entity.ProfileEntity;
import com.money.manager.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
	
	private final ProfileRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		ProfileEntity entity=repository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Email not found " + email));
		return User.builder()
				.username(entity.getEmail())
				.password(entity.getPassword())
				.authorities(Collections.emptyList())
				.build();
	}

}
