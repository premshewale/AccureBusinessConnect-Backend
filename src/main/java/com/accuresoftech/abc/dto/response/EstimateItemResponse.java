package com.accuresoftech.abc.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class EstimateItemResponse {
    private Long id;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal taxRate;
    private BigDecimal lineTotal;
	public EstimateItemResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public EstimateItemResponse(Long id, String description, Integer quantity, BigDecimal unitPrice, BigDecimal taxRate,
			BigDecimal lineTotal) {
		super();
		this.id = id;
		this.description = description;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.taxRate = taxRate;
		this.lineTotal = lineTotal;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public BigDecimal getLineTotal() {
		return lineTotal;
	}
	public void setLineTotal(BigDecimal lineTotal) {
		this.lineTotal = lineTotal;
	}
    
    
}

