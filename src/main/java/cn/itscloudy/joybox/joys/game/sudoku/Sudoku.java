package cn.itscloudy.joybox.joys.game.sudoku;

import cn.itscloudy.joybox.joys.VBoxJoy;
import cn.itscloudy.joybox.util.JoyButton;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class Sudoku extends VBoxJoy {
    public static final String NAME = "Sudoku";
    static final int BINGO = 511;
    static final int CELL_SIDE_LEN = 30;
    private final ChessBoard chessboard;

    public Sudoku() {
        chessboard = new ChessBoard(this);
        HBox splitter = new HBox(10);

        HBox candidates = new HBox();
        for (int i = 0; i < 9; i++) {
            CandidateButton candidate = new CandidateButton(CellValue.values()[i], chessboard);
            candidates.getChildren().add(candidate);
        }

        addAll(splitter, chessboard, candidates);
    }

    @Override
    protected List<Node> getRightControlNodes() {
        ArrayList<Node> controlNodes = new ArrayList<>();
        for (DifficultyLevel value : DifficultyLevel.values()) {
            Button dlButton = new JoyButton(value.display);
            dlButton.setOnAction(e -> {
                chessboard.setDifficultyLevel(value);
                chessboard.prepareNewQuiz();
            });
            controlNodes.add(dlButton);
        }
        return controlNodes;
    }

    @Override
    public void afterTaken() {
        chessboard.prepareNewQuiz();
    }

}
