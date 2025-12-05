package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByProjectId(Long projectId);

    List<Issue> findByAssignedTo(Long userId);

    List<Issue> findByStatusId(Long statusId);

    List<Issue> findByProjectIdAndStatusId(Long projectId, Long statusId);
}
