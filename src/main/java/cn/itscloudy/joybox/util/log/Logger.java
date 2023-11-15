package cn.itscloudy.joybox.util.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LogType logType;
    Logger(LogType logType) {
        this.logType = logType;
    }

    private String prefix() {
        return LocalDateTime.now().format(DTF) + " [" + logType + "]: ";
    }

    public void info(String info) {
        System.out.println(prefix() + info);
    }

    public void error(String err) {
        System.err.println(prefix() + err);
    }

}
