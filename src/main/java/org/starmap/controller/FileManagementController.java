package org.starmap.controller;

import org.starmap.model.Constellation;
import org.starmap.model.Star;
import org.starmap.utils.DataLoader;
import org.starmap.utils.DataWriter;

import java.io.IOException;
import java.util.List;

public class FileManagementController {
    private final DataWriter dataWriter;

    public FileManagementController() {
        this.dataWriter = new DataWriter();
    }

    public List<Star> loadStars(String filePath) throws IOException {
        return DataLoader.loadStars(filePath);
    }

    public List<Constellation> loadConstellations(String filePath, List<Star> stars) throws IOException {
        return DataLoader.loadConstellations(filePath, stars);
    }

    public void saveData(List<Star> stars, List<Constellation> constellations, String filename) throws IOException {
        dataWriter.saveData(stars, constellations, filename);
    }
}
