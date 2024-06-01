package com.example.nobukuni2023.controller;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nobukuni2023.entity.Category;
import com.example.nobukuni2023.form.CategoryRegisterForm;
import com.example.nobukuni2023.repository.CategoryRepository;
import com.example.nobukuni2023.service.CategoryService;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController {
	private final CategoryRepository categoryRepository;
	private final CategoryService categoryService;

	public AdminCategoryController(CategoryRepository categoryRepository, CategoryService categoryService) {
		this.categoryRepository = categoryRepository;
		this.categoryService = categoryService;
	}

	@GetMapping
	public String index(Model model) {
		
		List<Category> category = categoryRepository.findAll();
		

	model.addAttribute("category", category);

		return "admin/category/index";
	}

	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("categoryRegisterForm" , new CategoryRegisterForm());
		return "admin/category/register";
	}
	@PostMapping("/register")
	public String create(@ModelAttribute @Validated CategoryRegisterForm categoryRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) { 
		if(bindingResult.hasErrors()) {
		}
		categoryService.create(categoryRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage","店舗を登録しました。");
		return "redirect:/admin/category";
		}
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable(name ="id")Integer id,RedirectAttributes redirectAttributes) { 
		//categoryRepository.deleteById(id);
		redirectAttributes.addFlashAttribute("successMessage" , "カテゴリを削除しました。");
		
		return "redirect:/admin/category";
	}
}
