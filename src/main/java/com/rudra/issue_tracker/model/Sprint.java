// Sprint.java
package com.rudra.issue_tracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sprints")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // e.g. "Sprint 1", "2025-01"
    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @ToString.Exclude
    private Project project;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(columnDefinition = "text")
    private String goal; // Jira “Sprint Goal”[web:70]

    @Column(nullable = false, length = 20)
    private String status; // PLANNED, ACTIVE, COMPLETED

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
