package com.accuresoftech.abc.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Pageable;
import com.accuresoftech.abc.dto.request.CustomerRequest;
import com.accuresoftech.abc.dto.response.CustomPageResponse;
import com.accuresoftech.abc.dto.response.CustomerResponse;
import com.accuresoftech.abc.services.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); 
    }

    @GetMapping
    public CustomPageResponse<CustomerResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return customerService.getAll(pageable, search);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id)); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(
            @PathVariable Long id,
            @RequestBody CustomerRequest request) {

        return ResponseEntity.ok(customerService.updateCustomer(id, request)); 
    }
    
    
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<CustomerResponse> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.deactivateCustomer(id));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<CustomerResponse> activate(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.activateCustomer(id));
    }

    /*@DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build(); 
    }*/
}