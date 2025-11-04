package com.accuresoftech.abc.services;

import com.accuresoftech.abc.dto.request.ContactRequest;
import com.accuresoftech.abc.dto.response.ContactResponse;
import java.util.List;

public interface ContactService 
{
    ContactResponse createContact(Long customerId, ContactRequest request);
    List<ContactResponse> getContactsByCustomer(Long customerId);
    ContactResponse updateContact(Long id, ContactRequest request);
    void deleteContact(Long id);
    
}