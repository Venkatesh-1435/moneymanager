package com.money.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.money.manager.entity.ProfileEntity;
import java.util.Optional;



@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
	Optional<ProfileEntity> findByEmail(String email);
	
	Optional<ProfileEntity> findByActivationToken(String activationToken);
}
