package org.studentresource.decorator;

import org.studentresource.StudentResource;

public class RateableResource extends ResourceDecorator {
    private double rating;

    public RateableResource(StudentResource decoratedResource) {
        super(decoratedResource);
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public StudentResource getDecoratedResource() {
        return this.decoratedResource;
    }

}
