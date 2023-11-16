package cn.itscloudy.joybox.joys.game.minesweeper;

enum Level {
    EASY("E", 9, 9, 10),
    MEDIUM("M", 16, 16, 30),
    HARD("H", 16, 30, 70),
    ;
    final String display;
    final int rowsNum;
    final int colsNum;
    final int minesNum;
    final int totalCells;

    Level(String display, int rowsNum, int colsNum, int minesNum) {
        this.display = display;
        this.rowsNum = rowsNum;
        this.colsNum = colsNum;
        this.minesNum = minesNum;
        this.totalCells = rowsNum * colsNum;
    }
}
