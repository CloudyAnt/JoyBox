package cn.itscloudy.joybox.log;

public enum LogType {
    APP,
    SUDOKU,
    MINESWEEPER;

    private final Logger logger;
    LogType() {
        logger = new Logger(this);
    }

    public Logger getLogger() {
        return logger;
    }
}
