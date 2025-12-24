// config/DataSeeder.java
package com.rudra.issue_tracker.config;

import com.rudra.issue_tracker.model.IssuePriority;
import com.rudra.issue_tracker.model.IssueStatus;
import com.rudra.issue_tracker.model.IssueType;
import com.rudra.issue_tracker.repository.IssuePriorityRepository;
import com.rudra.issue_tracker.repository.IssueStatusRepository;
import com.rudra.issue_tracker.repository.IssueTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final IssueTypeRepository issueTypeRepository;
    private final IssuePriorityRepository issuePriorityRepository;
    private final IssueStatusRepository issueStatusRepository;

    @Override
    public void run(String... args) {
        seedIssueTypes();
        seedIssuePriorities();
        seedIssueStatuses();
    }

    private void seedIssueTypes() {
        createTypeIfNotExists("EPIC", "Large body of work");
        createTypeIfNotExists("STORY", "User story");
        createTypeIfNotExists("TASK", "General task");
        createTypeIfNotExists("BUG", "Defect or problem");
        createTypeIfNotExists("SUBTASK", "Smaller unit of work");
    }

    private void createTypeIfNotExists(String name, String description) {
        if (issueTypeRepository.findByName(name).isEmpty()) {
            issueTypeRepository.save(
                    IssueType.builder()
                            .name(name)
                            .description(description)
                            .build()
            );
        }
    }

    private void seedIssuePriorities() {
        createPriorityIfNotExists("HIGHEST", 5);
        createPriorityIfNotExists("HIGH", 4);
        createPriorityIfNotExists("MEDIUM", 3);
        createPriorityIfNotExists("LOW", 2);
        createPriorityIfNotExists("LOWEST", 1);
    }

    private void createPriorityIfNotExists(String name, int weight) {
        if (issuePriorityRepository.findByName(name).isEmpty()) {
            issuePriorityRepository.save(
                    IssuePriority.builder()
                            .name(name)
                            .weight(weight)
                            .build()
            );
        }
    }

    private void seedIssueStatuses() {
        createStatusIfNotExists("TO_DO");
        createStatusIfNotExists("IN_PROGRESS");
        createStatusIfNotExists("DONE");
    }

    private void createStatusIfNotExists(String name) {
        if (issueStatusRepository.findByName(name).isEmpty()) {
            issueStatusRepository.save(
                    IssueStatus.builder()
                            .name(name)
                            .build()
            );
        }
    }
}
