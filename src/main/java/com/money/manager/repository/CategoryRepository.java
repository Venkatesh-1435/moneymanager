package com.money.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.money.manager.entity.CategoryEntity;
import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>{
	
	//select * from tbl_category where profile=?1
	List<CategoryEntity> findByProfileId(Long profileId);
	
	//select * from tbl_category where id=?1 and profile=?2
    Optional<CategoryEntity>findByIdAndProfileId(Long id,Long profileId);
	
    List<CategoryEntity> findByTypeAndProfileId(String type,Long profileId);
    
    
    
    
    boolean existsByNameAndProfileId(String name,Long profileId);
    
}
