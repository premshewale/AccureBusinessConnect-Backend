package com.accuresoftech.abc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.accuresoftech.abc.entity.auth.SupportReply;

public interface SupportReplyRepository extends JpaRepository<SupportReply, Long> {
    List<SupportReply> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}

