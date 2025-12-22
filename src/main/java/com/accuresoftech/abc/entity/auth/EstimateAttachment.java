package com.accuresoftech.abc.entity.auth;

import jakarta.persistence.*;

@Entity
@Table(name = "estimate_attachments")
public class EstimateAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimate_id", nullable = false)
    private Estimate estimate;

    @Column(nullable = false, length = 255)
    private String filename;

    @Column(length = 1024)
    private String url;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column
    private Long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    public EstimateAttachment() {}

	public EstimateAttachment(Long id, Estimate estimate, String filename, String url, String contentType, Long size,
			User uploadedBy) {
		super();
		this.id = id;
		this.estimate = estimate;
		this.filename = filename;
		this.url = url;
		this.contentType = contentType;
		this.size = size;
		this.uploadedBy = uploadedBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Estimate getEstimate() {
		return estimate;
	}

	public void setEstimate(Estimate estimate) {
		this.estimate = estimate;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public User getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(User uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

   
	
	
}
