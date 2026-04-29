package org.example.projektrocnikovy; // Make sure this matches your project's package name

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Projekt extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Create the Drawing Area
        Pane drawingPane = new Pane();
        drawingPane.setStyle("-fx-background-color: white;");

        // 2. Create Tools for the Toolbar
        ToggleGroup shapeGroup = new ToggleGroup();

        RadioButton rbRect = new RadioButton("Rectangle");
        rbRect.setToggleGroup(shapeGroup);
        rbRect.setSelected(true); // Rectangle is selected by default

        RadioButton rbCircle = new RadioButton("Circle");
        rbCircle.setToggleGroup(shapeGroup);

        ColorPicker colorPicker = new ColorPicker(Color.ROYALBLUE);

        Button btnClear = new Button("Clear");
        Button btnSave = new Button("Save as PNG");

        // 3. Arrange Tools in a Layout
        HBox toolbar = new HBox(15, new Label("Shape:"), rbRect, rbCircle, colorPicker, btnClear, btnSave);
        toolbar.setPadding(new Insets(10));
        toolbar.setStyle("-fx-background-color: #e0e0e0;");

        // 4. Handle Clicking to Draw
        drawingPane.setOnMouseClicked(e -> {
            double x = e.getX();
            double y = e.getY();
            Color color = colorPicker.getValue();

            if (rbRect.isSelected()) {
                // Center the 50x50 rectangle on the mouse click
                Rectangle rect = new Rectangle(x - 25, y - 25, 50, 50);
                rect.setFill(color);
                drawingPane.getChildren().add(rect);
            } else if (rbCircle.isSelected()) {
                // Center a radius-25 circle on the mouse click
                Circle circle = new Circle(x, y, 25);
                circle.setFill(color);
                drawingPane.getChildren().add(circle);
            }
        });

        // 5. Handle Clearing the Canvas
        btnClear.setOnAction(e -> drawingPane.getChildren().clear());

        // 6. Handle Saving the Image
        btnSave.setOnAction(e -> saveImage(primaryStage, drawingPane));

        // 7. Put everything together in the Main Window
        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(drawingPane);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("JavaFX Simple Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveImage(Stage stage, Pane pane) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));

        // Open the save dialog
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                // Take a snapshot of the current state of the drawing pane
                WritableImage image = pane.snapshot(new SnapshotParameters(), null);

                // Convert the JavaFX Image to a standard Java format and write it to disk
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save image: " + ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}