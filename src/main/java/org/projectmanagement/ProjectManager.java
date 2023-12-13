package org.projectmanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectManager {
    private final List<Project> projects;

    public ProjectManager() {
        this.projects = new ArrayList<>();
    }

    public Project findProjectByName(String name) throws ProjectNotFoundException {
        for (Project project : projects) {
            if (project.getName().equals(name)) {
                return project;
            }
        }
        throw new ProjectNotFoundException("Project with name " + name + " not found");
    }

    public void updateProjectDeadline(String projectName, Date newDeadline) throws ProjectNotFoundException {
        Project project = findProjectByName(projectName);
        project.setDeadline(newDeadline);

        for (Task task : project.getTasks()) {
            if ("Open".equals(task.getStatus())) {
                task.setStatus("Updated due to deadline change");
                if (newDeadline.getTime() - System.currentTimeMillis() < 7 * 24 * 60 * 60 * 1000) {
                    task.setPriority(task.getPriority() + 1);
                }
            }
        }
    }

    public void addProject(Project project) throws ProjectDuplicatedNameException {
        for (Project existingProject : projects) {
            if (existingProject.getName().equals(project.getName())) {
                throw new ProjectDuplicatedNameException("Project with name " + project.getName() + " already exists");
            }
        }
        projects.add(project);
    }

    public <T> List<Project> filterProjects(ProjectFilter<T> filter, T name) {
        List<Project> filteredProjects = new ArrayList<>();
        for (Project project : projects) {
            if (filter.filter(project, name)) {
                filteredProjects.add(project);
            }
        }
        return filteredProjects;
    }
}
