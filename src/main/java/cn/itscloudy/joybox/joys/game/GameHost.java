package cn.itscloudy.joybox.joys.game;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public interface GameHost {

    Stage getOwnerStage();

    default void showAlert(String title, String message) {
        Alert sayYee = new Alert(Alert.AlertType.INFORMATION);
        sayYee.initOwner(getOwnerStage());
        sayYee.setTitle(title);
        sayYee.setContentText(message);
        sayYee.setHeaderText(null);
        sayYee.showAndWait();
    }
}
