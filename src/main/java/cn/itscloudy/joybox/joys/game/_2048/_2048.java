package cn.itscloudy.joybox.joys.game._2048;

import cn.itscloudy.joybox.joys.VBoxJoy;
import cn.itscloudy.joybox.util.JoyButton;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class _2048 extends VBoxJoy {
    public static final String NAME = "2048";
    static final int TILE_SIDE_LEN = 60;

    private final HBox central;
    private TilesBoard board;

    public _2048() {
        BoardSize[] boardSizes = BoardSize.values();
        HBox controls = getControls();
        BoardButton def = null;
        for (BoardSize boardSize : boardSizes) {
            BoardButton boardButton = new BoardButton(boardSize);
            if (def == null) {
                def = boardButton;
            }
            controls.getChildren().add(boardButton);
        }
        getChildren().add(controls);

        assert def != null;
        central = new HBox();
        getChildren().add(central);
        setAndClearBoard(def.board);

        Button slideLeft = new JoyButton("←");
        slideLeft.setOnAction(e -> board.slide(SlideDirection.LEFT));
        Button slideRight = new JoyButton("→");
        slideRight.setOnAction(e -> board.slide(SlideDirection.RIGHT));
        Button slideUp = new JoyButton("↑");
        slideUp.setOnAction(e -> board.slide(SlideDirection.UP));
        Button slideDown = new JoyButton("↓");
        slideDown.setOnAction(e -> board.slide(SlideDirection.DOWN));

        HBox leftRightBox = new HBox();
        Region leftRightRegion = new Region();
        HBox.setHgrow(leftRightRegion, Priority.ALWAYS);
        HBox.setMargin(slideRight, new Insets(0, 30, 0, 0));
        leftRightBox.getChildren().addAll(leftRightRegion, slideLeft, slideRight);
        getChildren().add(leftRightBox);

        VBox upDownBox = new VBox();
        Region upDownRegion = new Region();
        VBox.setVgrow(upDownRegion, Priority.ALWAYS);
        upDownBox.getChildren().addAll(upDownRegion, slideUp, slideDown);
        central.getChildren().add(upDownBox);
    }

    private void setAndClearBoard(TilesBoard board) {
        this.board = board;
        ObservableList<Node> children = central.getChildren();
        if (!children.isEmpty()) {
            children.remove(0);
        }
        children.add(0, board);
        board.prepare();
    }

    private class BoardButton extends JoyButton {

        private final TilesBoard board;

        private BoardButton(BoardSize size) {
            super(size.colsNum + "×" + size.rowsNum);
            this.board = new TilesBoard(_2048.this, size);
            setOnAction(e -> {
                setAndClearBoard(board);
                updateSize();
            });
        }
    }
}
