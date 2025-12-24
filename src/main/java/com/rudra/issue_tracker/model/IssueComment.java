package com.rudra.issue_tracker.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "issue_comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Issue this comment belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    @ToString.Exclude
    private Issue issue;

    // Author of the comment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @ToString.Exclude
    private User author;

    @Column(columnDefinition = "text", nullable = false)
    private String body; // comment text[web:181]

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Optional last edited info
    private LocalDateTime updatedAt;
}
