package com.accuresoftech.abc.services;

import com.accuresoftech.abc.dto.request.TicketRequest;
import com.accuresoftech.abc.dto.response.TicketResponse;

import java.util.List;

public interface TicketService {
    TicketResponse createTicket(TicketRequest request);
    List<TicketResponse> getAllTickets();
    TicketResponse getTicketById(Long id);
    TicketResponse updateTicket(Long id, TicketRequest request);
    //void deleteTicket(Long id);
    
    TicketResponse escalateTicket(Long id);
    
    void deactivateTicket(Long id);

    void activateTicket(Long id);
}
