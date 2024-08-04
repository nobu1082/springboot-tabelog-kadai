package com.example.nobukuni2023.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nobukuni2023.entity.Reservations;
import com.example.nobukuni2023.entity.Store;
import com.example.nobukuni2023.entity.User;
import com.example.nobukuni2023.form.ReservationInputForm;
import com.example.nobukuni2023.form.ReservationRegisterForm;
import com.example.nobukuni2023.repository.ReservationRepository;
import com.example.nobukuni2023.repository.StoreRepository;
import com.example.nobukuni2023.repository._CategoryRepository;
import com.example.nobukuni2023.security.UserDetailsImpl;
import com.example.nobukuni2023.service.ReservationService;



@Controller
public class ReservatinoController {
	private final ReservationRepository reservationRepository ;
	private final _CategoryRepository categoryRepository;
	private final StoreRepository storeRepository;
	private final ReservationService reservationService;
	
	public ReservatinoController(ReservationRepository reservationRepository,_CategoryRepository categoryRepository ,StoreRepository storeRepository , ReservationService reservationService) {
		this.reservationRepository = reservationRepository;
		this.categoryRepository = categoryRepository;
		this.storeRepository = storeRepository;
		this.reservationService = reservationService;
	}
	
	@GetMapping("/reservations")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, 
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
			User user = userDetailsImpl.getUser();
	
			
			Page<Reservations> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user,pageable);
			
			model.addAttribute("reservationPage",reservationPage);
			
			
			return "reservations/index";
	}
	
	@GetMapping("/stores/{id}/reservations/input")
	public String input(@PathVariable(name = "id") Integer id,@ModelAttribute @Validated ReservationInputForm reservationInputForm,
				BindingResult bindingResult,RedirectAttributes redirectAttributes, Model model)
	{
		Store store = storeRepository.getReferenceById(id);
		Integer numberOfPeople = reservationInputForm.getNumberOfPeople();
         
         redirectAttributes.addFlashAttribute("reservationInputForm", reservationInputForm);           
         
         return "redirect:/stores/{id}/reservations/confirm";
     }    
	
	@GetMapping("stores/{id}/reservations/confirm")
	public String confirm(@PathVariable(name ="id") Integer id,
						@ModelAttribute ReservationInputForm reservationInputForm,
						@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						Model model)
	{
		Store store = storeRepository.getReferenceById(id);
		User user = userDetailsImpl.getUser();
				
		String reservationDate = reservationInputForm.getReservationDate();
		
		ReservationRegisterForm reservationRegisterForm = new ReservationRegisterForm(store.getId(), user.getId(), 
				reservationInputForm.getReservationDate(),reservationInputForm.getNumberOfPeople());
	
		model.addAttribute("store",store);
		model.addAttribute("reservationRegisterForm",reservationRegisterForm);
		
		return "reservations/confirm";
	}
	
	@PostMapping("/stores/{id}/reservations/create")
	public String create(@ModelAttribute ReservationRegisterForm reservationRegisterForm) {
		reservationService.create(reservationRegisterForm);
		return "redirect:/reservations?reserved";
	}
	
	@PostMapping("/reservations/{id}/delete")
	 public String delete(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {        
       	reservationRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "予約を削除しました。");
        
        return "redirect:/";
    } 
	
}
