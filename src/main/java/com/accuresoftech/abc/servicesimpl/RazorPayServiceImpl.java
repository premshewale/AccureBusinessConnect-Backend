package com.accuresoftech.abc.servicesimpl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

public class RazorPayServiceImpl {
	
	@Value("${razorpay.key_id}")
	private String apiKey;
	
	@Value("${razorpay.key_secret}")
	private String apiSercret;

	public String createOrder(int amount , String currency, String receiptId) throws RazorpayException  {
		RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSercret);
		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount",amount * 100);
		orderRequest.put("currency", currency);
		orderRequest.put("receipt", receiptId);
		
		Order order = razorpayClient.orders.create(orderRequest);
		return order.toString();
		
	}

}
