package com.example.nobukuni2023.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import com.example.nobukuni2023.entity.Category;
import com.example.nobukuni2023.entity.Review;
import com.example.nobukuni2023.entity.Role;
import com.example.nobukuni2023.entity.Store;
import com.example.nobukuni2023.entity.User;
import com.example.nobukuni2023.form.ReservationInputForm;
import com.example.nobukuni2023.form.ReviewRegisterForm;
import com.example.nobukuni2023.repository.ReviewRepository;
import com.example.nobukuni2023.repository.StoreRepository;
import com.example.nobukuni2023.repository.UserRepository;
import com.example.nobukuni2023.repository._CategoryRepository;
import com.example.nobukuni2023.security.UserDetailsImpl;
import com.example.nobukuni2023.service.ReviewService;
@Controller
@RequestMapping("/stores")
public class StoreController {
	private final StoreRepository storeRepository;
	private final _CategoryRepository categoryRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewService reviewService;
	private final UserRepository userRepository;
    
    public StoreController(StoreRepository storeRepository ,_CategoryRepository categoryRepository ,
    		ReviewRepository reviewRepository,ReviewService reviewService,UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.categoryRepository = categoryRepository;
        this.reviewRepository =reviewRepository;
        this.reviewService = reviewService;
        this.userRepository = userRepository;
    }     
  
    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword,
                        @RequestParam(name = "amount", required = false) Integer amount,                        
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model) 
    {
        Page<Store> storePage;
                
        if (keyword != null && !keyword.isEmpty()) {
            storePage = storeRepository.findByNameLikeOrAddressLike("%" + keyword + "%", "%" + keyword + "%", pageable);
        } else if (amount != null) {
            storePage = storeRepository.findByAmountLessThanEqual(amount, pageable);
        } else {
            storePage = storeRepository.findAll(pageable);
        }                
        
        model.addAttribute("storePage", storePage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("price", amount);                              
        
        return "stores/index" ;
    }
    
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id ,@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,Model model) {
    	Store store = storeRepository.getReferenceById(id);
    	Category category = categoryRepository.getReferenceById(id);
    	User  user = userRepository.getReferenceById(id);
    	Role user2 = userDetailsImpl.getUser().getRole();
    	
    	List<Review> review = reviewRepository.getReferenceByStoreid(store);
    	
    	
    	model.addAttribute("store", store);
    	model.addAttribute("category",category);
    	model.addAttribute("reservationInputForm", new ReservationInputForm());
    	model.addAttribute("review",review);
    	model.addAttribute("user2" , user2);
    	
    	return "stores/show";
    }  
    //レビュー一覧
    @GetMapping("/{id}/reviews")
    public String showall(@PathVariable(name = "id") Integer id, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,Model model) {
   	 Store store2 = storeRepository.getReferenceById(id);
   	List<Review> reviews2 = reviewRepository.findByStoreid(store2);
   	User user2 = userDetailsImpl.getUser();
  	 model.addAttribute("user" , user2);
        model.addAttribute("reviews2", reviews2);
        model.addAttribute("store2",store2);
        return "stores/reviews/show";
    }
    
   
    @GetMapping("/stores/{id}/reviews/register")
 	public String register(@PathVariable(name = "id") Integer id, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,Model model) {
 		model.addAttribute("reviewRegisterForm",new ReviewRegisterForm());
 		return "/stores/reviews/register";
 	}
     
     @PostMapping("/stores/{id}/reviews/create")
     public String create(@PathVariable(name = "id") Integer id, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
         if (bindingResult.hasErrors()) {
             return "/stores/index";
         }
         
         // ユーザーIDの設定
        // reviewRegisterForm.setUserid(userDetailsImpl.getId());
         
         // ハウスIDの設定
         reviewRegisterForm.setHouseid(id);
         
         reviewService.create(reviewRegisterForm);
         
         redirectAttributes.addFlashAttribute("successMessage", "レビューを登録しました。");
         return "redirect:/houses";
     }
}
