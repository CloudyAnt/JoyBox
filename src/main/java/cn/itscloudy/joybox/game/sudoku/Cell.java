package cn.itscloudy.joybox.game.sudoku;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

class Cell extends Label {
    static final int SIDE_LEN = 30;
    private CellValue preparedValue;
    private CellValue filledValue;
    private boolean fixed;
    private static final Border EDITING_BORDER = new Border(new BorderStroke(Color.DARKBLUE,
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

    void fix() {
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
            col.recalculateFillingRecord();
            row.recalculateFillingRecord();
            circle.recalculateFillingRecord();
        } else {
            fillGroup(filledValue);
        }
    }

    private void fillGroup(CellValue filledValue) {
        col.getFillingRecord().addValue(filledValue);
        row.getFillingRecord().addValue(filledValue);
        circle.getFillingRecord().addValue(filledValue);
    }

    void prepareValue(CellValue prepValue) {
        this.preparedValue = prepValue;
        col.getPrepRecord().addValue(prepValue);
        row.getPrepRecord().addValue(prepValue);
        circle.getPrepRecord().addValue(prepValue);
    }

    void setNoBorder() {
        setBorder(null);
    }

    boolean groupPrepContains(CellValue cellValue) {
        return col.getPrepRecord().contains(cellValue)
                || row.getPrepRecord().contains(cellValue)
                || circle.getPrepRecord().contains(cellValue);
    }

    CellValue getDisplayingValue() {
        if (fixed) {
            return preparedValue;
        } else {
            return filledValue;
        }
    }
}
