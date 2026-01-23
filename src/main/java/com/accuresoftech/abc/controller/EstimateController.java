package com.accuresoftech.abc.controller;

import com.accuresoftech.abc.dto.request.EstimateRequest;
import com.accuresoftech.abc.dto.response.EstimateResponse;
import com.accuresoftech.abc.services.EstimateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estimates")
public class EstimateController {

    private final EstimateService estimateService;

    public EstimateController(EstimateService estimateService) {
        this.estimateService = estimateService;
    }

    @PostMapping
    public ResponseEntity<EstimateResponse> create(@RequestBody EstimateRequest req) {
        return ResponseEntity.ok(estimateService.createEstimate(req));
    }

    @GetMapping
    public ResponseEntity<List<EstimateResponse>> list() {
        return ResponseEntity.ok(estimateService.getAllEstimates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstimateResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(estimateService.getEstimateById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstimateResponse> update(@PathVariable Long id, @RequestBody EstimateRequest req) {
        return ResponseEntity.ok(estimateService.updateEstimate(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        estimateService.deleteEstimate(id);
        return ResponseEntity.ok("Estimate deleted");
    }

    @PostMapping("/{id}/send")
    public ResponseEntity<EstimateResponse> send(@PathVariable Long id) {
        return ResponseEntity.ok(estimateService.sendEstimate(id));
    }

    @PostMapping("/{id}/convert")
    public ResponseEntity<EstimateResponse> convertToInvoice(@PathVariable Long id) {
        return ResponseEntity.ok(estimateService.convertToInvoice(id));
    }
}
