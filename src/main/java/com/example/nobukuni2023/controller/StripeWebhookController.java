package com.example.nobukuni2023.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.nobukuni2023.entity.Role;
import com.example.nobukuni2023.entity.User;
import com.example.nobukuni2023.repository.RoleRepository;
import com.example.nobukuni2023.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

@Controller
public class StripeWebhookController<TradeOrder> {
	private final UserRepository userRepository;
	private Object request;
	private RoleRepository roleRepository;
	public StripeWebhookController(UserRepository userRepository,RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
	@PostMapping("/stripe/webhook")
	public ResponseEntity<String> webhook(@RequestBody String payload,
			@RequestHeader("Stripe-Signature") String sigHeader, Map<String, String> paymentIntentObject) 
	
	{
		Stripe.apiKey = "sk_test_51OLEXaJsSQOmZTMCrcb62esT4RG6bjjugAXnoxNd6E7gCTCJPdCGPRK1KgYHvgoro0A3ckUdXMCOoMeZmp6uwDeX00agPzG7vY";
		String endpointSecret = "whsec_4ca99ba1872d7c985bb82dc4fcb9978341bff67cd76d700697fe355272893e55";
		Event event = null;
		try {
			event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
		} catch (SignatureVerificationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
		StripeObject stripeObject = null;
		if (dataObjectDeserializer.getObject().isPresent()) {
			stripeObject = dataObjectDeserializer.getObject().get();
		}
		switch (event.getType()) {
		case "checkout.session.completed":
			Session session = (Session)stripeObject;
			String subId = session.getSubscription();
			String userId = session.getMetadata().get("userId");

		        if (userId != null) {
		            // ユーザーをデータベースから取得
		            Optional<User> userOptional = userRepository.findById(Integer.parseInt(userId));
		            if (userOptional.isPresent()) {
		                User user = userOptional.get();
		                // サブスクリプションIDを設定
		                Role role = roleRepository.findByName("ROLE_SUBSC");
		                user.setRole(role);
		                user.setSubscription(subId);
		                userRepository.save(user);
		            } else {
		                // ユーザーが見つからない場合の処理
		                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		            }
		        } else {
		            // userIdがメタデータに含まれていない場合の処理
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Metadata userId is missing");
		        }
				break;
		}

		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

}
