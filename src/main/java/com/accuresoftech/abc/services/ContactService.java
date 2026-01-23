package com.accuresoftech.abc.services;

import java.util.List;

import com.accuresoftech.abc.dto.request.ContactRequest;
import com.accuresoftech.abc.dto.response.ContactResponse;

public interface ContactService 
{
	ContactResponse createContact(Long customerId, ContactRequest request);

	List<ContactResponse> getContactsByCustomer(Long customerId);

	ContactResponse updateContact(Long id, ContactRequest request);

	void deleteContact(Long id);

	List<ContactResponse> getAllContacts();
	
	void deactivateContact(Long id);

    void activateContact(Long id);
	
//	List<ContactResponse> getAllContacts();

}