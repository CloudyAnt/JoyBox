package cn.itscloudy.joybox.joys.game.minesweeper;

import cn.itscloudy.joybox.joys.VBoxJoy;
import cn.itscloudy.joybox.util.JoyButton;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class MineSweeper extends VBoxJoy {
    public static final String NAME = "Mine Sweeper";
    static final int CELL_SIDE_LEN = 30;
    private final MineField minesField;

    public MineSweeper() {
        minesField = new MineField(this);
        getChildren().add(minesField);
    }

    @Override
    protected List<Node> getRightControlNodes() {
        List<Node> controlNodes = new ArrayList<>();
        for (Level value : Level.values()) {
            Button lvButton = new JoyButton(value.display);
            lvButton.setOnAction(e -> minesField.setLevelAndPrepare(value));
            controlNodes.add(lvButton);
        }
        return controlNodes;
    }

    void afterLevelChanged() {
        updateSize();
    }

    @Override
    public void beforeTaken() {
        minesField.setLevelAndPrepare(Level.EASY);
    }
}
