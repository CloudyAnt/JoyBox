package cn.itscloudy.joybox.joys.game.hexsweeper;

enum HexLevel {
    EASY("E", 5, 5, 91),
    MEDIUM("M", 7, 15, 169),
    HARD("H", 9, 35, 271),
    ;
    final String display;
    final int radius;
    final int minesNum;
    final int totalCells;

    HexLevel(String display, int radius, int minesNum, int totalCells) {
        this.display = display;
        this.radius = radius;
        this.minesNum = minesNum;
        this.totalCells = totalCells;
    }
}