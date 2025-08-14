package com.money.manager.service;


import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.money.manager.dto.AuthDto;
import com.money.manager.dto.ProfileDto;
import com.money.manager.entity.ProfileEntity;
import com.money.manager.repository.ProfileRepository;
import com.money.manager.util.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
	
	private final PasswordEncoder encoder;
	
	private final JwtUtils utils;
	
	private final AuthenticationManager manager;
	
	private final ProfileRepository repository;
	
	private final EmailService emailService;
	
	@Value("${activate.url}")
	private String activationUrl;
	
	public ProfileDto registerProfile(ProfileDto profileDto) {
		ProfileEntity entity=convertToEntity(profileDto);
		entity.setActivationToken(UUID.randomUUID().toString());
		//entity.setPassword(encoder.encode(profileDto.getPassword()));
		entity=repository.save(entity);
		//send activation email
		String activationLink=activationUrl+"/api/v1.0/activate?token="+entity.getActivationToken();
		String body="Click on the following link to activate your account: "+activationLink;
		String subject="Activate your Money Manager Account";
		emailService.sendMail(entity.getEmail(), subject, body);
		return toDto(entity);
	}
	
	public ProfileEntity convertToEntity(ProfileDto profileDto) {
		return ProfileEntity.builder()
				.id(profileDto.getId())
				.fullName(profileDto.getFullName())
				.email(profileDto.getEmail())
				.password(encoder.encode(profileDto.getPassword()))
				.createdAt(profileDto.getCreatedAt())
				.updateAt(profileDto.getUpdateAt())
				.build();
	}
	
	public ProfileDto toDto(ProfileEntity entity) {
		return ProfileDto.builder()
				.id(entity.getId())
				.fullName(entity.getFullName())
				.email(entity.getEmail())
				.profileImageUrl(entity.getProfileImageUrl())
				.createdAt(entity.getCreatedAt())
				.updateAt(entity.getUpdateAt())
				.build();
	}
	
	public Boolean activateProfile(String activateToken) {
		return repository.findByActivationToken(activateToken)
			.map(profile->{
			profile.setIsActive(true);
			repository.save(profile);
			return true;
		}).orElse(false);
	}
	
	public Boolean isAccountActive(String email) {
		return repository.findByEmail(email).map(ProfileEntity::getIsActive).orElse(false);
	}
	
	public ProfileEntity getCurrProfileEntity() {
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		String email=authentication.getName();
		return repository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("No profile exist "+email));
	}
	
	public ProfileDto getPublicProfileDto(String email) {
		ProfileEntity entity=null;
		if(email==null) {
			entity=getCurrProfileEntity();
		}else {
			entity=repository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("No profile exist "+email));
		}
		return toDto(entity);
	}

	public Map<String, Object> authenticationAndGenerateToken(AuthDto dto) {
		// TODO Auto-generated method stub
		try {
			manager.authenticate(
					new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
			String token=utils.generateToken(dto.getEmail());
			return Map.of("token",token,
					       "user",getPublicProfileDto(dto.getEmail()));
			
		}catch(Exception ex) {
			throw new RuntimeException("Invalid email or password");
		}
	}
	
	
}
