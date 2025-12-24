// IssueType.java
package com.rudra.issue_tracker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "issue_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // BUG, TASK, STORY, EPIC, SUBTASK
    @Column(unique = true, nullable = false, length = 30)
    private String name;

    private String description;
}
