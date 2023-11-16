package cn.itscloudy.joybox.joys.game.sudoku;

class Group {

    private final GroupRecord prepRecord;
    private final GroupRecord fillingRecord;
    private final Cell[] cells = new Cell[9];
    private int ci;

    Group() {
        prepRecord = new GroupRecord();
        fillingRecord = new GroupRecord();
    }

    void addCell(Cell cell) {
        cells[ci++] = cell;
    }

    void reset() {
        prepRecord.reset();
        fillingRecord.reset();
    }

    GroupRecord getPrepRecord() {
        return prepRecord;
    }

    GroupRecord getFillingRecord() {
        return fillingRecord;
    }

    void recalculateFillingRecord() {
        fillingRecord.reset();
        for (Cell cell : cells) {
            CellValue displayingValue = cell.getDisplayingValue();
            if (displayingValue != null) {
                fillingRecord.addValue(displayingValue);
            }
        }
    }

    void recalculatePrepRecord() {
        prepRecord.reset();
        for (Cell cell : cells) {
            CellValue preparedValue = cell.getPreparedValue();
            if (preparedValue != null) {
                prepRecord.addValue(preparedValue);
            }
        }

    }

    Cell[] getCells() {
        return cells;
    }
}
