package org.example.projektrocnikovy;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import javafx.scene.Cursor;

import javax.imageio.ImageIO;
import java.io.File;

public class Editor extends Application{
    private double poziceX;
    private double poziceY;
    private Color barva = Color.BLACK;


    @Override
    public void start(Stage hlavniScena) {

        RadioButton Gumovat = new RadioButton("Gumovat");
        RadioButton Kreslit = new RadioButton("Kreslit");
        RadioButton CtverecekPlny = new RadioButton("■");
        RadioButton CtverecekPrazdny = new RadioButton("□");
        ToggleGroup KoleckoCtverecek = new ToggleGroup();
        ColorPicker vyberBarev = new ColorPicker();
        Canvas canvas = new Canvas(800, 400);
        StackPane canvasDrzak = new StackPane();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Slider posuvnice = new Slider(1, 30, 10);
        Circle ukazatel = new Circle(0, 0, 5);
        StackPane ukazatelKontejner = new StackPane();
        Button ulozit = new Button("Uložit jako");
        Button smazatVse = new Button("Smazat vše");

        Image Gumovani = new Image(getClass().getResourceAsStream("/gumovat.png"));
        ImageView GumovaniZobraz = new ImageView(Gumovani);
        GumovaniZobraz.setFitHeight(20);
        GumovaniZobraz.setFitWidth(20);

        Image Kresleni = new Image(getClass().getResourceAsStream("/kreslit.png"));
        ImageView KresleniZobraz = new ImageView(Kresleni);
        KresleniZobraz.setFitHeight(20);
        KresleniZobraz.setFitWidth(20);

        Gumovat.setGraphic(GumovaniZobraz);
        Gumovat.setText("");

        Kreslit.setGraphic(KresleniZobraz);
        Kreslit.setText("");

        ukazatelKontejner.getChildren().add(ukazatel);
        ukazatelKontejner.setPrefSize(30, 30);



        Gumovat.setToggleGroup(KoleckoCtverecek);
        Kreslit.setToggleGroup(KoleckoCtverecek);
        CtverecekPlny.setToggleGroup(KoleckoCtverecek);
        CtverecekPrazdny.setToggleGroup(KoleckoCtverecek);

        canvasDrzak.getChildren().add(canvas);
        canvas.widthProperty().bind(canvasDrzak.widthProperty());
        canvas.heightProperty().bind(canvasDrzak.heightProperty());
        canvasDrzak.setStyle("-fx-background-color: white");
        ukazatel.setStyle("-fx-stroke: #817c7c; -fx-stroke-width: 1px;");
        vyberBarev.setValue(Color.BLACK);


        canvasDrzak.setCursor(Cursor.CROSSHAIR);

        Gumovat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (Gumovat.isSelected()) {
                    barva = Color.WHITE;
                    canvasDrzak.setCursor(Cursor.CROSSHAIR);
                }
            }
        });


        vyberBarev.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (Kreslit.isSelected()){
                    barva = vyberBarev.getValue();
                    canvasDrzak.setCursor(Cursor.CROSSHAIR);

                }
            }
        });
        Kreslit.setSelected(true);
        Kreslit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (Kreslit.isSelected()) {
                    barva = vyberBarev.getValue();
                    canvasDrzak.setCursor(Cursor.CROSSHAIR);
                }
            }
        });
        smazatVse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gc.clearRect(0, 0, canvas.getWidth(),   canvas.getHeight());
            }
        });
        ukazatel.setFill(barva);
        ukazatel.radiusProperty().bind(posuvnice.valueProperty().divide(2));
        ukazatel.fillProperty().bind(vyberBarev.valueProperty());


        HBox listaHorni = new HBox(ulozit, smazatVse);

        HBox listaDolni = new HBox(10, Kreslit, Gumovat, vyberBarev, posuvnice, ukazatelKontejner);
        listaDolni.setAlignment(Pos.CENTER_LEFT);

        VBox velkaLista = new VBox(listaHorni, listaDolni);

        listaDolni.setPadding(new Insets(20, 20, 20, 20));
        listaDolni.setStyle("-fx-background-color: #fafafa");
        listaDolni.setSpacing(20);

        BorderPane root = new BorderPane();
        root.setTop(velkaLista);

        root.setCenter(canvasDrzak);



        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                poziceX = e.getX();
                poziceY = e.getY();
            }
        });




        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent udalost) {

                double sirka = posuvnice.getValue();

                gc.setStroke(barva);
                gc.setLineWidth(sirka);
                gc.setLineCap(StrokeLineCap.ROUND);
                gc.strokeLine(poziceX, poziceY, udalost.getX(), udalost.getY());

                poziceX = udalost.getX();
                poziceY = udalost.getY();
            }
        });

                ulozit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Obrázek PNG", "*.png"));
                File soubor = fc.showSaveDialog(hlavniScena);

                if (soubor != null) {
                    try {
                        WritableImage snimek = canvas.snapshot(null, null);
                        ImageIO.write(SwingFXUtils.fromFXImage(snimek, null), "png", soubor);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


        Scene scene = new Scene(root, 800, 600);

        scene.getStylesheets().add(getClass().getResource("/org/example/projektrocnikovy/style.css").toExternalForm());
        hlavniScena.setTitle("Grafický editor");
        hlavniScena.setScene(scene);
        hlavniScena.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
