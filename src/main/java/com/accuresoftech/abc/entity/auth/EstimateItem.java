package com.accuresoftech.abc.entity.auth;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "estimate_items")
public class EstimateItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // parent estimate
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimate_id", nullable = false)
    private Estimate estimate;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(name = "unit_price", precision = 12, scale = 2)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name = "tax_rate", precision = 6, scale = 2)
    private BigDecimal taxRate = BigDecimal.ZERO; // percent (e.g., 18.00)

    @Column(name = "line_total", precision = 12, scale = 2)
    private BigDecimal lineTotal = BigDecimal.ZERO;

    public EstimateItem() {}

	public EstimateItem(Long id, Estimate estimate, String description, Integer quantity, BigDecimal unitPrice,
			BigDecimal taxRate, BigDecimal lineTotal) {
		super();
		this.id = id;
		this.estimate = estimate;
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

	public Estimate getEstimate() {
		return estimate;
	}

	public void setEstimate(Estimate estimate) {
		this.estimate = estimate;
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
