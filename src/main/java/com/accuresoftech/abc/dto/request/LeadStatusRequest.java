package com.accuresoftech.abc.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LeadStatusRequest 
{
    @NotBlank
    private String status; // e.g. NEW, CONTACTED, QUALIFIED, WON, LOST
}