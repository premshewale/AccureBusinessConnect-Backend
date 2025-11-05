package com.accuresoftech.abc.repository;

import com.accuresoftech.abc.entity.auth.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByCustomer_Id(Long customerId);
}