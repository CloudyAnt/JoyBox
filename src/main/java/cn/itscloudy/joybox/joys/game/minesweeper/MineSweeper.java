package cn.itscloudy.joybox.joys.game.minesweeper;

import cn.itscloudy.joybox.joys.VBoxJoy;
import cn.itscloudy.joybox.util.JoyDimension;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class MineSweeper extends VBoxJoy {

    static final int CELL_SIDE_LEN = 30;
    private final MineField minesField;
    private final HBox controls;
    public MineSweeper(Runnable onClose) {
        super(onClose);
        minesField = new MineField(this);

        controls = getControls();
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
    protected JoyDimension getJoyDimension() {
        double width = Math.max(controls.getPrefWidth(), minesField.getPrefWidth());
        double height = controls.getPrefHeight() + minesField.getPrefHeight();
        return new JoyDimension(width, height);
    }

    @Override
    public void beforeTaken() {
        minesField.setLevelAndPrepare(Level.EASY);
    }

    @Override
    public String getDisplay() {
        return "Mine Sweeper";
    }
}
