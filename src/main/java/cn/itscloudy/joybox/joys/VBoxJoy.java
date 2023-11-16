package cn.itscloudy.joybox.joys;

import cn.itscloudy.joybox.util.Draggable;
import cn.itscloudy.joybox.util.JoyConst;
import cn.itscloudy.joybox.util.JoyDimension;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class VBoxJoy extends VBox implements Joy {

    private Stage stage;
    private final Runnable onClose;
    private Button closeButton;
    private HBox controls;

    public VBoxJoy(Runnable onClose) {
        this.onClose = onClose;
        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
    }

    protected HBox getControls() {
        if (controls == null) {
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            controls = new HBox(getCloseButton(), region);
        }
        return controls;
    }
    private Button getCloseButton() {
        if (closeButton == null) {
            closeButton = new Button("←");
            closeButton.setOnAction(e -> onClose.run());
        }
        return closeButton;
    }

    public Stage toStage() {
        if (stage == null) {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);

            JoyDimension dimension = getJoyDimension();
            Scene scene = new Scene(this, dimension.width(), dimension.height(), JoyConst.SCENE_BG);
            stage.setScene(scene);
            new Draggable(stage, scene);
            this.stage = stage;
        }
        return stage;
    }

    protected void updateSize() {
        JoyDimension joyDimension = getJoyDimension();
        Stage stage = toStage();
        stage.setWidth(joyDimension.width());
        stage.setHeight(joyDimension.height());
    }

    protected JoyDimension getJoyDimension() {
        return new JoyDimension(getPrefWidth(), getPrefHeight());
    }

    public void beforeTaken() {
    }

    public void afterTaken() {
    }

    public abstract String getDisplay();

    protected void addAll(Node... nodes) {
        for (Node node : nodes) {
            getChildren().add(node);
        }
    }

}
