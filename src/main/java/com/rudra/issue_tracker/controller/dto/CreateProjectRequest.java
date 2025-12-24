// controller/dto/CreateProjectRequest.java
package com.rudra.issue_tracker.controller.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectRequest {

    private String key;

    private String name;

    private String description;
}
