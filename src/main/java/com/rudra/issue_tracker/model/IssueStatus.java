package com.rudra.issue_tracker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "issue_status",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_issue_status_name",
                        columnNames = {"name"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Example values: Backlog, In Progress, Review, Done
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    // Used to control Kanban column order
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;
}
