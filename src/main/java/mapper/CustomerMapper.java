package mapper;

import com.accuresoftech.abc.dto.request.CustomerRequest;
import com.accuresoftech.abc.dto.response.CustomerResponse;
import com.accuresoftech.abc.entity.auth.Customer;

public class CustomerMapper {

	 /**
     * Convert CustomerRequest DTO to Customer entity
     */
    public Customer toEntity(CustomerRequest dto) {
        Customer entity = new Customer();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setIndustry(dto.getIndustry());
        entity.setType(dto.getType());       // Enum from DTO
        entity.setStatus(dto.getStatus());   // Enum from DTO
        entity.setWebsite(dto.getWebsite());
        entity.setContactPersonCount(dto.getContactPersonCount());
        return entity;
    }

    /**
     * Convert Customer entity to CustomerResponse DTO
     * Includes null-safe owner and department mapping
     * Converts enums to Strings for API response
     */
    public CustomerResponse toResponse(Customer entity) {
        CustomerResponse dto = new CustomerResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setAddress(entity.getAddress());
        dto.setIndustry(entity.getIndustry());

        // Convert enums to String for API response
        dto.setType(entity.getType() != null ? entity.getType().name() : null);
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);

        dto.setWebsite(entity.getWebsite());
        dto.setContactPersonCount(entity.getContactPersonCount());

        // Null-safe mapping for owner
        if (entity.getOwner() != null && entity.getOwner().getName() != null) {
            dto.setOwnerName(entity.getOwner().getName());
        } else {
            dto.setOwnerName("N/A");
        }

        // Null-safe mapping for department
        if (entity.getDepartment() != null && entity.getDepartment().getName() != null) {
            dto.setDepartmentName(entity.getDepartment().getName());
        } else {
            dto.setDepartmentName("N/A");
        }

        return dto;
    }

    /**
     * Update existing Customer entity from CustomerRequest
     */
    public void updateEntity(Customer entity, CustomerRequest dto) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setIndustry(dto.getIndustry());
        entity.setType(dto.getType());
        entity.setStatus(dto.getStatus());
        entity.setWebsite(dto.getWebsite());
        entity.setContactPersonCount(dto.getContactPersonCount());
    }
}
