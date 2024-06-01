package com.example.nobukuni2023.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.nobukuni2023.entity.Store;
import com.example.nobukuni2023.form.StoreEditForm;
import com.example.nobukuni2023.form.StoreRegisterForm;
import com.example.nobukuni2023.repository.StoreRepository;

@Service
public class StoreService {
	private final StoreRepository storeRepository;

	public StoreService(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}

	@Transactional
	public void create(StoreRegisterForm storeRegisterForm) {
		Store store = new Store();
		MultipartFile imageFile = storeRegisterForm.getImageFile();

		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			store.setImageName(hashedImageName);
		}

		store.setCategoryId(storeRegisterForm.getCategoryId());
		store.setName(storeRegisterForm.getName());
		store.setDescription(storeRegisterForm.getDescription());
		store.setOpenTime(storeRegisterForm.getOpenTime());
		store.setCloseTime(storeRegisterForm.getCloseTime());
		store.setAmount(storeRegisterForm.getAmount());
		store.setOpenTime(storeRegisterForm.getOpenTime());
		store.setCloseTime(storeRegisterForm.getCloseTime());
		store.setPostalCode(storeRegisterForm.getPostalCode());
		store.setAddress(storeRegisterForm.getAddress());
		store.setPhoneNumber(storeRegisterForm.getPhoneNumber());
		store.setClosedDays(storeRegisterForm.getClosedDays());

		storeRepository.save(store);
	}

	@Transactional
	public void update(StoreEditForm storeEditForm) {
		Store store = storeRepository.getReferenceById(storeEditForm.getId());
		MultipartFile imageFile = storeEditForm.getImageFile();
		
		if(!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			store.setImageName(hashedImageName);
		}
		
		store.setCategoryId(storeEditForm.getCategoryId());
		store.setName(storeEditForm.getName());
		store.setDescription(storeEditForm.getDescription());
		store.setOpenTime(storeEditForm.getOpenTime());
		store.setCloseTime(storeEditForm.getCloseTime());
		store.setAmount(storeEditForm.getAmount());
		store.setPostalCode(storeEditForm.getPostalCode());
		store.setAddress(storeEditForm.getAddress());
		store.setPhoneNumber(storeEditForm.getPhoneNumber());
		store.setClosedDays(storeEditForm.getClosedDays());
		
		storeRepository.save(store);
	}
	
	public String

			generateNewFileName(String fileName) {
		String[] fileNames = fileName.split("\\.");
		for (int i = 0; i < fileNames.length - 1; i++) {
			fileNames[i] = UUID.randomUUID().toString();
		}
		String hashedFileName = String.join(".", fileNames);
		return hashedFileName;
	}

	public void copyImageFile(MultipartFile imageFile, Path filePath) {
		try {
			Files.copy(imageFile.getInputStream(), filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
