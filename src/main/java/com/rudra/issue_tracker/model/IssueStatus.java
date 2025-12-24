// IssueStatus.java (optional lookup table)
package com.rudra.issue_tracker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "issue_statuses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // OPEN, IN_PROGRESS, RESOLVED, CLOSED, REOPENED
    @Column(unique = true, nullable = false, length = 30)
    private String name;
}
