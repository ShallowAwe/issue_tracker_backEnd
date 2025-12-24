package com.rudra.issue_tracker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;          // 1=OWNER, 2=DEVELOPER, 3=VIEWER

    @Column(unique = true, nullable = false, length = 30)
    private String name;      // OWNER, DEVELOPER, VIEWER

    private String description;
}
