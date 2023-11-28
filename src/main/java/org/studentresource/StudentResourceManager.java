package org.studentresource;

import org.studentresource.decorator.CommentableResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentResourceManager<T extends StudentResource> {
    private List<T> resources = new ArrayList<>();

    public void addResource(T resource) {
        resources.add(resource);
    }

    public boolean removeResource(T resource) {
        return resources.remove(resource);
    }

    public Optional<T> findResourceById(String id) {
        return resources.stream()
                .filter(resource -> resource.getId().equals(id))
                .findFirst();
    }

    public T getResource(String id) {
        return resources.stream()
                .filter(resource -> resource.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<CommentableResource> findAllCommentableResources() {
        return resources.stream()
                .filter(resource -> resource instanceof CommentableResource)
                .map(resource -> (CommentableResource) resource)
                .collect(Collectors.toList());
    }
}
