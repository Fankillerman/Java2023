package org.starmap.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.starmap.model.Star;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class StarMapControllerTest {
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
    void testAddStar() throws IOException {
        StarMapController controller = new StarMapController(testFilePath.toString());
        Star testStar = new Star("TestStar", 100, 100, 1.0);
        controller.addStar(testStar);

        assertTrue(controller.getStars().contains(testStar));
    }

    @Test
    void testRemoveStar() throws IOException {
        StarMapController controller = new StarMapController(testFilePath.toString());
        Star testStar = new Star("TestStar", 100, 100, 1.0);
        controller.addStar(testStar);
        controller.removeStar("TestStar");

        assertFalse(controller.getStars().contains(testStar));
    }

    @Test
    void testLoadData() throws IOException {
        StarMapController controller = new StarMapController(testFilePath.toString());

        assertFalse(controller.getStars().isEmpty());
        assertFalse(controller.getConstellations().isEmpty());
    }

    @Test
    void testLoadDataWithInvalidFile() {
        assertThrows(IOException.class, () -> {
            new StarMapController("non_existing_file.json");
        });
    }

}
