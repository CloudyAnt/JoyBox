package cn.itscloudy.joybox.joys.game.sudoku;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

class CandidateButton extends Label {

    CandidateButton(CellValue cellValue, ChessBoard chessBoard) {
        setPrefSize(Sudoku.CELL_SIDE_LEN, Sudoku.CELL_SIDE_LEN);
        setText(cellValue.display);
        setAlignment(Pos.CENTER);
        setOnMouseClicked(e -> chessBoard.fillCellValue(cellValue));
    }
}
