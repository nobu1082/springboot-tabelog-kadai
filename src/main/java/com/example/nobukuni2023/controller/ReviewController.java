
package com.example.nobukuni2023.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nobukuni2023.entity.Review;
import com.example.nobukuni2023.entity.Store;
import com.example.nobukuni2023.entity.User;
import com.example.nobukuni2023.form.ReviewEditForm;
import com.example.nobukuni2023.repository.ReviewRepository;
import com.example.nobukuni2023.repository.StoreRepository;
import com.example.nobukuni2023.repository.UserRepository;
import com.example.nobukuni2023.security.UserDetailsImpl;
import com.example.nobukuni2023.service.ReviewService;


@Controller
public class ReviewController {
	private final ReviewRepository reviewRepository;
	private final ReviewService reviewService;
	private final StoreRepository storeRepository;
	private final UserRepository userRepository;


	@Autowired
	public ReviewController(ReviewRepository reviewRepository , ReviewService reviewService , StoreRepository storeRepository ,UserRepository userRepository) {
		this.reviewRepository = reviewRepository;
		this.reviewService = reviewService;
		this.storeRepository = storeRepository;
		this.userRepository = userRepository;
		
	}

	@GetMapping("/stores/reviews/{id}")
	public String edit(@PathVariable(name = "id") Integer id ,Store storeid,@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,Model model) {
		Review review = reviewRepository.getReferenceById(id);
		ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(),review.getStoreid(),review.getUserid(),review.getCommenttext(),review.getValue());
		model.addAttribute("reviewEditForm" , reviewEditForm);
		
		return "stores/reviews/edit";
	}	
		
		
	@PostMapping("/stores/reviews/{id}/update")
	public String update(@PathVariable(name = "id") Integer  id,@ModelAttribute @Validated ReviewEditForm reviewEditForm,BindingResult bindingResult,RedirectAttributes redirectAttributes,Model model){
		
		if(bindingResult.hasErrors()) {
			return "/stores/reviews/edit";
		}
		reviewService.update(reviewEditForm);
		
		redirectAttributes.addFlashAttribute("successMessage","編集しました");
			return "redirect:/";

 }
	 @PostMapping("/stores/{id}/reviews/delete")
	 	public String delete(@PathVariable(name ="id")Integer id, RedirectAttributes redirectAttributes) {
	 		reviewRepository.deleteById(id);
	 		redirectAttributes.addFlashAttribute("successMessage","削除しました");
	 		return "redirect:/";
	 }
	 
	 @GetMapping("/stores/reviews")
	  	public String reviews( @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,Model model) {
	    	 User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
	    	 List<Review> reviews = reviewRepository.findByUserid(user);
	    	
	  		model.addAttribute("reviews",reviews);
	  		 
	  		return "/stores/reviews/show";
	  	} 
}







