package com.example.nobukuni2023.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nobukuni2023.entity.Reservations;
import com.example.nobukuni2023.entity.User;
import com.example.nobukuni2023.form.ReservationRegisterForm;

public interface ReservationRepository extends JpaRepository<Reservations ,Long>{
	public Page<Reservations> findByUserOrderByCreatedAtDesc(User user , Pageable pageable);

	public Reservations save(ReservationRegisterForm reservationRegisterForm);

	public Optional<Reservations> findById(Integer reservationId);

	
}
