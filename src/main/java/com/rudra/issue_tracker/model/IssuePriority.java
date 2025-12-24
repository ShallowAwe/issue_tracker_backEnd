// IssuePriority.java
package com.rudra.issue_tracker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "issue_priorities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssuePriority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // LOW, MEDIUM, HIGH, CRITICAL
    @Column(unique = true, nullable = false, length = 30)
    private String name;

    private Integer weight;
}
