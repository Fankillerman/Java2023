package org.starmap.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.starmap.model.Constellation;
import org.starmap.model.Star;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DataWriter {
    public void saveData(List<Star> stars, List<Constellation> constellations, String filename) throws IOException {
        JSONObject json = new JSONObject();
        JSONArray starsJson = new JSONArray();
        JSONArray constellationsJson = new JSONArray();

        for (Star star : stars) {
            JSONObject starJson = new JSONObject();
            starJson.put("name", star.getName());
            starJson.put("xPosition", star.getXPosition());
            starJson.put("yPosition", star.getYPosition());
            starJson.put("brightness", star.getBrightness());
            starsJson.put(starJson);
        }

        for (Constellation constellation : constellations) {
            JSONObject constellationJson = new JSONObject();
            constellationJson.put("name", constellation.getName());
            JSONArray starNames = new JSONArray();
            for (Star star : constellation.getStars()) {
                starNames.put(star.getName());
            }
            constellationJson.put("stars", starNames);
            constellationsJson.put(constellationJson);
        }

        json.put("stars", starsJson);
        json.put("constellations", constellationsJson);

        try (FileWriter file = new FileWriter(filename)) {
            file.write(json.toString(4));
        }
    }
}
