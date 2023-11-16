package cn.itscloudy.joybox.joys;

import javafx.scene.control.Button;
import javafx.stage.Stage;

public interface Joy {

    Stage toStage();

    void beforeTaken();

    void afterTaken();

    String getDisplay();

    default Button getDefaultEntrance() {
        return new Button(getDisplay());
    }
}
