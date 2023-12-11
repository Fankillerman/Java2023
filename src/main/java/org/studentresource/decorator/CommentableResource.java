package org.studentresource.decorator;

import org.studentresource.StudentResource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentableResource extends ResourceDecorator {
    private List<Comment> comments;

    public CommentableResource(StudentResource decoratedResource) {
        super(decoratedResource);
        this.comments = new ArrayList<>();
    }

    public Comment addComment(String text, String author) {
        Comment newComment = new Comment(text, author, LocalDateTime.now());
        comments.add(newComment);
        return newComment;
    }


    public boolean removeComment(Comment comment) {
        return comments.remove(comment);
    }

    public List<Comment> searchComments(String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return comments.stream()
                .filter(comment -> comment.getText().toLowerCase().contains(lowerCaseKeyword))
                .collect(Collectors.toList());
    }


    public List<Comment> getComments() {
        return new ArrayList<>(comments);
    }

    @Override
    public String getId() {
        return decoratedResource.getId();
    }

    @Override
    public String getName() {
        return decoratedResource.getName();
    }

    public String getComment() {
        if (comments.isEmpty()) {
            return null;
        }
        return comments.get(comments.size() - 1).getText();
    }

}
