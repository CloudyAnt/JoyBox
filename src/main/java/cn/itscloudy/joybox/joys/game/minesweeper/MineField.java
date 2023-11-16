package cn.itscloudy.joybox.joys.game.minesweeper;

import cn.itscloudy.joybox.joys.game.GameHost;
import cn.itscloudy.joybox.log.LogType;
import cn.itscloudy.joybox.log.Logger;
import cn.itscloudy.joybox.util.JoyConst;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class MineField extends GridPane implements GameHost {
    private static final Logger LOGGER = LogType.MINESWEEPER.getLogger();
    private final List<Cell> availableCells = new ArrayList<>();
    private final MineSweeper mineSweeper;
    private Level level;
    private int laidMinesCount = 0;
    private State state = State.IDLING;
    // in sweeping variables
    private int signedCellsCount;
    private int leftCellsCount;
    private final List<Cell> traces = new ArrayList<>();
    private int badRandomTimes;

    MineField(MineSweeper mineSweeper) {
        this.mineSweeper = mineSweeper;
        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        setVgap(1);
        setHgap(1);
    }

    void setLevelAndPrepare(Level level) {
        Level lastLevel = this.level;
        this.level = level;
        int colsNum = level.colsNum;
        int rowsNum = level.rowsNum;
        double width = colsNum * MineSweeper.CELL_SIDE_LEN + (colsNum + 1) * getHgap();
        double height = rowsNum * MineSweeper.CELL_SIDE_LEN + (rowsNum + 1) * getVgap();
        setPrefSize(width, height);

        if (lastLevel != level) {
            mineSweeper.afterLevelChanged();
            getChildren().removeAll(getChildren());
            arrangeCells(this::initCellAtIndex);
        } else {
            arrangeCells(((index, row, col) -> availableCells.get(index).setData(row, col)));
        }
        signedCellsCount = 0;
        setVisible(false);
        setVisible(true);
        state = State.IDLING;
        laidMinesCount = 0;
        signedCellsCount = 0;
        leftCellsCount = level.totalCells;
        traces.clear();
    }

    private void arrangeCells(CellsArranger arranger) {
        int index = 0;
        for (int row = 0; row < level.rowsNum; row++) {
            for (int col = 0; col < level.colsNum; col++) {
                arranger.arrange(index, row, col);
                index++;
            }
        }
    }

    private void initCellAtIndex(int index, int row, int col) {
        Cell cell;
        if (index < availableCells.size()) {
            cell = availableCells.get(index);
            cell.setData(row, col);
        } else {
            cell = new Cell(this, row, col);
            availableCells.add(cell);
        }
        add(cell, col, row);
    }

    void layMines() {
        laidMinesCount = 0;
        leftCellsCount = level.totalCells;

        badRandomTimes = 0;
        layNextMine();
        LOGGER.info("%s level mines laid. bad random times: %d", level, badRandomTimes);
    }

    private void layNextMine() {
        if (laidMinesCount == level.minesNum) {
            return;
        } else {
            int row = JoyConst.RANDOM.nextInt(level.rowsNum);
            int col = JoyConst.RANDOM.nextInt(level.colsNum);
            Cell cell = getCell(row, col);
            if (!cell.hasMine() && !cell.isStarter()) {
                cell.layMine();
                laidMinesCount++;
            } else {
                badRandomTimes++;
            }
        }
        layNextMine();
    }

    Cell getCell(int row, int col) {
        if (row < 0 || col < 0 || row >= level.rowsNum || col >= level.colsNum) {
            return null;
        }
        int index = level.colsNum * row + col;
        return availableCells.get(index);
    }

    boolean isIdling() {
        return state == State.IDLING;
    }

    boolean isSweeping() {
        return state == State.SWEEPING;
    }

    void startSweeping() {
        state = State.SWEEPING;
        // start timing
    }

    void gameOver(boolean success) {
        // stop timing
        state = State.FINISHED;

        if (success) {
            showSuccessAlert("Good job!", "You sweep out all mines");
        }
    }

    void reportMine() {
        digAllCell(Cell::detonate);
        gameOver(false);
    }

    void trackFrom(Cell point) {
        traces.clear();
        track(point);

        // show tracing/show digging result
        traces.forEach(cell -> {
            cell.setIndicatorNumber();
            cell.markIndicated();
        });

        // check rest cells
        leftCellsCount -= traces.size();
        if (leftCellsCount == level.minesNum) {
            digAllCell(Cell::sweep);
            gameOver(true);
        }
    }

    void digAllCell(Consumer<Cell> digger) {
        for (int i = 0; i < level.totalCells; i++) {
            digger.accept(availableCells.get(i));
        }
    }

    private void track(Cell point) {
        if (point.isVirgin()) {
            point.markDug();
            traces.add(point);
            point.removeSignState();
            detectAround(point);
        }
    }

    private void detectAround(Cell point) {
        int row = point.getRow();
        int col = point.getCol();
        int nearbyMinesCount = countMinesNear(row, col);
        point.setNearbyMinesCount(nearbyMinesCount);

        // open nearby lands
        if (nearbyMinesCount == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    Cell cell = getCell(row + i, col + j);
                    if (cell != null) {
                        track(cell);
                    }
                }
            }
        }
    }

    private int countMinesNear(int row, int col) {
        int nearbyMinesCount = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Cell cell = getCell(row + i, col + j);
                if (cell != null && cell.hasMine()) {
                    nearbyMinesCount++;
                }
            }
        }
        return nearbyMinesCount;
    }

    void addSignedCellsCount() {
        signedCellsCount -= 1;
    }

    void minusSignedCellsCount() {
        signedCellsCount += 1;
    }

    @Override
    public Stage getOwner() {
        return mineSweeper.toStage();
    }

    enum State {
        IDLING,
        SWEEPING,
        FINISHED
    }

    interface CellsArranger {
        void arrange(int index, int row, int col);
    }
}
