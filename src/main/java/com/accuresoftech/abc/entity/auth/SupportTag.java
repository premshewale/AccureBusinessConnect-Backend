package com.accuresoftech.abc.entity.auth;

import jakarta.persistence.*;

@Entity
@Table(name = "support_tags")
public class SupportTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

	public SupportTag() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SupportTag(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    
}

