package com.accuresoftech.abc.entity.auth;

import com.accuresoftech.abc.entity.BaseEntity;
import com.accuresoftech.abc.entity.auth.User;
import jakarta.persistence.*;

@Entity
@Table(name = "support_replies")
public class SupportReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private SupportTicket ticket;

    @ManyToOne
    @JoinColumn(name = "replied_by", nullable = false)
    private User repliedBy;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

	public SupportReply() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SupportReply(Long id, SupportTicket ticket, User repliedBy, String message) {
		super();
		this.id = id;
		this.ticket = ticket;
		this.repliedBy = repliedBy;
		this.message = message;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SupportTicket getTicket() {
		return ticket;
	}

	public void setTicket(SupportTicket ticket) {
		this.ticket = ticket;
	}

	public User getRepliedBy() {
		return repliedBy;
	}

	public void setRepliedBy(User repliedBy) {
		this.repliedBy = repliedBy;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

    
}

