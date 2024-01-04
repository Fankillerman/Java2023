package org.starmap.view;

import javafx.animation.PauseTransition;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.starmap.controller.StarMapController;
import org.starmap.model.Constellation;
import org.starmap.model.Star;

import java.util.*;

public class StarMapView extends Canvas {
    private final StarMapController controller;
    private Star selectedStar;
    private final PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
    private Star currentHoveredStar = null;
    private final Map<String, Color> constellationColors = new HashMap<>();

    // Элементы управления
    private VBox controlPanel;
    private TextField nameField, xPositionField, yPositionField, brightnessField;

    public StarMapView(StarMapController controller) {
        this.controller = controller;
        this.setWidth(1024);
        this.setHeight(768);
        initializeUI();
        drawMap();
        initializeConstellationColors();
        addMouseMotionListener();
        addMouseHandlers();
    }

    private void initializeUI() {
        controlPanel = new VBox(10);

        Label nameLabel = new Label("Name:");
        nameField = new TextField();
        HBox nameBox = new HBox(5, nameLabel, nameField);

        Label xPositionLabel = new Label("X Position:");
        xPositionField = new TextField();
        HBox xPositionBox = new HBox(5, xPositionLabel, xPositionField);

        Label yPositionLabel = new Label("Y Position:");
        yPositionField = new TextField();
        HBox yPositionBox = new HBox(5, yPositionLabel, yPositionField);

        Label brightnessLabel = new Label("Brightness:");
        brightnessField = new TextField();
        HBox brightnessBox = new HBox(5, brightnessLabel, brightnessField);

        Button addButton = new Button("Add Star");
        addButton.setOnAction(e -> addStar());

        Button editButton = new Button("Edit Star");
        editButton.setOnAction(e -> editStar());

        Button removeButton = new Button("Remove Star");
        removeButton.setOnAction(e -> removeStar());

        controlPanel.getChildren().addAll(nameBox, xPositionBox, yPositionBox, brightnessBox, addButton, editButton, removeButton);
    }

    private void addStar() {
        Dialog<Star> dialog = new Dialog<>();
        dialog.setTitle("Add a star");
        dialog.setHeaderText("Enter the star data");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField name = new TextField();
        name.setPromptText("Name");
        TextField xPosition = new TextField();
        xPosition.setPromptText("X Position");
        TextField yPosition = new TextField();
        yPosition.setPromptText("Y Position");
        TextField brightness = new TextField();
        brightness.setPromptText("Brightness");
        ComboBox<String> constellation = new ComboBox<>();
        loadConstellationsToComboBox(constellation);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("X Position:"), 0, 1);
        grid.add(xPosition, 1, 1);
        grid.add(new Label("Y Position:"), 0, 2);
        grid.add(yPosition, 1, 2);
        grid.add(new Label("Яркость:"), 0, 3);
        grid.add(brightness, 1, 3);
        grid.add(new Label("Constellation:"), 0, 4);
        grid.add(constellation, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    Star newStar = new Star(
                            name.getText(),
                            Double.parseDouble(xPosition.getText().replace(',', '.')),
                            Double.parseDouble(yPosition.getText().replace(',', '.')),
                            Double.parseDouble(brightness.getText().replace(',', '.'))
                    );

                    String selectedConstellationName = constellation.getValue();
                    Constellation selectedConstellation = findConstellationByName(selectedConstellationName);
                    if (selectedConstellation != null) {
                        selectedConstellation.getStars().add(newStar);
                    }

                    controller.addStar(newStar);
                    drawMap();
                    return newStar;
                } catch (NumberFormatException e) {
                    showAlert("Input error", "Incorrect input", "Please enter the correct numeric values.");
                    return null;
                }
            }
            return null;
        });

        Optional<Star> result = dialog.showAndWait();
        result.ifPresent(star -> {
            drawMap();
            dialog.close();
        });
    }

    private void editStar() {
        if (selectedStar == null) {
            showAlert("Error", "Star not selected", "Please select a star before editing.");
            return;
        }

        try {
            System.out.println("Editing star: " + nameField.getText());
            System.out.println("X position: " + xPositionField.getText());
            System.out.println("Y position: " + yPositionField.getText());
            System.out.println("Brightness: " + brightnessField.getText());

            String newName = nameField.getText();
            double newXPosition = parseDouble(xPositionField.getText());
            double newYPosition = parseDouble(yPositionField.getText());
            double newBrightness = parseDouble(brightnessField.getText());

            selectedStar.setName(newName);
            selectedStar.setXPosition(newXPosition);
            selectedStar.setYPosition(newYPosition);
            selectedStar.setBrightness(newBrightness);

            drawMap();
        } catch (NumberFormatException e) {
            System.out.println("Error : " + e.getMessage());
            showAlert("Input error", "Incorrect input", "Please enter the correct numeric values.");
        }
    }

    private void loadConstellationsToComboBox(ComboBox<String> comboBox) {
        List<Constellation> constellations = controller.getConstellations();
        for (Constellation constellation : constellations) {
            comboBox.getItems().add(constellation.getName());
        }
    }

    private double parseDouble(String text) throws NumberFormatException {
        if (text == null || text.trim().isEmpty()) {
            throw new NumberFormatException("Text field is empty");
        }
        text = text.replace(',', '.');
        return Double.parseDouble(text);
    }


    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void removeStar() {
        String name = nameField.getText();
        controller.removeStar(name);
        removeFromConstellations(name);
        clearFields();
        drawMap();
    }

    private void removeFromConstellations(String starName) {
        for (Constellation constellation : controller.getConstellations()) {
            constellation.getStars().removeIf(star -> star.getName().equals(starName));
        }
    }

    private void addMouseHandlers() {
        setOnMousePressed(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();
            selectedStar = findStarAt(mouseX, mouseY);
            if (selectedStar != null) {
                fillFieldsWithStarData(selectedStar);
            } else {
                clearFields();
            }
        });

        setOnMouseDragged(event -> {
            if (selectedStar != null) {
                selectedStar.setXPosition(event.getX());
                selectedStar.setYPosition(event.getY());
                updateFieldsWithStarData(selectedStar);
                drawMap();
            }
        });

    }

    private void updateFieldsWithStarData(Star star) {
        xPositionField.setText(String.format("%.2f", star.getXPosition()));
        yPositionField.setText(String.format("%.2f", star.getYPosition()));
    }

    private void clearFields() {
        nameField.clear();
        xPositionField.clear();
        yPositionField.clear();
        brightnessField.clear();
        selectedStar = null;
    }

    private Star findStarAt(double x, double y) {
        for (Star star : controller.getStars()) {
            if (Math.hypot(star.getXPosition() - x, star.getYPosition() - y) < 10) {
                System.out.println("Star selected: " + star.getName());
                return star;
            }
        }
        return null;
    }

    private Constellation findConstellationByName(String name) {
        for (Constellation constellation : controller.getConstellations()) {
            if (constellation.getName().equals(name)) {
                return constellation;
            }
        }
        return null;
    }

    private void fillFieldsWithStarData(Star star) {
        nameField.setText(star.getName());
        xPositionField.setText(String.valueOf(star.getXPosition()));
        yPositionField.setText(String.valueOf(star.getYPosition()));
        brightnessField.setText(String.valueOf(star.getBrightness()));
    }

    public VBox getControlPanel() {
        return controlPanel;
    }

    private void initializeConstellationColors() {
        List<Constellation> constellations = controller.getConstellations();
        for (Constellation constellation : constellations) {
            int hash = constellation.getName().hashCode();
            Random rand = new Random(hash);
            Color color = new Color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), 1);
            constellationColors.put(constellation.getName(), color);
        }
    }

    public void drawMap() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getWidth(), getHeight());
        drawStars();
        drawConstellations();
        drawCoordinateAxes();
    }

    private void drawStars() {
        GraphicsContext gc = getGraphicsContext2D();
        List<Star> stars = controller.getStars();
        for (Star star : stars) {
            double brightnessScale = star.getBrightness() / 2.0; // Scale brightness
            double starSize = 2 + (5 - brightnessScale); // Calculate star size
            Color starColor = Color.hsb(60, 0.5, 1 - 0.2 * brightnessScale); // Color based on brightness
            drawStar(gc, star.getXPosition(), star.getYPosition(), starSize, starColor);
        }
    }

    private void drawStar(GraphicsContext gc, double x, double y, double size, Color color) {
        double[] xPoints = new double[10];
        double[] yPoints = new double[10];
        for (int i = 0; i < 10; i++) {
            double angle = Math.PI / 5 * i;
            double radius = i % 2 == 0 ? size : size / 2;
            xPoints[i] = x + radius * Math.sin(angle);
            yPoints[i] = y - radius * Math.cos(angle);
        }
        gc.setStroke(color);
        gc.strokePolyline(xPoints, yPoints, 10);
    }

    private void drawConstellations() {
        GraphicsContext gc = getGraphicsContext2D();
        for (Constellation constellation : controller.getConstellations()) {
            Color lineColor = constellationColors.getOrDefault(constellation.getName(), Color.BLUE);
            gc.setStroke(lineColor);
            gc.setLineWidth(1);

            List<Star> stars = constellation.getStars();
            for (int i = 0; i < stars.size() - 1; i++) {
                Star start = stars.get(i);
                Star end = stars.get(i + 1);
                gc.strokeLine(start.getXPosition(), start.getYPosition(), end.getXPosition(), end.getYPosition());
            }
        }
    }

    private void drawCoordinateAxes() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setStroke(Color.GRAY);
        gc.setFill(Color.GRAY);
        gc.setLineWidth(1);

        // Рисование оси X
        gc.strokeLine(0, 0, getWidth(), 0);

        // Рисование оси Y
        gc.strokeLine(0, 0, 0, getHeight());

        // Добавление меток на оси
        int step = 50; // Шаг между метками
        for (int i = 0; i < getWidth(); i += step) {
            // Метки на оси X
            gc.strokeLine(i, -5, i, 5);
            gc.fillText(String.valueOf(i), i - 10, 15);
        }
        for (int i = 0; i < getHeight(); i += step) {
            // Метки на оси Y
            gc.strokeLine(-5, i, 5, i);
            gc.fillText(String.valueOf(i), 10, i + 5);
        }
    }


    private void addMouseMotionListener() {
        this.setOnMouseMoved(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();
            Star foundStar = null;

            List<Star> stars = controller.getStars();
            for (Star star : stars) {
                if (Math.abs(mouseX - star.getXPosition()) < 10 && Math.abs(mouseY - star.getYPosition()) < 10) {
                    foundStar = star;
                    break;
                }
            }

            if (foundStar != null && foundStar != currentHoveredStar) {
                currentHoveredStar = foundStar;
                pause.stop();
                drawStarName(foundStar);
            } else if (foundStar == null && currentHoveredStar != null) {
                pause.setOnFinished(e -> {
                    hideStarName();
                    currentHoveredStar = null;
                });
                pause.playFromStart();
            }
        });
    }

    private void drawStarName(Star star) {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillText(star.getName(), star.getXPosition() + 10, star.getYPosition() - 10);
    }

    private void hideStarName() {
        if (currentHoveredStar != null) {
            pause.setOnFinished(e -> {
                clearCanvas();
                drawMap();
            });
            pause.playFromStart();
        }
    }

    private void clearCanvas() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
    }
}
