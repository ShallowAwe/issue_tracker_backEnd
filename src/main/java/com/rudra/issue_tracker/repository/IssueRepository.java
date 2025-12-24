// IssueRepository.java
package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.Issue;
import com.rudra.issue_tracker.model.Project;
import com.rudra.issue_tracker.model.Sprint;
import com.rudra.issue_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    Optional<Issue> findByKey(String key);
    List<Issue> findByProject(Project project);
    List<Issue> findByAssignee(User assignee);
    List<Issue> findBySprint(Sprint sprint);
}
