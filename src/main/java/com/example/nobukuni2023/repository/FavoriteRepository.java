package com.example.nobukuni2023.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nobukuni2023.entity.Favorite;
import com.example.nobukuni2023.entity.Store;
import com.example.nobukuni2023.entity.User;

public interface FavoriteRepository  extends JpaRepository<Favorite, Integer >{
	public Page<Favorite> findByUserId(Integer userId ,Pageable pageable);

	public Page<Favorite> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

	public Favorite findByStoreAndUser(Store store, User user);




}
