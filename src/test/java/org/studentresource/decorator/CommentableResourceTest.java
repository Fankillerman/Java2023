package org.studentresource.decorator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.studentresource.Course;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommentableResourceTest {
    private CommentableResource commentableResource;

    @BeforeEach
    void setUp() {
        Course course = new Course("CS101", "Introduction to Computer Science");
        commentableResource = new CommentableResource(course);
    }

    @Test
    void addAndGetCommentTest() {
        String comment = "Excellent course for beginners.";
        String author = "Student B";
        commentableResource.addComment(comment, author);

        assertEquals(comment, commentableResource.getComment(), "The comment should match the added one.");
    }
    @Test
    void removeCommentTest() {
        String commentText = "A temporary comment.";
        String author = "Student C";
        Comment comment = commentableResource.addComment(commentText, author);

        assertTrue(commentableResource.removeComment(comment), "Comment should be removed successfully.");
        assertFalse(commentableResource.getComments().contains(comment), "Comment should not be present after removal.");
    }
    @Test
    void searchCommentsTest() {
        String comment1 = "This course is great!";
        String comment2 = "Great course, highly recommended!";
        String author = "Student D";
        commentableResource.addComment(comment1, author);
        commentableResource.addComment(comment2, author);

        List<Comment> searchResults = commentableResource.searchComments("great");
        assertEquals(2, searchResults.size(), "Should find two comments with the word 'great'.");
    }





}
