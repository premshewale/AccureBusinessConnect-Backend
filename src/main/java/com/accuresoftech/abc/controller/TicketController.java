package com.accuresoftech.abc.controller;

import com.accuresoftech.abc.dto.request.TicketRequest;
import com.accuresoftech.abc.dto.response.TicketResponse;
import com.accuresoftech.abc.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // 🟩 Create Ticket
    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest request) {
        TicketResponse response = ticketService.createTicket(request);
        return ResponseEntity.ok(response);
    }

    // 🟦 Get All Tickets (Role-Based Visibility handled in service)
    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        List<TicketResponse> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    // 🟨 Get Ticket by ID (Access check handled in service)
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Long id) {
        TicketResponse response = ticketService.getTicketById(id);
        return ResponseEntity.ok(response);
    }

    // 🟧 Update Ticket
    @PutMapping("/{id}")
    public ResponseEntity<TicketResponse> updateTicket(@PathVariable Long id, @RequestBody TicketRequest request) {
        TicketResponse response = ticketService.updateTicket(id, request);
        return ResponseEntity.ok(response);
    }

    // 🟥 Delete Ticket
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.ok("Ticket deleted successfully");
    }
    
    //escalate
   /* @PostMapping("/{id}/escalate")
    public ResponseEntity<TicketResponse> escalateTicket(@PathVariable Long id) {
        TicketResponse response = ticketService.escalateTicket(id);
        return ResponseEntity.ok(response);
    }*/
    
    @PostMapping("/{id}/escalate")
    public ResponseEntity<TicketResponse> escalateTicket(@PathVariable Long id) {
        TicketResponse escalatedTicket = ticketService.escalateTicket(id);
        return ResponseEntity.ok(escalatedTicket);
    }
}
