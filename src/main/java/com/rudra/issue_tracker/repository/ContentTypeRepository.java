package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentTypeRepository extends JpaRepository<ContentType, Long> {
    Optional<ContentType> findByName(String name);
}
