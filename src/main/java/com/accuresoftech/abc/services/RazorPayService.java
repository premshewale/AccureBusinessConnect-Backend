package com.accuresoftech.abc.services;

import com.razorpay.RazorpayException;

public interface RazorPayService {
	public String createOrder(int amount , String currency, String receiptId) throws RazorpayException;

}
