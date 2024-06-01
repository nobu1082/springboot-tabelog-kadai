package com.example.nobukuni2023.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nobukuni2023.entity.Category;
import com.example.nobukuni2023.form.CategoryRegisterForm;
import com.example.nobukuni2023.repository._CategoryRepository;
@Service
public class CategoryService {
	private final _CategoryRepository categoryRepository;
	
	public CategoryService(_CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	@Transactional
	public void create(CategoryRegisterForm categoryRegisterForm) {
		Category category = new Category();
			
		category.setName(categoryRegisterForm.getName());
		categoryRepository.save(category);
	}
}