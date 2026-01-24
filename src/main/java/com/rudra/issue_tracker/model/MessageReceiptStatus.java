package com.rudra.issue_tracker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "message_receipt_statuses")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MessageReceiptStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 30)
    private String name;
}
