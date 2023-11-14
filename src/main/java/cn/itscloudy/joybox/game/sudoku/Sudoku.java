package cn.itscloudy.joybox.game.sudoku;

import cn.itscloudy.joybox.util.JoyBoxStuffVBox;
import cn.itscloudy.joybox.util.JoyDimension;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Sudoku extends JoyBoxStuffVBox {
    static final int BINGO = 511;
    private final ChessBoard chessboard;

    public Sudoku(Runnable onClose) {
        super(onClose);
        chessboard = new ChessBoard();

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        HBox controls = new HBox(closeButton, region);

        for (DifficultyLevel value : DifficultyLevel.values()) {
            Button dlButton = new Button(value.getDisplay());
            dlButton.setOnAction(e -> {
                chessboard.setDifficultyLevel(value);
                chessboard.prepareNewQuiz();
            });
            controls.getChildren().add(dlButton);
        }

        HBox splitter = new HBox(10);

        HBox candidates = new HBox();
        for (int i = 0; i < 9; i++) {
            CandidateButton candidate = new CandidateButton(CellValue.values()[i], chessboard);
            candidates.getChildren().add(candidate);
        }

        addAll(controls, splitter, chessboard, candidates);
        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
    }

    @Override
    protected JoyDimension getJoyDimension() {
        return new JoyDimension(280, getPrefHeight());
    }

    @Override
    public void afterSeen() {
        chessboard.prepareNewQuiz();
    }
}
