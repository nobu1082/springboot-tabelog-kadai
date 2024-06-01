package com.example.nobukuni2023.controller;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nobukuni2023.entity.Role;
import com.example.nobukuni2023.entity.User;
import com.example.nobukuni2023.form.UserEditForm;
import com.example.nobukuni2023.repository.RoleRepository;
import com.example.nobukuni2023.repository.UserRepository;
import com.example.nobukuni2023.security.UserDetailsImpl;
import com.example.nobukuni2023.service.SubscriptionService;
import com.example.nobukuni2023.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.SubscriptionUpdateParams;
import com.stripe.param.checkout.SessionCreateParams;

@Controller
@RequestMapping("/user")
public class UserController {
	private final UserRepository userRepository;
	private final UserService userService;
	private Map<String, Object> params;
	private final SubscriptionService stripeService;
	public String apiKey;
	public String priceId;
	private final RoleRepository roleRepository;

	public UserController(UserRepository userRepository, UserService userService, SubscriptionService stripeService,RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.userService = userService;
		this.stripeService = stripeService;
		this.roleRepository = roleRepository;
	}

	@GetMapping
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());

		model.addAttribute("user", user);

		return "user/index";
	}

	@GetMapping("/edit")
	public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
		UserEditForm userEditForm = new UserEditForm(user.getId(), user.getName(), user.getEmail());

		model.addAttribute("userEditForm", userEditForm);

		return "user/edit";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		// メールアドレスが変更されており、かつ登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
		if (userService.isEmailChanged(userEditForm) && userService.isEmailRegistered(userEditForm.getEmail())) {
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
			bindingResult.addError(fieldError);
		}

		if (bindingResult.hasErrors()) {
			return "user/edit";
		}

		userService.update(userEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");

		return "redirect:/user";
	}

	@GetMapping("/subsc")
	public String subsc() {

		return "user/subsc";
	}

	@PostMapping("/create-checkout-session")
	public String subsbcheckout(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws StripeException {

		Stripe.apiKey = "sk_test_51OLEXaJsSQOmZTMCrcb62esT4RG6bjjugAXnoxNd6E7gCTCJPdCGPRK1KgYHvgoro0A3ckUdXMCOoMeZmp6uwDeX00agPzG7vY";
		String priceId = "price_1PCcVZJsSQOmZTMC9yEmSw84";

		SessionCreateParams params = new SessionCreateParams.Builder()
				.putMetadata("userId", String.valueOf(userDetailsImpl.getUser().getId()))
				.setSuccessUrl("http://localhost:8080/")
				//.setSuccessUrl("https://example.com/success.html?session_id={CHECKOUT_SESSION_ID}")
				.setCancelUrl("https://example.com/canceled.html")
				.setMode(SessionCreateParams.Mode.SUBSCRIPTION)
				.addLineItem(new SessionCreateParams.LineItem.Builder()
						// For metered billing, do not pass quantity
						.setQuantity(1L)
						.setPrice(priceId)
						.build())
				.build();

		Session session = Session.create(params);

		return "redirect:" + session.getUrl();
	}

	@GetMapping("subsccancel")
	public String subsccancel(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws StripeException {
		Stripe.apiKey = "sk_test_51OLEXaJsSQOmZTMCrcb62esT4RG6bjjugAXnoxNd6E7gCTCJPdCGPRK1KgYHvgoro0A3ckUdXMCOoMeZmp6uwDeX00agPzG7vY";

		User user =userDetailsImpl.getUser();
		Subscription resource = Subscription.retrieve(user.getSubscription());
		//Subscription resource = Subscription.retrieve("sub_1PFXnVJsSQOmZTMCUYx36diw");
		SubscriptionUpdateParams params = SubscriptionUpdateParams.builder().setCancelAtPeriodEnd(true).build();

		Subscription subscription = resource.update(params);
		Role role = roleRepository.findByName("ROLE_GENERAL");
        user.setRole(role);
        userRepository.save(user);
		return "redirect:/";
	}
}


