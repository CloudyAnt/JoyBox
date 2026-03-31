package cn.itscloudy.joybox.joys.game.hexsweeper;

import cn.itscloudy.joybox.joys.VBoxJoy;
import cn.itscloudy.joybox.util.JoyButton;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class HexSweeper extends VBoxJoy {
    public static final String NAME = "HexSweeper";
    private final HexField hexField;

    public HexSweeper() {
        hexField = new HexField(this);
        getChildren().add(hexField);
    }

    @Override
    protected List<Node> getRightControlNodes() {
        List<Node> controlNodes = new ArrayList<>();
        for (HexLevel value : HexLevel.values()) {
            Button lvButton = new JoyButton(value.display);
            lvButton.setOnAction(e -> hexField.setLevelAndPrepare(value));
            controlNodes.add(lvButton);
        }
        return controlNodes;
    }

    void afterLevelChanged() {
        updateSize();
    }

    @Override
    public void beforeTaken() {
        hexField.setLevelAndPrepare(HexLevel.EASY);
    }
}