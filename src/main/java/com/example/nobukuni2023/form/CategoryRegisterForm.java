package com.example.nobukuni2023.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRegisterForm {

	@NotNull(message = "カテゴリーの内容を入力して下さい。")
	private String name;
}
