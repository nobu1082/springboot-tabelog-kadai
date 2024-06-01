package com.example.nobukuni2023.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nobukuni2023.entity.Store;
import com.example.nobukuni2023.repository._CategoryRepository;
import com.example.nobukuni2023.repository.StoreRepository;

@Controller
public class HomeController {
	private final StoreRepository storeRepository;
	private final _CategoryRepository categoryRepository;

	public HomeController(StoreRepository storeRepository, _CategoryRepository categoryRepository) {
		this.storeRepository = storeRepository;
		this.categoryRepository = categoryRepository;
	}

	
		@GetMapping("stores/category/{id}")
	public String category(@PathVariable(name = "id") Integer id,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "amount", required = false) Integer amount,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model) {
		Page<Store> storePage = storeRepository.findByCategoryId(id, pageable);
	    model.addAttribute("storePage", storePage);
	    
		
		return "stores/category/index";

	}

}
