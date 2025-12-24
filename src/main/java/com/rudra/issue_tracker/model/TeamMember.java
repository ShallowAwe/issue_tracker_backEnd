package com.rudra.issue_tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "project_teams", uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "team_id"}))
public class TeamMember {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Team team;
    @ManyToOne
    private User user;
    @Column
    private String roleInTeam;
}
