package cn.itscloudy.joybox.util;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Draggable {

    private double pressedAtX;
    private double pressedAtY;
    private double targetXWhenPress;
    private double targetYWhenPress;
    public Draggable(Stage target, Scene listener) {
        listener.setOnMousePressed(e -> {
            pressedAtX = e.getScreenX();
            pressedAtY = e.getScreenY();
            targetXWhenPress = target.getX();
            targetYWhenPress = target.getY();
        });
        listener.setOnMouseDragged(e -> {
            target.setX(targetXWhenPress + (e.getScreenX() - pressedAtX));
            target.setY(targetYWhenPress + (e.getScreenY() - pressedAtY));
        });
    }

    public Draggable(Stage target, Node listener) {
        listener.setOnMousePressed(e -> {
            pressedAtX = e.getScreenX();
            pressedAtY = e.getScreenY();
            targetXWhenPress = target.getX();
            targetYWhenPress = target.getY();
        });
        listener.setOnMouseDragged(e -> {
            target.setX(targetXWhenPress + (e.getScreenX() - pressedAtX));
            target.setY(targetYWhenPress + (e.getScreenY() - pressedAtY));
        });
    }
}
