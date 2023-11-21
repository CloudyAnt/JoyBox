package cn.itscloudy.joybox.joys.game.sudoku;

import cn.itscloudy.joybox.joys.VBoxJoy;
import cn.itscloudy.joybox.util.JoyButton;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class Sudoku extends VBoxJoy {
    public static final String NAME = "Sudoku";
    static final int BINGO = 511;
    static final int CELL_SIDE_LEN = 30;
    private final ChessBoard chessboard;

    public Sudoku(Runnable onClose) {
        super(onClose);
        chessboard = new ChessBoard(this);

        HBox controls = getControls();
        for (DifficultyLevel value : DifficultyLevel.values()) {
            Button dlButton = new JoyButton(value.display);
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
    }

    @Override
    public void afterTaken() {
        chessboard.prepareNewQuiz();
    }

}
