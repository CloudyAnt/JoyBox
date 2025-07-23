package cn.itscloudy.joybox.joys;

import cn.itscloudy.joybox.AppUtils;
import cn.itscloudy.joybox.util.Draggable;
import cn.itscloudy.joybox.util.JoyButton;
import cn.itscloudy.joybox.util.JoyConst;
import cn.itscloudy.joybox.util.JoyDimension;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

public abstract class VBoxJoy extends VBox implements Joy {

    private Stage stage;
    private Button closeButton;
    private HBox controls;

    protected VBoxJoy() {
        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        getChildren().add(getControls());
    }

    protected abstract List<Node> getRightControlNodes();

    private HBox getControls() {
        if (controls == null) {
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            controls = new HBox(getCloseButton(), region);

            List<Node> rightControlNodes = getRightControlNodes();
            controls.getChildren().addAll(rightControlNodes);
        }
        return controls;
    }

    private Button getCloseButton() {
        if (closeButton == null) {
            closeButton = new JoyButton("←");
            closeButton.setOnAction(e -> AppUtils.returnToPrimary());
        }
        return closeButton;
    }

    public Stage getStage() {
        if (stage == null) {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);

            JoyDimension dimension = getJoyDimension();
            Scene scene = new Scene(this, dimension.width(), dimension.height(), JoyConst.SCENE_BG);
            stage.setScene(scene);
            new Draggable(stage, getDraggingNode());
            this.stage = stage;
        }
        return stage;
    }

    protected Node getDraggingNode() {
        return getControls();
    }

    protected void updateSize() {
        Stage stage = getStage();
        stage.sizeToScene();
    }

    protected JoyDimension getJoyDimension() {
        return new JoyDimension(getPrefWidth(), getPrefHeight());
    }

    public void beforeTaken() {
    }

    public void afterTaken() {
    }

    protected void addAll(Node... nodes) {
        for (Node node : nodes) {
            getChildren().add(node);
        }
    }

}
