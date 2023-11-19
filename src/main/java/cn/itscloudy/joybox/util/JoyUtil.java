package cn.itscloudy.joybox.util;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class JoyUtil {
    private JoyUtil() {
    }

    public static Background bgOf(double red, double green, double blue, double opacity) {
        return new Background(new BackgroundFill(new Color(red, green, blue, opacity), null, null));
    }

    public static Background bgOf(Color color, CornerRadii cornerRadii) {
        return new Background(new BackgroundFill(color, cornerRadii, null));
    }

    public static double max(double first, double... rest) {
        double max = first;
        for (double r : rest) {
            if (r > max) {
                max = r;
            }
        }
        return max;
    }
}
