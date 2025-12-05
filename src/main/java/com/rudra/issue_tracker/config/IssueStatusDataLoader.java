package com.rudra.issue_tracker.config;

import com.rudra.issue_tracker.model.IssueStatus;
import com.rudra.issue_tracker.repository.IssueStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IssueStatusDataLoader implements ApplicationRunner {

    private final IssueStatusRepository issueStatusRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (issueStatusRepository.count() > 0) {
            return; // Already seeded, do nothing
        }

        List<IssueStatus> defaultStatuses = List.of(
                IssueStatus.builder().name("Backlog").orderIndex(1).build(),
                IssueStatus.builder().name("In Progress").orderIndex(2).build(),
                IssueStatus.builder().name("Review").orderIndex(3).build(),
                IssueStatus.builder().name("Done").orderIndex(4).build()
        );

        issueStatusRepository.saveAll(defaultStatuses);

        System.out.println("✅ Default IssueStatus seeded successfully.");
    }
}
