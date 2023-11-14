package cn.itscloudy.joybox.game.sudoku;

enum DifficultyLevel {
    EASY(50, "E"),
    MEDIUM(45, "M"),
    HARD(40, "H");
    private final int fixCells;
    private final String display;
    DifficultyLevel(int fixCells, String display) {
        this.fixCells = fixCells;
        this.display = display;
    }
    public int getFixCells() {
        return fixCells;
    }

    public String getDisplay() {
        return display;
    }
}
