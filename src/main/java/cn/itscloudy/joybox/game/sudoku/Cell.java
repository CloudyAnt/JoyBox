package cn.itscloudy.joybox.game.sudoku;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

class Cell extends Label {
    static final int SIDE_LEN = 30;
    private CellValue preparedValue;
    private CellValue filledValue;
    private boolean fixed;
    private static final Border EDITING_BORDER = new Border(new BorderStroke(Paint
            .valueOf("linear-gradient(from 0% 0% to 100% 100%, red  0% , blue 30%,  black 100%)"),
            BorderStrokeStyle.SOLID, null, null));

    final Group col;
    final Group row;
    final Group circle;
    Cell(ChessBoard board, Group col, Group row, Group circle) {
        this.col = col;
        this.row = row;
        this.circle = circle;

        setAlignment(Pos.CENTER);
        setPrefSize(SIDE_LEN, SIDE_LEN);
        setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, .1),
                null, null)));
        setOnMouseClicked(e -> {
            if (fixed) {
                board.setEditing(null);
            } else {
                board.setEditing(this);
                setBorder(EDITING_BORDER);
            }
        });
    }

    void reset() {
        this.preparedValue = null;
        this.fixed = false;
        setText("");
        setNoBorder();
    }

    void markFixed() {
        this.fixed = true;
        setTextFill(Color.GRAY);
        setText(preparedValue.display);
        fillGroup(preparedValue);
    }

    void fillValue(CellValue filledValue) {
        CellValue lastFillValue = this.filledValue;
        this.filledValue = filledValue;
        setTextFill(Color.LIGHTCYAN);
        setText(filledValue.display);
        if (lastFillValue != null) {
            col.getFillingRecord().removeValue(lastFillValue);
            row.getFillingRecord().removeValue(lastFillValue);
            circle.getFillingRecord().removeValue(lastFillValue);
        }
        fillGroup(filledValue);
    }

    private void fillGroup(CellValue filledValue) {
        col.getFillingRecord().addValue(filledValue);
        row.getFillingRecord().addValue(filledValue);
        circle.getFillingRecord().addValue(filledValue);
    }

    void prepareValue(CellValue actualValue) {
        this.preparedValue = actualValue;
        col.getPrepRecord().addValue(actualValue);
        row.getPrepRecord().addValue(actualValue);
        circle.getPrepRecord().addValue(actualValue);
    }

    public void setNoBorder() {
        setBorder(null);
    }

    public boolean groupPrepContains(CellValue cellValue) {
        return col.getPrepRecord().contains(cellValue)
                || row.getPrepRecord().contains(cellValue)
                || circle.getPrepRecord().contains(cellValue);
    }
}
