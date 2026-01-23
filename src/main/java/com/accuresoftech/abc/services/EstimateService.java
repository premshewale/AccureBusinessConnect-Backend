package com.accuresoftech.abc.services;

import com.accuresoftech.abc.dto.request.EstimateRequest;
import com.accuresoftech.abc.dto.response.EstimateResponse;
import java.util.List;

public interface EstimateService {
    EstimateResponse createEstimate(EstimateRequest request);
    List<EstimateResponse> getAllEstimates();
    EstimateResponse getEstimateById(Long id);
    EstimateResponse updateEstimate(Long id, EstimateRequest request);
    void deleteEstimate(Long id);
    EstimateResponse sendEstimate(Long id); // mark sent
    EstimateResponse convertToInvoice(Long id); // optional marker
}
