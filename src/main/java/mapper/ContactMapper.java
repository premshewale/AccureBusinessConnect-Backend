/*package mapper;

import com.accuresoftech.abc.dto.request.CustomerRequest.ContactRequest;
import com.accuresoftech.abc.dto.response.CustomerResponse.ContactResponse;
import com.accuresoftech.abc.entity.auth.Contact;
import com.accuresoftech.abc.entity.auth.Customer;

public class ContactMapper {

	 // Request → Entity
    public Contact toEntity(ContactRequest dto, Customer customer) {
        Contact contact = new Contact();
        contact.setCustomer(customer);
        contact.setFirstName(dto.getFirstName());
        contact.setLastName(dto.getLastName());
        contact.setEmail(dto.getEmail());
        contact.setPhone(dto.getPhone());
        contact.setRole(dto.getRole());
        contact.setIsPrimary(dto.getIsPrimary() != null ? dto.getIsPrimary() : false);
        return contact;
    }

    // Entity → Response
    public ContactResponse toResponse(Contact entity) {
        ContactResponse dto = new ContactResponse();
        dto.setId(entity.getId());
        dto.setCustomerId(entity.getCustomer() != null ? entity.getCustomer().getId() : null);
        dto.setCustomerName(entity.getCustomer() != null ? entity.getCustomer().getName() : "N/A");
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setRole(entity.getRole() != null ? entity.getRole().name() : null);
        dto.setIsPrimary(entity.getIsPrimary() != null ? entity.getIsPrimary() : false);
        return dto;
    }

    // Update entity from request
    public void updateEntity(Contact entity, ContactRequest dto, Customer customer) {
        entity.setCustomer(customer);
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setRole(dto.getRole());
        entity.setIsPrimary(dto.getIsPrimary() != null ? dto.getIsPrimary() : false);
    }
}*/
