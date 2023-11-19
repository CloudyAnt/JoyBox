package cn.itscloudy.joybox.joys.game.minesweeper;

import cn.itscloudy.joybox.joys.VBoxJoy;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class MineSweeper extends VBoxJoy {
    public static final String NAME = "MineSweeper";
    static final int CELL_SIDE_LEN = 30;
    private final MineField minesField;

    public MineSweeper(Runnable onClose) {
        super(onClose);
        minesField = new MineField(this);

        HBox controls = getControls();
        for (Level value : Level.values()) {
            Button lvButton = new Button(value.display);
            lvButton.setOnAction(e -> minesField.setLevelAndPrepare(value));
            controls.getChildren().add(lvButton);
        }

        addAll(controls, minesField);
    }

    void afterLevelChanged() {
        updateSize();
    }

    @Override
    public void beforeTaken() {
        minesField.setLevelAndPrepare(Level.EASY);
    }
}
