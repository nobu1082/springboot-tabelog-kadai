package com.example.nobukuni2023.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nobukuni2023.entity.Favorite;
import com.example.nobukuni2023.entity.Store;
import com.example.nobukuni2023.entity.User;
import com.example.nobukuni2023.repository.FavoriteRepository;
import com.example.nobukuni2023.repository.StoreRepository;
import com.example.nobukuni2023.repository.UserRepository;
import com.example.nobukuni2023.security.UserDetailsImpl;
import com.example.nobukuni2023.service.FavoriteService;

@Controller
public class FavoriteController {
	private final FavoriteRepository favoriteRepository;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final FavoriteService favoriteService;
	
	public FavoriteController(FavoriteRepository favoriteRepository,UserRepository userRepository,
			StoreRepository storeRepository,FavoriteService favoriteService) {
		this.favoriteRepository = favoriteRepository;
		this.userRepository = userRepository;
		this.storeRepository = storeRepository;
		this.favoriteService = favoriteService;
	}
	
	@GetMapping("/favorite")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@PageableDefault(page = 0 ,size = 10 ,sort = "id",direction = Direction.ASC)Pageable pageable, Model model) {
		
		User user = userDetailsImpl.getUser();
		Integer UserId = user.getId();
		Page<Favorite> favoritePage = favoriteRepository.findByUserId(UserId, pageable);
		model.addAttribute("favoritePage" ,favoritePage);
 		
		return "favorite/index";
	}
	@PostMapping("favorite/add")
	public String addFavorite(@RequestParam("storeId") Integer storeId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,RedirectAttributes redirectAttributes) {
		User user = userDetailsImpl.getUser();
		java.util.Optional<Store> storeOptional = storeRepository.findById(storeId);
		boolean isSuccess = storeOptional.map(store-> favoriteService.subscribe(store, user)).orElse(false);
		if (!isSuccess) {
			redirectAttributes.addFlashAttribute("errorMessage","すでにお気に入りに登録されています。");
		}
		
		return "redirect:/favorite";
	}
	@PostMapping("/favorite/remove")
	public String removeFavorite(@RequestParam("storeId") Integer storeId, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
	    User user = userDetailsImpl.getUser();
	    java.util.Optional<Store> storeOptional = storeRepository.findById(storeId);
	    storeOptional.ifPresent(store -> favoriteService.unsubscribe(store, user));
	    
	    return "redirect:/favorite";
	}
	
}
