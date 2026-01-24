package com.rudra.issue_tracker.service;

import com.rudra.issue_tracker.exceptions.NotFoundException;
import com.rudra.issue_tracker.model.Project;
import com.rudra.issue_tracker.model.User;
import com.rudra.issue_tracker.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    /**
     * ✅ CREATE PROJECT
     * Enforces unique project key.
     */
    @Transactional
    public Project createProject(String key, String name, String description, User owner) {

        if (projectRepository.existsByKey(key)) {
            throw new IllegalArgumentException("Project key already exists");
        }

        Project project = Project.builder()
                .key(key)
                .name(name)
                .description(description)
                .owner(owner)
                .build();

        return projectRepository.save(project);
    }

    /**
     * ✅ FETCH PROJECT BY KEY
     */
    @Transactional(readOnly = true)
    public Project getByKey(String key) {
        return projectRepository.findByKey(key)
                .orElseThrow(() ->
                        new NotFoundException("Project not found: " + key)
                );
    }

    public Project getByOwnerId(Long id){
       return projectRepository.findByOwnerId(id)
               .orElseThrow(()-> new NotFoundException("Project Not Found By OwnerId"+ id));
    }

    /**
     * ✅ FETCH ALL PROJECTS
     */
    @Transactional(readOnly = true)
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    /**
     * ✅ UPDATE PROJECT
     */
    @Transactional
    public Project updateProject(String key,
                                 String name,
                                 String description,
                                 Boolean archived) {

        Project project = getByKey(key);

        if (name != null) project.setName(name);
        if (description != null) project.setDescription(description);
        if (archived != null) project.setArchived(archived);

        return projectRepository.save(project);
    }

    /**
     * ✅ DELETE PROJECT
     */
    @Transactional
    public void deleteProject(String key) {
        Project project = getByKey(key);
        projectRepository.delete(project);
    }
}
