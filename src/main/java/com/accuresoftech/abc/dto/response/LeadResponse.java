package com.accuresoftech.abc.dto.response;

import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String source;
    private String status;
    private String ownerName;
    private String assignedToName;   // new
    private String departmentName;
    private Long customerId;
    private LocalDateTime createdAt;
}