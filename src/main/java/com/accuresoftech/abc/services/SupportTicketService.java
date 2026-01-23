package com.accuresoftech.abc.services;

import com.accuresoftech.abc.dto.request.SupportReplyRequest;
import com.accuresoftech.abc.dto.request.SupportTicketRequest;
import com.accuresoftech.abc.dto.response.SupportReplyResponse;
import com.accuresoftech.abc.dto.response.SupportTicketResponse;

import java.util.List;

public interface SupportTicketService {

    SupportTicketResponse createTicket(SupportTicketRequest request);

    List<SupportTicketResponse> getAllTickets();

    SupportTicketResponse getTicketById(Long id);

    SupportReplyResponse addReply(Long ticketId, SupportReplyRequest request);
}
