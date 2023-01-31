package com.mb.api.web.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mb.api.web.model.CustomerModel;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Balance;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.EventCollection;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Token;
import com.stripe.param.PaymentIntentCreateParams;

@RestController
@RequestMapping("/api/v1")
public class StripePaymentController
{	
	@Value("${app.stripe_secret_key}")
	private String secretKey;
	
	
	
	//Create customer 
	@PostMapping("/customers")
	public CustomerModel createCustomer(@RequestBody CustomerModel customerModel) throws StripeException {
		
		Stripe.apiKey = secretKey;
		
		Map<String ,Object> params = new HashMap<>();
		params.put("name", customerModel.getName());
		params.put("email",customerModel.getEmail());
		
		//add card to customers
		Map<String, Object> cardParams = new HashMap<>();
		cardParams.put("name", customerModel.getName());
		cardParams.put("last4", "4242");
		cardParams.put("exp_month", 12);
		cardParams.put("exp_year", 2023);
		
		//create token params
		Map<String, Object> tokenParams = new HashMap<>();
		tokenParams.put("card", cardParams);
		
		//Create token for card parameter
		Token token = Token.create(tokenParams);
		
		//create source 
		Map<String, Object> source = new HashMap();
		source.put("source", token.getId());
		
		Customer customer = Customer.create(params);
		
		customer.getSources().create(source);
		
		customerModel.setCustomerId(customer.getId());
		return customerModel;
	}
	
	//Retrive Customer
		@GetMapping("/customers/{customerId}")
	public String retriveCustomer(@PathVariable(name = "customerId") String customerId ) throws StripeException {
		Stripe.apiKey = secretKey;
		return Customer.retrieve(customerId).toJson();
	}
	
	
	//Charge 
	//TODO : send model for data
	@PostMapping("/payment-intent")
	public String paymentIntent() throws StripeException {
	
		Stripe.apiKey = secretKey;
		
		PaymentIntentCreateParams params =
				  PaymentIntentCreateParams
				    .builder()
				    .setAmount(1099L)
				    .setCurrency("usd")
				    .addPaymentMethodType("card")
				    .build();

				PaymentIntent paymentIntent = PaymentIntent.create(params);

		return paymentIntent.toJson();
	}
	
	//Retrive all event whether succeed or failed
	@GetMapping("/events")
	public String getAllEvents() throws StripeException {
		Stripe.apiKey = secretKey;
		Map<String, Object> params = new HashMap<>();
		params.put("limit", 3);

		//EventCollection events = Event.list(params);
		//evt_1M6uelSIFJPCF6ONN1XpWasT
		
		Event event =
				  Event.retrieve("evt_1M6uelSIFJPCF6ONN1XpWasT");
		return event.toJson();
	}
	
	//retrive balance from stripe account

	@GetMapping("/balance")
	public String getBalance() throws StripeException {
		Stripe.apiKey = secretKey;
		Balance balance = Balance.retrieve();
		return balance.toJson();
		
	}
	
}
