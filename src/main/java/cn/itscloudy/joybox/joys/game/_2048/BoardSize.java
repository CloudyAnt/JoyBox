package cn.itscloudy.joybox.joys.game._2048;

enum BoardSize {
    STANDARD(4, 4),
    EXTENDED(5, 5);

    final int rowsNum;
    final int colsNum;
    final int tilesNem;

    BoardSize(int rowsNum, int colsNum) {
        this.rowsNum = rowsNum;
        this.colsNum = colsNum;
        this.tilesNem = rowsNum * colsNum;
    }
}
