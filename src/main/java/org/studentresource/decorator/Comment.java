package org.studentresource.decorator;

import java.time.LocalDateTime;

public class Comment {
    private String text;
    private String author;
    private LocalDateTime created;

    public Comment(String text, String author, LocalDateTime created) {
        this.text = text;
        this.author = author;
        this.created = created;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getCreated() {
        return created;
    }

}
