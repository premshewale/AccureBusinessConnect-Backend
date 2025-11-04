package com.accuresoftech.abc.dto.request;

import java.util.List;

import com.accuresoftech.abc.enums.CustomerStatus;
import com.accuresoftech.abc.enums.CustomerType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerRequest 
{
    @NotBlank
    private String name;
   
    @Email
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    private String phone;
    
    
    private String address;
    private String industry;
    private String website;
    private Integer contactPersonCount;
    private String tags;
    private CustomerType type;
    private CustomerStatus status;
    private Long assignedUserId;
    private Long departmentId;
    
    
    private List<ContactRequest> contacts;
    
}