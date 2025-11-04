package com.accuresoftech.abc.dto.response;

import com.accuresoftech.abc.enums.ContactRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactResponse 
{
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private ContactRole role;
    private boolean isPrimary;
    private String customerName;
}