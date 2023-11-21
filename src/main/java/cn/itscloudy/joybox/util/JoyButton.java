package cn.itscloudy.joybox.util;

import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class JoyButton extends Button {

    private static final Background DEF_BG = new Background(new BackgroundFill(
            new Color(0, 0, 0, .5), null, null));

    private static final Background PRESSED_BG = new Background(new BackgroundFill(
            new Color(0, 0, 0, .9), null, null));

    public JoyButton() {
        this("");
    }

    public JoyButton(String text) {
        setText(text);
        setBackground(DEF_BG);
        setOnMousePressed(e -> setBackground(PRESSED_BG));
        setOnMouseReleased(e -> setBackground(DEF_BG));
    }
}
