package org.projectmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectManagerTest {
    private ProjectManager projectManager;
    private Project testProject;

    @BeforeEach
    void setUp() throws ProjectDuplicatedNameException {
        projectManager = new ProjectManager();

        testProject = new Project("Test Project", new Date());
        testProject.addTask(new Task("Task 1", "Open", 1));
        testProject.addTask(new Task("Task 2", "InProgress", 2));
        testProject.addTask(new Task("Task 3", "InProgress", 3));
        testProject.addTask(new Task("Task 4", "InProgress", 4));
        testProject.addTask(new Task("Task 5", "Closed", 4));
        projectManager.addProject(testProject);

        projectManager.addProject(new Project("Projekt X", new Date(2024, 1, 1)));
        projectManager.addProject(new Project("Projekt Y", new Date(2023, 6, 1)));
    }

    @Test
    void testAddDuplicatedProjectThrowsException() {
        String projectName = "Projekt Z";
        Project project1 = new Project(projectName, new Date());
        Project project2 = new Project(projectName, new Date());

        // Adding first project should succeed
        assertDoesNotThrow(() -> projectManager.addProject(project1));

        // Attempting to add a second project with the same name should result in an exception
        assertThrows(ProjectDuplicatedNameException.class, () -> projectManager.addProject(project2));
    }

    @Test
    void testUpdateProjectDeadline() throws ProjectNotFoundException, ProjectDuplicatedNameException {
        String projectName = "Projekt X";

        ProjectManager newProjectManager = new ProjectManager();
        Project project = new Project(projectName, new Date(System.currentTimeMillis() + 8 * 24 * 60 * 60 * 1000)); // 8 dni od teraz
        project.addTask(new Task("Task 1", "Open", 1));
        newProjectManager.addProject(project);

        Date newDeadline = new Date(System.currentTimeMillis() + 6 * 24 * 60 * 60 * 1000); // 6 days from now
        newProjectManager.updateProjectDeadline(projectName, newDeadline);

        Project updatedProject = newProjectManager.findProjectByName(projectName);
        assertEquals(newDeadline, updatedProject.getDeadline());

        // Check, if priority was increased from 1 to 2
        Task task = updatedProject.getTasks().get(0);
        assertEquals("Updated due to deadline change", task.getStatus());
        assertEquals(2, task.getPriority()); // Priority should be increased
    }

    @Test
    void testFilterProjectsByName() {
        // Find "Projekt X"
        List<Project> projectsWithNameX = projectManager.filterProjects(
                (project, name) -> project.getName().equals(name),
                new String("Projekt X")
        );
        assertEquals(1, projectsWithNameX.size());
        assertEquals("Projekt X", projectsWithNameX.get(0).getName());
    }

    @Test
    void testFilterProjectsByDeadline() {
        // Find project with specific deadline
        Date specificDate = new Date(2024, 1, 1);
        List<Project> projectsWithSpecificDeadline = projectManager.filterProjects(
                (project, deadline) -> project.getDeadline().equals(deadline),
                specificDate
        );
        assertTrue(projectsWithSpecificDeadline.stream().allMatch(project -> project.getDeadline().equals(specificDate)));
    }

    @Test
    void testFilterProjectsByTaskCount() throws ProjectNotFoundException {
        // Find project with at last five tasks
        int taskThreshold = 5;
        // Let's consider "Projekt Y" with more than 5 tasks
        projectManager.findProjectByName("Projekt Y").getTasks().add(new Task("Task A", "Open", 1));
        projectManager.findProjectByName("Projekt Y").getTasks().add(new Task("Task B", "Open", 1));
        projectManager.findProjectByName("Projekt Y").getTasks().add(new Task("Task C", "Open", 1));
        projectManager.findProjectByName("Projekt Y").getTasks().add(new Task("Task D", "Open", 1));
        projectManager.findProjectByName("Projekt Y").getTasks().add(new Task("Task E", "Open", 1));
        projectManager.findProjectByName("Projekt Y").getTasks().add(new Task("Task F", "Open", 1));

        List<Project> projectsWithMoreThan5Tasks = projectManager.filterProjects(
                (project, threshold) -> project.getTasks().size() > threshold,
                taskThreshold
        );
        assertTrue(projectsWithMoreThan5Tasks.stream().anyMatch(project -> project.getName().equals("Projekt Y")));
    }

    @Test
    void testAddAndFindProject() throws ProjectNotFoundException {
        Project found = projectManager.findProjectByName("Test Project");
        assertEquals(testProject, found);
    }
}
