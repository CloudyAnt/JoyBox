package cn.itscloudy.joybox;

import cn.itscloudy.joybox.joys.Joy;
import cn.itscloudy.joybox.joys.JoyEntrance;
import cn.itscloudy.joybox.joys.Joys;
import cn.itscloudy.joybox.util.Draggable;
import cn.itscloudy.joybox.util.JoyConst;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

public class JoyBox extends Application {

    private Stage primaryStage;
    private Stage joyStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        Label titleLabel = new Label("JoyBox");
        titleLabel.setTextFill(Color.BLUE);
        Button closeButton = new Button("X");
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        closeButton.setOnAction(e -> System.exit(0));
        HBox controlsFloor = new HBox(titleLabel, region, closeButton);

        Runnable joyClosingAction = this::returnToPrimary;
        List<JoyEntrance<?>> entrances = Joys.getAllJoys(joyClosingAction);
        Button[] joyEntrances = new Button[entrances.size()];
        for (int i = 0; i < entrances.size(); i++) {
            JoyEntrance<?> joy = entrances.get(i);
            joyEntrances[i] = joy.getDefaultEntrance();
            joyEntrances[i].setOnAction(e -> takeJoy(joy.getJoy()));
        }

        VBox vb = new VBox();
        vb.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        vb.getChildren().add(controlsFloor);
        vb.getChildren().addAll(joyEntrances);

        Scene primaryScene = new Scene(vb, 300, 250, JoyConst.SCENE_BG);
        primaryStage.setScene(primaryScene);
        new Draggable(this.primaryStage, primaryScene);
        primaryStage.setTitle("JoyBox");
        primaryStage.show();
    }

    void takeJoy(Joy joy) {
        joy.beforeTaken();
        this.joyStage = joy.getStage();
        joyStage.setX(primaryStage.getX());
        joyStage.setY(primaryStage.getY());
        joyStage.show();
        primaryStage.hide();
        joy.afterTaken();
    }

    void returnToPrimary() {
        joyStage.close();
        primaryStage.setX(joyStage.getX());
        primaryStage.setY(joyStage.getY());
        primaryStage.show();
    }

}