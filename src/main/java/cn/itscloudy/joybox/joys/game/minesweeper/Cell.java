package cn.itscloudy.joybox.joys.game.minesweeper;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class Cell extends Label {
    private static final Background DEF_BG = new Background(new BackgroundFill(
            new Color(0, 0, 0, .1), null, null));
    private static final Background DUG_BG = new Background(new BackgroundFill(
            new Color(1, 1, 1, .1), null, null));
    private final MineField field;
    private boolean hasMine = false;
    private boolean starter;
    private boolean signed;
    private int row;
    private int col;
    private int nearbyMinesCount;
    private State state;

    private static final EventHandler<MouseEvent> PRESSING_HANDLER = event -> {
        Cell cell = (Cell) event.getSource();
        cell.afterPressed(event);
    };
    private static final EventHandler<MouseEvent> RELEASING_HANDLER = event -> {
        Cell cell = (Cell) event.getSource();
        cell.afterReleased(event);
    };

    private static final double INDICATOR_TRANS = .5;
    private static final Color[] INDICATOR_COLORS = new Color[]{
            new Color(0, 0, 1, INDICATOR_TRANS),
            new Color(0, 1, 0, INDICATOR_TRANS),
            new Color(1, 0, 0, INDICATOR_TRANS),
            new Color(1, 0, 1, INDICATOR_TRANS),
            new Color(1, 1, 0, INDICATOR_TRANS),
            new Color(0, 1, 1, INDICATOR_TRANS),
            new Color(0, .482, 1, INDICATOR_TRANS),
            new Color(1, .482, 0, INDICATOR_TRANS),
    };

    Cell(MineField field, int row, int col) {
        this.field = field;
        setData(row, col);
        setAlignment(Pos.CENTER);
        setBackground(DEF_BG);
        setOnMousePressed(PRESSING_HANDLER);
        setOnMouseReleased(RELEASING_HANDLER);
        setPrefSize(MineSweeper.CELL_SIDE_LEN, MineSweeper.CELL_SIDE_LEN);
    }

    void setData(int row, int col) {
        this.row = row;
        this.col = col;
        nearbyMinesCount = 0;

        hasMine = false;
        starter = false;
        signed = false;
        state = State.VIRGIN;

        setText("");
        setTextFill(Color.BLACK);
        setBackground(DEF_BG);
    }

    int getRow() {
        return row;
    }

    int getCol() {
        return col;
    }

    boolean isSigned() {
        return signed;
    }

    boolean isStarter() {
        return starter;
    }

    boolean hasMine() {
        return hasMine;
    }

    void layMine() {
        hasMine = true;
    }

    private void afterPressed(MouseEvent event) {
        if (field.isSweeping() && event.getButton() == MouseButton.PRIMARY && isVirgin()) {
            dig();
        } else if (field.isIdling()) {
            starter = true;
            field.layMines();
            dig();
        }
    }

    private void dig() {
        if (isDug()) {
            return;
        }
        if (hasMine) {
            explode();
            return;
        }

        field.trackFrom(this);
    }

    private void explode() {
        setTextFill(Color.RED);
        setText("O");
        hasMine = false;
        field.reportMine();
    }

    private void afterReleased(MouseEvent event) {
        if (field.isSweeping()) {
            MouseButton button = event.getButton();
            if (button != MouseButton.PRIMARY) {
                switchSignState();
            }
        }
        // start the game
        else if (field.isIdling()) {
            field.startSweeping();
        }
    }

    boolean isVirgin() {
        return state == State.VIRGIN;
    }

    boolean isDug() {
        return state == State.DUG;
    }

    void switchSignState() {
        signed = !signed;
        if (signed) {
            field.addSignedCellsCount();
        } else {
            field.minusSignedCellsCount();
        }
    }

    void removeSignState() {
        if (signed) {
            signed = false;
            field.addSignedCellsCount();
        }
    }

    void markDug() {
        state = State.DUG;
        setBackground(DUG_BG);
    }

    void markIndicated() {
        state = State.INDICATED;
    }

    void setNearbyMinesCount(int nearbyMinesCount) {
        this.nearbyMinesCount = nearbyMinesCount;
    }

    void setIndicatorNumber() {
        if (nearbyMinesCount == 0) {
            return;
        }
        setText(String.valueOf(nearbyMinesCount));
        setTextFill(INDICATOR_COLORS[nearbyMinesCount - 1]);
    }

    void detonate() {
        if (hasMine) {
            setText("X");
        }
    }

    void sweep() {
        if (hasMine) {
            setTextFill(Color.GREEN);
            setText("√");
        }
    }

    private enum State {
        VIRGIN,
        DUG,
        INDICATED
    }
}
