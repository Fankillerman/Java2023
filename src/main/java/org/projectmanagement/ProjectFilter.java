package org.projectmanagement;

public interface ProjectFilter<T> {
    boolean filter(Project project, T name);
}
