package org.projectmanagement;

public class Task {
    private final String description;
    private String status;
    private int priority;

    public Task(String description, String status, int priority) {
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

    // Getters and Setters
    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
