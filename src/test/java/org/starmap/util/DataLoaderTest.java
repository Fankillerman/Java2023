package org.starmap.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.starmap.model.Constellation;
import org.starmap.model.Star;
import org.starmap.utils.DataLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataLoaderTest {

    @TempDir
    Path tempDir;
    private Path testFilePath;

    @BeforeEach
    void setUp() throws IOException {
        testFilePath = tempDir.resolve("test.json");
        String testJson = """
                {
                  "stars": [
                    {
                      "name": "Sirius",
                      "xPosition": 100,
                      "yPosition": 200,
                      "brightness": 1.46
                    },
                    {
                      "name": "Canopus",
                      "xPosition": 150,
                      "yPosition": 250,
                      "brightness": 0.72
                    },
                    {
                      "name": "Aldebaran",
                      "xPosition": 50,
                      "yPosition": 400,
                      "brightness": 0.85
                    },
                    {
                      "name": "Elnath",
                      "xPosition": 100,
                      "yPosition": 450,
                      "brightness": 1.65
                    }
                  ],
                  "constellations": [
                    {
                      "name": "Taurus",
                      "stars": [
                        "Aldebaran",
                        "Elnath"
                      ]
                    }
                  ]
                }""";
        Files.writeString(testFilePath, testJson);
    }

    @Test
    void testLoadStars() throws IOException {
        List<Star> stars = DataLoader.loadStars(testFilePath.toString());
        assertEquals(4, stars.size());
        assertTrue(stars.stream().anyMatch(star -> star.getName().equals("Sirius")));
        assertTrue(stars.stream().anyMatch(star -> star.getName().equals("Canopus")));
    }

    @Test
    void testLoadConstellations() throws IOException {
        List<Star> stars = DataLoader.loadStars(testFilePath.toString());
        List<Constellation> constellations = DataLoader.loadConstellations(testFilePath.toString(), stars);

        assertEquals(1, constellations.size());
        Constellation taurus = constellations.get(0);
        assertEquals("Taurus", taurus.getName());
        assertEquals(2, taurus.getStars().size());
        assertTrue(taurus.getStars().stream().anyMatch(star -> star.getName().equals("Aldebaran")));
        assertTrue(taurus.getStars().stream().anyMatch(star -> star.getName().equals("Elnath")));
    }
    @Test
    void testLoadStarsAttributes() throws IOException{
        List<Star> stars = DataLoader.loadStars(testFilePath.toString());
        Star sirius = stars.stream().filter(star -> star.getName().equals("Sirius")).findFirst().orElse(null);

        assertNotNull(sirius);
        assertEquals(100, sirius.getXPosition());
        assertEquals(200, sirius.getYPosition());
        assertEquals(1.46, sirius.getBrightness());
    }
    @Test
    void testConstellationStarsIntegration() throws IOException{
        List<Star> stars = DataLoader.loadStars(testFilePath.toString());
        List<Constellation> constellations = DataLoader.loadConstellations(testFilePath.toString(), stars);

        Constellation taurus = constellations.stream().filter(c -> c.getName().equals("Taurus")).findFirst().orElse(null);
        assertNotNull(taurus);

        for (Star star : taurus.getStars()) {
            assertTrue(stars.contains(star));
        }
    }
    @Test
    void testLoadStarsWithInvalidFile() throws IOException {
        assertThrows(java.nio.file.NoSuchFileException.class, () -> {
            DataLoader.loadStars("non_existing_file.json");
        });
    }



}
