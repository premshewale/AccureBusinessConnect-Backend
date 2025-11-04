package com.accuresoftech.abc.dto.request;

import com.accuresoftech.abc.enums.LeadSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeadRequest {

    @NotBlank
    private String name;

    private String email;
    private String phone;

    @NotNull
    private LeadSource source;

    private Long ownerId;        // creator/owner assignment (manager)
    private Long assignedTo;     // staff handling the lead
    private Long departmentId;
}