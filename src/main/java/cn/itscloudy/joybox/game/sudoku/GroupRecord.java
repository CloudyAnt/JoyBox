package cn.itscloudy.joybox.game.sudoku;

class GroupRecord {
    private int appearances;

    void reset() {
        appearances = 0;
    }

    void addValue(CellValue v) {
        appearances |= v.flagValue;
    }

    void removeValue(CellValue v) {
        appearances = appearances & ~v.flagValue;
    }

    boolean contains(CellValue v) {
        return (appearances & v.flagValue) == v.flagValue;
    }

    boolean notBingo() {
        return appearances != Sudoku.BINGO;
    }
}
