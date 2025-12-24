// controller/dto/UpdateProjectRequest.java
package com.rudra.issue_tracker.controller.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectRequest {
    private String name;
    private String description;
    private Boolean archived;
}
