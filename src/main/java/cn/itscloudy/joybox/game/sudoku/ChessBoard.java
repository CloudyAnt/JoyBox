package cn.itscloudy.joybox.game.sudoku;

import javafx.scene.control.Alert;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ChessBoard extends GridPane {
    private final Group[] groups = new Group[27];
    private final Cell[] cells = new Cell[81];
    private final List<Integer> cellIndic = new ArrayList<>();
    private Cell editing;
    private DifficultyLevel difficultyLevel = DifficultyLevel.EASY;

    ChessBoard() {
        setPrefSize(280, 280);
        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        setVgap(1);
        setHgap(1);


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

        List<CellValue> randomOrderValues = CellValue.getRandomList();
        int vi = 0;
        for (Cell cell : cells) {
            CellValue cellValue;
            do {
                cellValue = randomOrderValues.get(vi++ % 9);
            } while (cell.groupPrepContains(cellValue));
            cell.prepareValue(cellValue);
        }

        Collections.shuffle(cellIndic);
        for (int i = 0; i < difficultyLevel.getFixCells(); i++) {
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
        bingo();
    }

    void bingo() {
        Alert sayYee = new Alert(Alert.AlertType.INFORMATION);
        sayYee.setTitle("Yee! Bingo");
        sayYee.setContentText("You solved this quiz");
        sayYee.setHeaderText(null);
        sayYee.showAndWait();
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
}
