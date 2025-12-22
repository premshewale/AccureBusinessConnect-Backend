package com.accuresoftech.abc.repository;

import com.accuresoftech.abc.entity.auth.EstimateAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstimateAttachmentRepository extends JpaRepository<EstimateAttachment, Long> {
}
