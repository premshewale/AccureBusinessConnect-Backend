package com.accuresoftech.abc.dto.request;

import java.math.BigDecimal;

public class EstimateItemRequest {
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal taxRate;
	public EstimateItemRequest(String description, Integer quantity, BigDecimal unitPrice, BigDecimal taxRate) {
		super();
		this.description = description;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.taxRate = taxRate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
    
    
    
}
