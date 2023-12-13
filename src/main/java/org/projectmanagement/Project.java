package org.projectmanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project {
    private final String name;
    private Date deadline;
    private final List<Task> tasks;

    public Project(String name, Date deadline) {
        this.name = name;
        this.deadline = deadline;
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
