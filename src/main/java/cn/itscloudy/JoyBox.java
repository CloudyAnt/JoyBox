package cn.itscloudy;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JoyBox extends Application {

    private double pressedAtX = 0;
    private double pressedAtY = 0;
    private double stageXWhenPress = 0;
    private double stageYWhenPress = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Button closeButton = new Button("X");
        closeButton.setOnAction(e -> System.exit(0));
        HBox controlsFloor = new HBox();
        controlsFloor.setAlignment(Pos.CENTER_RIGHT);
        controlsFloor.getChildren().add(closeButton);

        Button joyButton = new Button("Hey, say hello to the world!");

        Alert sayHelloWorld = new Alert(Alert.AlertType.INFORMATION);
        sayHelloWorld.setTitle("Hey!");
        sayHelloWorld.setContentText("Hello world!");
        sayHelloWorld.setHeaderText(null);
        joyButton.setOnAction(e -> sayHelloWorld.showAndWait());

        VBox vb = new VBox();
        vb.getChildren().addAll(controlsFloor, joyButton);

        Scene scene = new Scene(vb, 300, 250);
        scene.setOnMousePressed(e -> {
            pressedAtX = e.getScreenX();
            pressedAtY = e.getScreenY();
            stageXWhenPress = primaryStage.getX();
            stageYWhenPress = primaryStage.getY();
        });
        scene.setOnMouseDragged(e -> {
            primaryStage.setX(stageXWhenPress + (e.getScreenX() - pressedAtX));
            primaryStage.setY(stageYWhenPress + (e.getScreenY() - pressedAtY));
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("JoyBox");
        primaryStage.show();
    }
}