package com.accuresoftech.abc.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
	
	private String orderId;
    private String currency;
    private BigDecimal amount;
	public void setAmount(long amountInPaise) {
		// TODO Auto-generated method stub
		
	}

}
