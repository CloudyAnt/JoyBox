package cn.itscloudy.joybox.game.sudoku;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Sudoku {
    static final int BINGO = 511;

    private Sudoku() {
    }

    public static Scene show(Runnable onClose) {
        Button closeButton = new Button("←");
        closeButton.setOnAction(e -> onClose.run());
        HBox controls = new HBox(closeButton);

        HBox splitter = new HBox(10);
        ChessBoard chessboard = new ChessBoard();

        HBox candidates = new HBox();
        for (int i = 0; i < 9; i++) {
            CandidateButton candidate = new CandidateButton(CellValue.values()[i], chessboard);
            candidates.getChildren().add(candidate);
        }

        VBox root = new VBox(controls, splitter, chessboard, candidates);
        root.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

        chessboard.prepareNewQuiz();
        return new Scene(root, 280, root.getPrefHeight(), Color.TRANSPARENT);
    }

}
