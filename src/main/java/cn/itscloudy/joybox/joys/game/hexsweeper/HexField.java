package cn.itscloudy.joybox.joys.game.hexsweeper;

import cn.itscloudy.joybox.util.JoyConst;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

class HexField extends Pane {
    private static final double HEX_SIZE = 18;
    private static final double HEX_WIDTH = Math.sqrt(3) * HEX_SIZE;
    private static final double HEX_HEIGHT = 2 * HEX_SIZE;
    private static final double VERT_SPACING = HEX_HEIGHT * 0.75;
    private static final double HORZ_SPACING = HEX_WIDTH;

    private final HexSweeper hexSweeper;
    private final List<HexCell> cells = new ArrayList<>();
    private HexLevel level;
    private State state = State.IDLING;
    private int laidMinesCount = 0;
    private int leftCellsCount;
    private final List<HexCell> traces = new ArrayList<>();

    HexField(HexSweeper hexSweeper) {
        this.hexSweeper = hexSweeper;
        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
    }

    void setLevelAndPrepare(HexLevel level) {
        HexLevel lastLevel = this.level;
        this.level = level;
        double width = 2 * level.radius * HEX_WIDTH + HEX_HEIGHT;
        double height = (2 * level.radius + 1) * VERT_SPACING + HEX_HEIGHT;
        setPrefSize(width, height);

        if (lastLevel != level) {
            hexSweeper.afterLevelChanged();
            getChildren().removeAll(getChildren());
            initCells();
        } else {
            cells.forEach(HexCell::reset);
        }
        setVisible(false);
        setVisible(true);
        state = State.IDLING;
        laidMinesCount = 0;
        leftCellsCount = level.totalCells;
        traces.clear();
    }

    private void initCells() {
        cells.clear();
        for (int q = -level.radius; q <= level.radius; q++) {
            int r1 = Math.max(-level.radius, -q - level.radius);
            int r2 = Math.min(level.radius, -q + level.radius);
            for (int r = r1; r <= r2; r++) {
                HexCoord coord = new HexCoord(q, r);
                HexCell cell = new HexCell(this);
                cell.setCoord(coord);
                positionCell(cell, coord);
                getChildren().add(cell);
                cells.add(cell);
            }
        }
    }

    private void positionCell(HexCell cell, HexCoord coord) {
        double x = (coord.q() + level.radius) * HORZ_SPACING + coord.r() * HEX_WIDTH / 2;
        double y = (coord.r() + level.radius) * VERT_SPACING + HEX_SIZE;
        cell.setLayoutX(x);
        cell.setLayoutY(y);
    }

    void layMines() {
        laidMinesCount = 0;
        leftCellsCount = level.totalCells;
        cells.forEach(HexCell::clearMine);

        List<Integer> indices = getShuffledIndices();
        for (Integer i : indices) {
            HexCell cell = cells.get(i);
            if (!cell.isStarter()) {
                cell.layMine();
                laidMinesCount++;
            }
            if (laidMinesCount == level.minesNum) {
                break;
            }
        }
    }

    private List<Integer> getShuffledIndices() {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < cells.size(); i++) {
            indices.add(i);
        }
        Collections.shuffle(indices, JoyConst.RANDOM);
        return indices;
    }

    HexCell getCell(HexCoord coord) {
        return cells.stream()
                .filter(c -> c.getCoord().equals(coord))
                .findFirst()
                .orElse(null);
    }

    List<HexCell> getNeighbors(HexCoord coord) {
        List<HexCell> neighbors = new ArrayList<>();
        for (HexCoord neighborCoord : coord.neighbors()) {
            HexCell neighbor = getCell(neighborCoord);
            if (neighbor != null) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    boolean isIdling() {
        return state == State.IDLING;
    }

    boolean isSweeping() {
        return state == State.SWEEPING;
    }

    void startSweeping() {
        state = State.SWEEPING;
    }

    void gameOver(boolean success) {
        state = State.FINISHED;
        if (success) {
            hexSweeper.showAlert("Good job!", "You swept out all mines");
        }
    }

    void reportMine() {
        digAllCell(HexCell::detonate);
        gameOver(false);
    }

    void trackFrom(HexCell point) {
        traces.clear();
        track(point);

        traces.forEach(cell -> {
            cell.setIndicatorNumber();
            cell.markDug();
        });

        leftCellsCount -= traces.size();
        if (leftCellsCount == level.minesNum) {
            digAllCell(HexCell::sweep);
            gameOver(true);
        }
    }

    void digAllCell(Consumer<HexCell> digger) {
        cells.forEach(digger);
    }

    private void track(HexCell point) {
        if (point.isVirgin()) {
            point.markDug();
            traces.add(point);
            point.removeSignState();
            detectAround(point);
        }
    }

    private void detectAround(HexCell point) {
        HexCoord coord = point.getCoord();
        if (coord == null) {
            return;
        }

        if (point.hasMine()) {
            point.explode();
            return;
        }

        int nearbyMinesCount = countMinesNear(coord);
        point.setNearbyMinesCount(nearbyMinesCount);

        if (nearbyMinesCount == 0) {
            for (HexCoord neighborCoord : coord.neighbors()) {
                HexCell neighbor = getCell(neighborCoord);
                if (neighbor != null) {
                    track(neighbor);
                }
            }
        }
    }

    private int countMinesNear(HexCoord coord) {
        int count = 0;
        for (HexCoord neighborCoord : coord.neighbors()) {
            HexCell neighbor = getCell(neighborCoord);
            if (neighbor != null && neighbor.hasMine()) {
                count++;
            }
        }
        return count;
    }

    enum State {
        IDLING,
        SWEEPING,
        FINISHED
    }
}