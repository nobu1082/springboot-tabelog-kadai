package com.example.nobukuni2023.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreRegisterForm {

	@NotNull(message = "和食は1 中華は2 洋食は3を入力して下さい")
	private Integer categoryId;

	@NotBlank(message = "お店の名前を入力して下さい。")
	private String name;

	private MultipartFile imageFile;

	@NotBlank(message = "お店の紹介をして下さい。")
	private String description;

	@NotBlank(message = "営業開始時間を〇〇時〇〇分で記載して下さい。")
	private String openTime;

	@NotBlank(message = "営業終了時間を〇〇時〇〇分で入力して下さい。")
	private String closeTime;

	@NotNull(message = "お会計の目安を入力して下さい。1,000円単位でお願いします。")
	private Integer amount;

	@NotBlank(message = "郵便番号を入力して下さい。")
	private String postalCode;

	@NotBlank(message = "お店の住所を入力して下さい。")
	private String address;

	@NotBlank(message = "お店の電話番号を入力して下さい。")
	private String phoneNumber;

	@NotBlank(message = "お店のお休みの曜日を入力して下さい。")
	private String closedDays;

}
