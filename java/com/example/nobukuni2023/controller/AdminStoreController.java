package com.example.nobukuni2023.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nobukuni2023.entity.Store;
import com.example.nobukuni2023.form.StoreEditForm;
import com.example.nobukuni2023.form.StoreRegisterForm;
import com.example.nobukuni2023.repository.CategoryRepository;
import com.example.nobukuni2023.repository.FavoriteRepository;
import com.example.nobukuni2023.repository.ReviewRepository;
import com.example.nobukuni2023.repository.StoreRepository;
import com.example.nobukuni2023.service.StoreService;

@Controller
@RequestMapping("/admin/stores")
public class AdminStoreController {

	private final StoreRepository storeRepository;
	private final StoreService storeService;
	private final ReviewRepository reviewRepository;
	private final FavoriteRepository favoriteRepository;
	private CategoryRepository categoryRepository;
	public AdminStoreController(StoreRepository storeRepository, StoreService storeService,ReviewRepository reviewRepository,FavoriteRepository favoriteRepository,
			CategoryRepository categoryRepository) {
		this.storeRepository = storeRepository;
		this.storeService = storeService;
		this.reviewRepository = reviewRepository;
		this.favoriteRepository = favoriteRepository;
		this.categoryRepository = categoryRepository;
	}

	@GetMapping
	public String index(Model model,
			@PageableDefault(page = 0, size = 5, sort = "id", direction = Direction.ASC) Pageable pageable,
			@RequestParam(name = "keyword", required = false) String keyword) {
		Page<Store> storePage;

		if (keyword != null && !keyword.isEmpty()) {
			storePage = storeRepository.findByNameLike("%" + keyword + "%", pageable);
		} else {
			storePage = storeRepository.findAll(pageable);
		}

		model.addAttribute("storePage", storePage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("categories",categoryRepository.findAll());
		return "stores/index";
		//return "admin/stores/index";
	}
	
	@PostMapping("/{id}/delete")
 	public String delete(@PathVariable(name ="id")Integer id,Store storeId,Store store,RedirectAttributes redirectAttributes) {
	//Review review = reviewRepository.findByStoreId(storeId);
	List reviews = reviewRepository.findByStoreId(storeId);
	List favorite = favoriteRepository.findByStore(store);
	if ((reviews == null || reviews.size() == 0) && favorite == null) {
			storeRepository.deleteById(id);
			redirectAttributes.addFlashAttribute("successMessage", "削除しました");
			return "index"; 
			}
		if (reviews != null || favorite != null) {
			redirectAttributes.addFlashAttribute("successMessage", "レビュー登録もしくはお気に入り登録されているので削除できません");
			return "redirect:/";
		}
		redirectAttributes.addFlashAttribute("successMessage", "レビュー登録もしくはお気に入り登録されているので削除できません");
		return "redirect:/";
			
 }

	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id, Model model) {
		Store store = storeRepository.getReferenceById(id);

		model.addAttribute("store", store);
		return "admin/stores/show";
	}	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("categories",categoryRepository.findAll());
		model.addAttribute("storeRegisterForm", new StoreRegisterForm());
		return "admin/stores/register";
	}

	@PostMapping("/create")
	public String create(@ModelAttribute @Validated StoreRegisterForm storeRegisterForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes,Model model) {
		if (bindingResult.hasErrors()) {
			return "admin/stores/register";
		}
		storeService.create(storeRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "店舗を登録しました。");
		return "redirect:/stores";
	}

	@GetMapping("/{id}/edit")
	public String edit(@PathVariable(name ="id") Integer id,Model model) {
		Store store = storeRepository.getReferenceById(id);
		String imageName = store.getImageName();
		StoreEditForm storeEditForm = 
				new StoreEditForm(store.getId(),store.getCategoryId(),store.getName(),null,store.getDescription(),store.getOpenTime(),store.getCloseTime(),store.getAmount(),
						store.getPostalCode(),store.getAddress(),store.getPhoneNumber(),store.getClosedDays());
		model.addAttribute("imageName",imageName);
		model.addAttribute("storeEditForm",storeEditForm);
		model.addAttribute("categories",categoryRepository.findAll());
		return "admin/stores/edit";
	}
	
	@PostMapping("/{id}/update")
	public String update(@ModelAttribute @Validated StoreEditForm storeEditForm,BindingResult bindingResult ,RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			return "admin/stores/edit";
		}
			storeService.update(storeEditForm);
			redirectAttributes.addFlashAttribute("successMessage","店舗情報を編集しました。");
			return "redirect:/stores";
		}

}
