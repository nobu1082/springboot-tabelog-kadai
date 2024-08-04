

package com.example.nobukuni2023.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nobukuni2023.entity.Category;


public interface _CategoryRepository extends JpaRepository<Category , Integer>{

	public Page<Category> findByNameLike(String keyward , Pageable pagealbe);

	
	
}