package com.accuresoftech.abc.dto.request;

import lombok.Data;

@Data //  For converting lead to customer
public class LeadConversionRequest 
{
    private Long leadId;
    private String customerType;
    private String industry;
    private String address;
    private String website;
    
}