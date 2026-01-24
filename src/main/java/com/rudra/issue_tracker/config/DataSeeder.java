// config/DataSeeder.java
package com.rudra.issue_tracker.config;

import com.rudra.issue_tracker.model.*;
import com.rudra.issue_tracker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final IssueTypeRepository issueTypeRepository;
    private final IssuePriorityRepository issuePriorityRepository;
    private final IssueStatusRepository issueStatusRepository;
    private final ProjectRoleRepository projectRoleRepository;
    private final ConversationStatusRepository conversationStatusRepository;
    private final DeliveryStatusRepository deliveryStatusRepository;
    private final MessageReceiptStatusRepository messageReceiptStatusRepository;
    private final ContentTypeRepository contentTypeRepository;

    @Override
    public void run(String... args) {
        seedIssueTypes();
        seedIssuePriorities();
        seedIssueStatuses();
        seedProjectRoles();
        seedConversationStatuses();
        seedDeliveryStatuses();
        seedMessageReceiptStatuses();
        seedContentTypes();
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
    
    // Injecting the enum data for Conversation Status
    private void seedConversationStatuses(){
        createConversationStatusIfNotExists("ACTIVE");
        createConversationStatusIfNotExists("ARCHIVED");
        createConversationStatusIfNotExists("BLOCKED");
    }
    /// Initalization of the for seeding the Conversation Status data
    private void createConversationStatusIfNotExists(String name){
        if(conversationStatusRepository.findByName(name).isEmpty()){
            conversationStatusRepository.save(
                    ConversationStatus.builder()
                            .name(name)
                            .build()
            );
        }
    }

    // Seeding Delivery Status data
    private void seedDeliveryStatuses() {
        createDeliveryStatusIfNotExists("SENT");
        createDeliveryStatusIfNotExists("DELIVERED");
        createDeliveryStatusIfNotExists("READ");
    }

    private void createDeliveryStatusIfNotExists(String name) {
        if (deliveryStatusRepository.findByName(name).isEmpty()) {
            deliveryStatusRepository.save(
                    DeliveryStatus.builder()
                            .name(name)
                            .build()
            );
        }
    }

    // Seeding Message Receipt Status data
    private void seedMessageReceiptStatuses() {
        createMessageReceiptStatusIfNotExists("DELIVERED");
        createMessageReceiptStatusIfNotExists("READ");
    }

    private void createMessageReceiptStatusIfNotExists(String name) {
        if (messageReceiptStatusRepository.findByName(name).isEmpty()) {
            messageReceiptStatusRepository.save(
                    MessageReceiptStatus.builder()
                            .name(name)
                            .build()
            );
        }
    }

    // Seeding Content Type data
    private void seedContentTypes() {
        createContentTypeIfNotExists("TEXT");
        createContentTypeIfNotExists("IMAGE");
        createContentTypeIfNotExists("SYSTEM");
    }

    private void createContentTypeIfNotExists(String name) {
        if (contentTypeRepository.findByName(name).isEmpty()) {
            contentTypeRepository.save(
                    ContentType.builder()
                            .name(name)
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


    //seeding the project Roles
    private void seedProjectRoles() {
        createRoleIfNotExists("OWNER", "Full project control");
        createRoleIfNotExists("DEVELOPER", "Can edit issues and sprints");
        createRoleIfNotExists("TESTER","Can edit issues and sprints");
        createRoleIfNotExists("VIEWER", "Read-only access");
    }

    private void createRoleIfNotExists(String name, String description) {
        if (projectRoleRepository.findByName(name).isEmpty()) {
            projectRoleRepository.save(
                    ProjectRole.builder()
                            .name(name)
                            .description(description)
                            .build()
            );
        }
    }

}
