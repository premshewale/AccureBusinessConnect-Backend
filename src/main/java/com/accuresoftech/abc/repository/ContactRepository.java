package com.accuresoftech.abc.repository;

import com.accuresoftech.abc.entity.auth.Contact;
import com.accuresoftech.abc.entity.auth.Customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
	
	List<Contact> findByCustomer(Customer customer);
}