package cn.itscloudy.joybox.joys;

import javafx.stage.Stage;

public interface Joy {

    Stage getStage();

    void beforeTaken();

    void afterTaken();

}
