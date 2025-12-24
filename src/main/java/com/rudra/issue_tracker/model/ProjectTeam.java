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
@Table(name = "project_teams", uniqueConstraints = @UniqueConstraint(columnNames = {"Project_id","team_id"}))
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectTeam {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Project project;
    @ManyToOne
    private Team team;
    @CreationTimestamp
    private LocalDateTime assignAt;

}
