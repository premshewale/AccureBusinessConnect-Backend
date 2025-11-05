package com.accuresoftech.abc.dto.request;

import com.accuresoftech.abc.enums.ContactRole;
import lombok.Data;

@Data
public class ContactRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private ContactRole role;
    private boolean isPrimary;
}