package com.accuresoftech.abc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Configuration
public class RazorpayConfig {
	
	 @Value("${razorpay.key_id}")
	    private String key;

	    @Value("${razorpay.key_secret}")
	    private String secret;

	    @Bean
	    public RazorpayClient razorpayClient() throws RazorpayException {
	        return new RazorpayClient(key, secret);
	    }

}
