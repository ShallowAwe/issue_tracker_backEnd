// IssueService.java
package com.rudra.issue_tracker.service;

import com.rudra.issue_tracker.exceptions.NotFoundException;
import com.rudra.issue_tracker.model.*;
import com.rudra.issue_tracker.repository.IssueRepository;
import com.rudra.issue_tracker.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectService projectService;
    private final UserService userService;
    private final SprintRepository sprintRepository;

    @Transactional
    public Issue createIssue(String projectKey,
                             String summary,
                             String description,
                             IssueType type,
                             IssuePriority priority,
                             User reporter,
                             User assignee) {

        Project project = projectService.getByKey(projectKey);

        // Simple JIRA-like key generation: PROJ-<seq>
        long nextNumber = issueRepository.count() + 1; // later replace with per-project sequence
        String issueKey = project.getKey() + "-" + nextNumber;

        Issue issue = Issue.builder()
                .key(issueKey)
                .project(project)
                .summary(summary)
                .description(description)
                .type(type)
                .priority(priority)
                .status(defaultStatus()) // e.g. OPEN
                .reporter(reporter)
                .assignee(assignee)
                .build();

        return issueRepository.save(issue);
    }

    private IssueStatus defaultStatus() {
        IssueStatus status = new IssueStatus();
        status.setId(1); // or load from DB by name "OPEN"
        return status;
    }

    public Issue getByKey(String key) {
        return issueRepository.findByKey(key)
                .orElseThrow(() -> new NotFoundException("Issue not found: " + key));
    }

    @Transactional
    public Issue updateIssue(String issueKey,
                             String summary,
                             String description,
                             Integer priorityId,
                             Integer statusId,
                             Long assigneeId,
                             LocalDateTime dueDate) {

        Issue issue = getByKey(issueKey);

        if (summary != null) issue.setSummary(summary);
        if (description != null) issue.setDescription(description);

        if (priorityId != null) {
            IssuePriority priority = new IssuePriority();
            priority.setId(priorityId);
            issue.setPriority(priority);
        }

        if (statusId != null) {
            IssueStatus status = new IssueStatus();
            status.setId(statusId);
            issue.setStatus(status);
        }

        if (assigneeId != null) {
            User assignee = userService.findById(assigneeId);
            issue.setAssignee(assignee);
        }

        if (dueDate != null) {
            issue.setDueDate(dueDate);
        }

        return issueRepository.save(issue);
    }

    @Transactional(readOnly = true)
    public List<Issue> getIssuesByProject(String projectKey) {
        Project project = projectService.getByKey(projectKey);
        return issueRepository.findByProject(project);
    }

    @Transactional
    public void deleteIssue(String issueKey) {
        Issue issue = getByKey(issueKey);
        issueRepository.delete(issue);
    }

    @Transactional
    public Issue assignIssueToSprint(String issueKey, Long sprintId) {
        Issue issue = getByKey(issueKey);

        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new NotFoundException("Sprint not found: " + sprintId));

        issue.setSprint(sprint);
        return issueRepository.save(issue);
    }

    @Transactional
    public Issue removeIssueFromSprint(String issueKey) {
        Issue issue = getByKey(issueKey);
        issue.setSprint(null);
        return issueRepository.save(issue);
    }

    @Transactional(readOnly = true)
    public List<Issue> getIssuesBySprint(Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new NotFoundException("Sprint not found: " + sprintId));
        return issueRepository.findBySprint(sprint);
    }

}
