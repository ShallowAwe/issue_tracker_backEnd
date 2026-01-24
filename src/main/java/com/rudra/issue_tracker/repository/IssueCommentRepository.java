package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.Issue;
import com.rudra.issue_tracker.model.IssueComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueCommentRepository extends JpaRepository<IssueComment, Long> {
    List<IssueComment> findByIssueOrderByCreatedAtAsc(Issue issue);
}