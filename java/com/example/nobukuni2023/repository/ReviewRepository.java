
package com.example.nobukuni2023.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nobukuni2023.entity.Review;
import com.example.nobukuni2023.entity.Store;
import com.example.nobukuni2023.entity.User;




public interface ReviewRepository extends JpaRepository<Review, Integer> {
	
	public List<Review> findTop6ByStoreIdOrderByUpdatedAtDesc(Store storeId);
    
	

	public List<Review> findAllById(Store store);

	public List<Review> getReferenceByStoreId(Store storeId);

	public List<Review> findByUserid(User user);
	
	public List<Review> findByStoreId(Store storeId);
	
}
