package cn.itscloudy.joybox.game.sudoku;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

class CandidateButton extends Label {

    CandidateButton(CellValue cellValue, ChessBoard chessBoard) {
        setPrefSize(Cell.SIDE_LEN, Cell.SIDE_LEN);
        setText(cellValue.display);
        setAlignment(Pos.CENTER);
        setOnMouseClicked(e -> chessBoard.fillCellValue(cellValue));
    }
}
