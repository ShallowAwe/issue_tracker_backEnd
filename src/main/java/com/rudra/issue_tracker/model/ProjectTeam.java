package com.rudra.issue_tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "project_teams",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_project_teams_project_team",
                        columnNames = {"project_id", "team_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK to projects.id
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    // FK to teams.id
    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
