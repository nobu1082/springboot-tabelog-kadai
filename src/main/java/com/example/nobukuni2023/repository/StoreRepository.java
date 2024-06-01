package com.example.nobukuni2023.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nobukuni2023.entity.Category;
import com.example.nobukuni2023.entity.Store;

public interface StoreRepository extends JpaRepository<Store , Integer>{

	public Page<Store> findByNameLike(String keyward , Pageable pagealbe);
	public Page<Store> findByNameLikeOrAddressLike(String nameKeyword, String addressKeyword, Pageable pageable);    
    public Page<Store> findByAmountLessThanEqual(Integer amount, Pageable pageable);  
	
    public List<Store> findTop10ByOrderByCreatedAtDesc();
	public List<Store> findByCategoryId(Category category);
	Page<Store> findByCategoryId(Integer categoryId, Pageable pageable);

}
