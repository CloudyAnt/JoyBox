package cn.itscloudy.joybox.joys.game.sudoku;

import cn.itscloudy.joybox.joys.game.GameHost;
import cn.itscloudy.joybox.log.LogType;
import cn.itscloudy.joybox.log.Logger;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ChessBoard extends GridPane implements GameHost {
    private static final Logger LOGGER = LogType.SUDOKU.getLogger();
    private final Group[] groups = new Group[27];
    private final Cell[] cells = new Cell[81];
    private final List<Integer> cellIndic = new ArrayList<>();
    private final Sudoku sudoku;
    private Cell editing;
    private DifficultyLevel difficultyLevel = DifficultyLevel.EASY;

    ChessBoard(Sudoku sudoku) {
        this.sudoku = sudoku;
        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        setVgap(1);
        setHgap(1);
        setPrefSize(Sudoku.CELL_SIDE_LEN * 9 + getHgap() * 10,
                Sudoku.CELL_SIDE_LEN * 9 + getVgap() * 10);

        for (int i = 0; i < 81; i++) {
            cellIndic.add(i);
        }

        for (int i = 0; i < 27; i++) {
            this.groups[i] = new Group();
        }

        int k = 0;
        for (int c = 0; c < 9; c++) {
            for (int r = 0; r < 9; r++) {
                int cir = (c / 3) * 3 + (r / 3);
                Group col = groups[c];
                Group row = groups[r + 9];
                Group circle = groups[cir + 18];

                Cell cell = new Cell(this, col, row, circle);
                col.addCell(cell);
                row.addCell(cell);
                circle.addCell(cell);

                add(cell, c, r);
                cells[k++] = cell;
            }
        }

        setOnKeyTyped(e -> {
            char char0 = e.getCharacter().charAt(0);
            if (char0 >= '0' && char0 <= '9') {
                fillCellValue(CellValue.values()[char0 - '0']);
            }
        });
    }

    void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    void prepareNewQuiz() {
        for (Group group : groups) {
            group.reset();
        }
        for (Cell cell : cells) {
            cell.reset();
        }
        editing = null;

        int badTimes = 0;
        for (int i = 0; i < 9; i++) {
            Group col = groups[i];
            Cell[] colCells = col.getCells();
            boolean badValues = false;
            do {
                if (badTimes > 100) {
                    LOGGER.error("bad value times exceed 100, please reset");
                    break;
                }
                int vi = 0;
                List<CellValue> valuesList = CellValue.getRandomList();
                for (int j = 0; j < colCells.length; j++) {
                    Cell cell = colCells[j];
                    int vc = 0;
                    CellValue cellValue;
                    do {
                        vc++;
                        cellValue = valuesList.get(vi++ % 9);
                    } while (cell.groupPrepContains(cellValue) && vc <= 9);

                    if (vc == 10) {
                        // all 9 value cannot be used
                        badTimes++;
                        badValues = true;
                        for (int jj = 0; jj < j; jj++) {
                            colCells[jj].reset();
                        }
                        for (Group group : groups) {
                            group.recalculatePrepRecord();
                        }
                        break;
                    }
                    cell.prepareValue(cellValue);
                    badValues = false;
                }
            } while (badValues);
        }

        LogType.SUDOKU.getLogger().info("%s level prepared, bad value times: %d", difficultyLevel,  badTimes);

        Collections.shuffle(cellIndic);
        for (int i = 0; i < difficultyLevel.fixCells; i++) {
            Integer i1 = cellIndic.get(i);
            cells[i1].fix();
        }
    }

    void checkBingo() {
        for (Group g : groups) {
            if (g.getFillingRecord().notBingo()) {
                return;
            }
        }
        showAlert("Yee! Bingo", "You solved this quiz");
    }

    void setEditing(Cell editing) {
        if (this.editing == editing) {
            return;
        } else if (this.editing != null) {
            this.editing.setNoBorder();
        }
        this.editing = editing;
    }

    public void fillCellValue(CellValue cellValue) {
        if (editing != null) {
            editing.fillValue(cellValue);
            checkBingo();
        }
    }

    @Override
    public Stage getOwnerStage() {
        return sudoku.getStage();
    }
}
