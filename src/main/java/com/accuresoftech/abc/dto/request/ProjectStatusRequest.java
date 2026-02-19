package com.accuresoftech.abc.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectStatusRequest {
    @NotBlank
    private String status;
}