package cn.itscloudy.joybox.util;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public abstract class JoyBoxStuffVBox extends VBox {


    protected Button closeButton;

    public JoyBoxStuffVBox(Runnable onClose) {
        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

        closeButton = new Button("←");
        closeButton.setOnAction(e -> onClose.run());
    }

    public Scene toScene() {
        JoyDimension joyDimension = getJoyDimension();
        return new Scene(this, joyDimension.width(), joyDimension.height(), JoyConst.SCENE_BG);
    }

    protected abstract JoyDimension getJoyDimension();

    public abstract void afterSeen();

    protected void addAll(Node... nodes) {
        for (Node node : nodes) {
            getChildren().add(node);
        }
    }

}
