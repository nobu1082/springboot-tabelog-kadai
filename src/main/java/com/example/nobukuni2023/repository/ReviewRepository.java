package com.example.nobukuni2023.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nobukuni2023.entity.Review;
import com.example.nobukuni2023.entity.Store;
import com.example.nobukuni2023.entity.User;




public interface ReviewRepository extends JpaRepository<Review, Integer> {
	
	public List<Review> findTop6ByStoreidOrderByUpdatedAtDesc(Store storeid);
    
	public List<Review> findByStoreid(Store storeid);

	public List<Review> findAllById(Store store);

	public List<Review> getReferenceByStoreid(Store storeid);

	public List<Review> findByUserid(User user);
	
}
