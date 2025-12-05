package com.accuresoftech.abc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.accuresoftech.abc.entity.auth.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByCustomer_Id(Long customerId);

    List<Contact> findByOwner_Id(Long ownerId);

    List<Contact> findByCustomer_Department_Id(Long departmentId);
}