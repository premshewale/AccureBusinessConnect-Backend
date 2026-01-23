package com.accuresoftech.abc.services;

import java.time.LocalDateTime;
import java.util.List;

import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.entity.auth.Lead;

public interface ReportService {
    List<Lead> getLeadConversionReport(
            LocalDateTime from,
            LocalDateTime to
        );

    List<Customer> getCustomerGrowthReport(
    	    LocalDateTime from,
    	    LocalDateTime to
    	);

}
