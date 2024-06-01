package com.example.nobukuni2023.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nobukuni2023.entity.Reservations;
import com.example.nobukuni2023.entity.Store;
import com.example.nobukuni2023.entity.User;
import com.example.nobukuni2023.form.ReservationRegisterForm;
import com.example.nobukuni2023.repository.ReservationRepository;
import com.example.nobukuni2023.repository.StoreRepository;
import com.example.nobukuni2023.repository.UserRepository;

@Service
public class ReservationService {
	 private final ReservationRepository reservationRepository;
	 private final StoreRepository storeRepository;
	 private final UserRepository userRepository;
	    
	    @Autowired
	    public ReservationService(ReservationRepository reservationRepository,StoreRepository storeRepository
	    		,UserRepository userRepository) {
	        this.reservationRepository = reservationRepository;
	        this.storeRepository = storeRepository;
	        this.userRepository = userRepository;
			
	    }
	    
	    @Transactional
	    public Reservations create(ReservationRegisterForm reservationRegisterForm) {
	    	
	    	if(reservationRegisterForm.getStoreid() != null && reservationRegisterForm.getUserid() != null) {
	    	Reservations reservations = new Reservations();
	    	Store store = storeRepository.getReferenceById(reservationRegisterForm.getStoreid());
	    	User user = userRepository.getReferenceById(reservationRegisterForm.getUserid());
	    	LocalDate reservationDate = LocalDate.parse(reservationRegisterForm.getReservationDate());
	    	
	    	reservations.setStore(store);
	    	reservations.setUser(user);
	    	reservations.setReservationDate(reservationDate);
	    	reservations.setNumberOfPeople(reservationRegisterForm.getNumberOfPeople());
	    	
	        return reservationRepository.save(reservations);
	    } else {
	    	throw new IllegalArgumentException("Store ID and User ID must not be null");
	    }
	    }
	    
	    
}