package org.starmap.controller;

import org.starmap.model.Constellation;
import org.starmap.model.Star;

import java.io.IOException;
import java.util.List;

public class StarMapController {
    private List<Star> stars;
    private List<Constellation> constellations;
    private FileManagementController fileManagementController;

    public StarMapController(String dataFilePath) throws IOException {
        fileManagementController = new FileManagementController();
        loadData(dataFilePath);
    }

    public void loadData(String filePath) throws IOException {
        this.stars = fileManagementController.loadStars(filePath);
        this.constellations = fileManagementController.loadConstellations(filePath, stars);
    }

    public void saveData(String filePath) {
        try {
            fileManagementController.saveData(stars, constellations, filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Star> getStars() {
        return stars;
    }

    public List<Constellation> getConstellations() {
        return constellations;
    }

    public void addStar(Star star) {
        stars.add(star);
    }

    public void removeStar(String name) {
        stars.removeIf(star -> star.getName().equalsIgnoreCase(name));
    }


}

