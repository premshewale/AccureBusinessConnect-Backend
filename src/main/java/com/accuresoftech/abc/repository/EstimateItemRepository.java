package com.accuresoftech.abc.repository;

import com.accuresoftech.abc.entity.auth.EstimateItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstimateItemRepository extends JpaRepository<EstimateItem, Long> {
}
