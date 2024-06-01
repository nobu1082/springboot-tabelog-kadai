package com.example.nobukuni2023.form;



import com.example.nobukuni2023.entity.Store;
import com.example.nobukuni2023.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewEditForm {

	
	 Integer id;
	
	
	 Store storeid;
	
	@NotNull
	  User userid;
	
	
	@NotBlank
	 String commenttext;
	
	
	Integer value;
}