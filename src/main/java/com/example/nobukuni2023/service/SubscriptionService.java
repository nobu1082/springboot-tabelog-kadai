package com.example.nobukuni2023.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nobukuni2023.entity.User;
import com.example.nobukuni2023.repository.UserRepository;

@Service
public class SubscriptionService {
	 private final UserRepository userRepository;  
	
	    
	    public SubscriptionService(UserRepository userRepository) {
	        this.userRepository = userRepository;  
	    }    
	    
	    @Transactional
	    public void create(Map<String, String> paymentIntentObject) {
	        User user = new User();
	         
	        String Subscription = String.valueOf(paymentIntentObject.get("Subscription"));
	        user.setSubscription(Subscription);
	        userRepository.save(user);
	        }
	    }
