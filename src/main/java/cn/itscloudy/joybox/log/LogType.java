package cn.itscloudy.joybox.log;

public enum LogType {
    APP,
    SUDOKU,
    MINESWEEPER,
    _2048
    ;

    private final Logger logger;
    private final String displayName;
    LogType() {
        this(null);
    }

    LogType(String displayName) {
        logger = new Logger(this);
        this.displayName = displayName;
    }

    public Logger getLogger() {
        return logger;
    }

    public String getDisplayName() {
        return displayName == null ? name() : displayName;
    }
}
