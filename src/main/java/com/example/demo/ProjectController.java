package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;

    @PostMapping
    @CacheEvict(value = "projects", allEntries = true) // Clear cache on update
    public Project createProject(@RequestBody Project project) {
        return projectRepository.save(project);
    }

    @GetMapping
    @Cacheable("projects") // Cache the result
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @GetMapping("/{id}")
    @Cacheable(value = "projects", key = "#id") // Cache by ID
    public Project getProjectById(@PathVariable Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "projects", key = "#id") // Clear specific cache
    public Project updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        project.setName(projectDetails.getName());
        project.setEmployee(projectDetails.getEmployee());
        project.setDescription(projectDetails.getDescription());
        return projectRepository.save(project);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "projects", key = "#id") // Clear specific cache
    public void deleteProject(@PathVariable Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    @GetMapping("/search")
    @Cacheable(value = "projects", key = "#keyword") // Cache by keyword
    public List<Project> searchProjectsByDescription(@RequestParam String keyword) {
        return projectRepository.findByDescriptionContaining(keyword);
    }
}