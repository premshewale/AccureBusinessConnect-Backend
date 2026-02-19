package com.accuresoftech.abc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accuresoftech.abc.entity.auth.ProjectTag;

@Repository
public interface ProjectTagRepository extends JpaRepository<ProjectTag, Long> {
}