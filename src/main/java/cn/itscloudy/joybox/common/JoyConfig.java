package cn.itscloudy.joybox.common;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Locale;

public class JoyConfig {

    private JoyConfig() {
    }

    public static final Paint SCENE_BG;
    static {
        String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if (os.contains("wind")) {
            SCENE_BG = new Color(0, 0, 0, .1);
        } else {
            SCENE_BG = Color.TRANSPARENT;
        }
    }
}
