package com.accuresoftech.abc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accuresoftech.abc.dto.request.SupportReplyRequest;
import com.accuresoftech.abc.dto.request.SupportTicketRequest;
import com.accuresoftech.abc.dto.response.SupportReplyResponse;
import com.accuresoftech.abc.dto.response.SupportTicketResponse;
import com.accuresoftech.abc.services.SupportTicketService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/support/tickets")
public class SupportTicketController {

    private final SupportTicketService service;

    public SupportTicketController(SupportTicketService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SupportTicketResponse> create(@RequestBody SupportTicketRequest req) {
        return ResponseEntity.ok(service.createTicket(req));
    }

    @GetMapping
    public ResponseEntity<List<SupportTicketResponse>> getAll() {
        return ResponseEntity.ok(service.getAllTickets());
    }

    @PostMapping("/{id}/reply")
    public ResponseEntity<SupportReplyResponse> reply(
            @PathVariable Long id,
            @RequestBody SupportReplyRequest req) {
        return ResponseEntity.ok(service.addReply(id, req));
    }
}
