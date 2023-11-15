package cn.itscloudy.joybox;

import cn.itscloudy.joybox.util.JoyConst;
import cn.itscloudy.joybox.game.sudoku.Sudoku;
import cn.itscloudy.joybox.util.Draggable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JoyBox extends Application {

    private Stage primaryStage;
    private Scene primaryScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        Label titleLabel = new Label("JoyBox");
        titleLabel.setTextFill(Color.BLUE);
        Button closeButton = new Button("X");
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        closeButton.setOnAction(e -> System.exit(0));
        HBox controlsFloor = new HBox(titleLabel, region, closeButton);

        Button sudokuButton = new Button("Sudoku");
        Runnable stuffClosingAction = () -> setScene(primaryScene);
        sudokuButton.setOnAction(e -> {
            Sudoku sudoku = new Sudoku(stuffClosingAction);
            setScene(sudoku.toScene());
            sudoku.afterSeen();
        });

        VBox vb = new VBox();
        vb.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        vb.getChildren().addAll(controlsFloor, sudokuButton);

        this.primaryScene = new Scene(vb, 300, 250, JoyConst.SCENE_BG);
        setScene(primaryScene);
        primaryStage.setTitle("JoyBox");
        primaryStage.show();
    }

    void setScene(Scene scene) {
        primaryStage.setScene(scene);
        new Draggable(primaryStage, scene);
    }
}