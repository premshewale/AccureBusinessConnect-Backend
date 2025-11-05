package com.accuresoftech.abc.dto.response;


import java.time.LocalDateTime;

import com.accuresoftech.abc.enums.CustomerStatus;
import com.accuresoftech.abc.enums.CustomerType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {
 
        private Long id;
        private String name;
        private String email;
        private String phone;
        private String address;
        private String industry;
        private CustomerType type;
        private CustomerStatus status;
        private String website;
        private Integer contactPersonCount;
        private String tags;

       
        private Integer totalContacts;
        private String assignedUserName;
        private String departmentName;
    
        
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        
        
}