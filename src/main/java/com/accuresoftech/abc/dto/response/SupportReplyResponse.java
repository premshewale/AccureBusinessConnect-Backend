package com.accuresoftech.abc.dto.response;

import java.time.LocalDateTime;

public class SupportReplyResponse {

    private Long id;
    private String message;
    private String repliedBy;
    private LocalDateTime repliedAt;
	public SupportReplyResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SupportReplyResponse(Long id, String message, String repliedBy, LocalDateTime repliedAt) {
		super();
		this.id = id;
		this.message = message;
		this.repliedBy = repliedBy;
		this.repliedAt = repliedAt;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRepliedBy() {
		return repliedBy;
	}
	public void setRepliedBy(String repliedBy) {
		this.repliedBy = repliedBy;
	}
	public LocalDateTime getRepliedAt() {
		return repliedAt;
	}
	public void setRepliedAt(LocalDateTime repliedAt) {
		this.repliedAt = repliedAt;
	}

    
}

