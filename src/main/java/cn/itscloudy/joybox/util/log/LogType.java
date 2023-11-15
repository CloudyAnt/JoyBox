package cn.itscloudy.joybox.util.log;

public enum LogType {
    APP,
    SUDOKU;

    private final Logger logger;
    LogType() {
        logger = new Logger(this);
    }

    public Logger getLogger() {
        return logger;
    }
}
