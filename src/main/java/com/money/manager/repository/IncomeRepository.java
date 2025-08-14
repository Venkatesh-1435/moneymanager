package com.money.manager.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.money.manager.entity.IncomeEntity;

public interface IncomeRepository extends JpaRepository<IncomeEntity, Long> {
	
	List<IncomeEntity>	findByProfileIdOrderByDateDesc(Long profileId); 
	   
    List<IncomeEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);
    
    @Query(value = "select sum(amount) from tbl_incomes where profile_id= ?1 ",nativeQuery = true )
    BigDecimal totalincomes(Long profileId);
    
    @Query("SELECT SUM(e.amount) FROM IncomeEntity e WHERE e.profile.id= :profileId ")
    BigDecimal findTotalByIncomesByProfileId(@Param("profileId") Long profileId);
    
    List<IncomeEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(Long profileId,
    		LocalDate startDate,
    		LocalDate endDate,
    		String keyword,
    		Sort sort);
    
    List<IncomeEntity> findByProfileIdAndDateBetween(Long profileId,LocalDate startDate,LocalDate endDate);

}
