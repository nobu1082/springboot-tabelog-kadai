package com.example.nobukuni2023.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.nobukuni2023.entity.Review;
import com.example.nobukuni2023.form.ReviewEditForm;
import com.example.nobukuni2023.form.ReviewRegisterForm;
import com.example.nobukuni2023.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
   	 this.reviewRepository = reviewRepository;
   }
    @Transactional
    public void update(ReviewEditForm reviewEditForm) {
   	Review  review = reviewRepository.getReferenceById(reviewEditForm.getId()); 	 
    
    	review.setId(reviewEditForm.getId());
    	review.setStoreid(reviewEditForm.getStoreid());
    	review.setUserid(reviewEditForm.getUserid());
       review.setCommenttext(reviewEditForm.getCommenttext());
       review.setValue(reviewEditForm.getValue());
       
      
       reviewRepository.save(review);
 }
    @Transactional
    public void create(ReviewRegisterForm reviewRegisterForm ) {
   	 
   	 Review review = new Review();
   	
       review.setCommenttext(reviewRegisterForm.getCommenttext());
    	review.setValue(reviewRegisterForm.getValue());
        
        reviewRepository.save(review);
    }  
    @Transactional
	public List<Review> getAllReviews(){
		return reviewRepository.findAll();
	}
}
