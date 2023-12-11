package org.studentresource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.studentresource.decorator.CommentableResource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StudentResourceManagerTest {
    private StudentResourceManager<Course> manager;

    @BeforeEach
    void setUp() {
        manager = new StudentResourceManager<>();
    }

    @Test
    void addAndRetrieveResourceTest() {
        Course course = new Course("CS101", "Introduction to Computer Science");
        manager.addResource(course);

        Course retrieved = manager.getResource("CS101");
        assertNotNull(retrieved, "Retrieved course should not be null.");
        assertEquals("Introduction to Computer Science", retrieved.getName(), "Course name should match.");
    }

    @Test
    void removeResourceTest() {
        Course course = new Course("CS101", "Introduction to Computer Science");
        manager.addResource(course);
        assertTrue(manager.removeResource(course), "Resource should be removed successfully");

        Course retrieved = manager.getResource("CS101");
        assertNull(retrieved, "Course should no longer exist after removal");
    }

    @Test
    void findAllCommentableResourcesTest() {
        StudentResourceManager<StudentResource> manager = new StudentResourceManager<>();
        Course course1 = new Course("CS101", "Intro to CS");
        CommentableResource commentableCourse = new CommentableResource(course1);

        manager.addResource(commentableCourse);
        manager.addResource(new Course("CS102", "Advanced CS")); // Обычный курс

        List<CommentableResource> commentableResources = manager.findAllCommentableResources();
        assertEquals(1, commentableResources.size(), "Should find only one commentable resource.");
    }

    @Test
    void findResourceByIdTest() {
        Course course = new Course("CS101", "Intro to CS");
        manager.addResource(course);

        Optional<Course> foundCourse = manager.findResourceById("CS101");
        assertTrue(foundCourse.isPresent(), "Course should be found by ID.");
        assertEquals("Intro to CS", foundCourse.get().getName(), "Course name should match.");
    }

}
