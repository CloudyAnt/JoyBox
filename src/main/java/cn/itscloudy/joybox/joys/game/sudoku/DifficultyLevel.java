package cn.itscloudy.joybox.joys.game.sudoku;

enum DifficultyLevel {
    EASY(50, "E"),
    MEDIUM(45, "M"),
    HARD(40, "H");
    final int fixCells;
    final String display;
    DifficultyLevel(int fixCells, String display) {
        this.fixCells = fixCells;
        this.display = display;
    }
}
