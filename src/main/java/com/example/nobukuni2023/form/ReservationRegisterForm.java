package com.example.nobukuni2023.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationRegisterForm {
	private Integer storeid;
	
	private Integer userid;
	
    private String reservationDate;
    
    private Integer numberOfPeople; 
}
