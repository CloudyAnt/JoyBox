package cn.itscloudy.joybox.joys;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public interface Joy {

    Stage getStage();

    void beforeTaken();

    void afterTaken();

    default void showAlert(String title, String message) {
        Alert sayYee = new Alert(Alert.AlertType.INFORMATION);
        sayYee.initOwner(getStage());
        sayYee.setTitle(title);
        sayYee.setContentText(message);
        sayYee.setHeaderText(null);
        sayYee.showAndWait();
    }
}
