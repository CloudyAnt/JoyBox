package cn.itscloudy.joybox.joys.game.hexsweeper;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;

class HexCell extends StackPane {
    private static final Color DEF_FILL = new Color(0, 0, 0, .15);
    private static final Color DUG_FILL = new Color(1, 1, 1, .1);
    private static final Color SIGNED_FILL = new Color(1, 0, 0, .2);
    private static final EventHandler<MouseEvent> PRESSING_HANDLER = event -> {
        HexCell cell = (HexCell) event.getSource();
        cell.afterPressed(event);
    };
    private static final EventHandler<MouseEvent> RELEASING_HANDLER = event -> {
        HexCell cell = (HexCell) event.getSource();
        cell.onReleased(event);
    };

    private static final double SIZE = 18;
    private static final double INDICATOR_TRANS = .6;
    private static final Color[] INDICATOR_COLORS = new Color[]{
            new Color(0, 0, 1, INDICATOR_TRANS),
            new Color(0, 1, 0, INDICATOR_TRANS),
            new Color(1, 0, 0, INDICATOR_TRANS),
            new Color(1, 0, 1, INDICATOR_TRANS),
            new Color(1, 1, 0, INDICATOR_TRANS),
            new Color(0, 1, 1, INDICATOR_TRANS),
    };

    private final HexField field;
    private final Polygon polygon;
    private final HexLabel label;

    private HexCoord coord;
    private boolean hasMine = false;
    private boolean starter = false;
    private boolean signed = false;
    private int nearbyMinesCount;
    private State state = State.VIRGIN;

    HexCell(HexField field) {
        this.field = field;
        this.polygon = createHexagon();
        this.label = new HexLabel();

        getChildren().addAll(polygon, label);
        setAlignment(Pos.CENTER);
        setPickOnBounds(false);
        label.setMouseTransparent(true);
        setOnMousePressed(PRESSING_HANDLER);
        setOnMouseReleased(RELEASING_HANDLER);
        applyVisualState();
    }

    private Polygon createHexagon() {
        double[] points = new double[12];
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i - 30);
            points[i * 2] = SIZE * Math.cos(angle);
            points[i * 2 + 1] = SIZE * Math.sin(angle);
        }
        Polygon hex = new Polygon(points);
        hex.setFill(Color.TRANSPARENT);
        hex.setStroke(Color.WHITE);
        hex.setStrokeWidth(1);
        return hex;
    }

    void setCoord(HexCoord coord) {
        this.coord = coord;
    }

    HexCoord getCoord() {
        return coord;
    }

    void reset() {
        hasMine = false;
        starter = false;
        signed = false;
        state = State.VIRGIN;
        nearbyMinesCount = 0;
        label.setText("");
        label.setTextFill(Color.BLACK);
        applyVisualState();
    }

    boolean isStarter() {
        return starter;
    }

    void layMine() {
        hasMine = true;
    }

    void clearMine() {
        hasMine = false;
    }

    boolean hasMine() {
        return hasMine;
    }

    boolean isSigned() {
        return signed;
    }

    boolean isVirgin() {
        return state == State.VIRGIN;
    }

    boolean isDug() {
        return state == State.DUG;
    }

    void setNearbyMinesCount(int count) {
        this.nearbyMinesCount = count;
    }

    private void afterPressed(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY) {
            return;
        }
        if (field.isSweeping() && isVirgin()) {
            dig();
        } else if (field.isIdling()) {
            starter = true;
            field.layMines();
            field.startSweeping();
            dig();
        }
    }

    private void dig() {
        if (isDug()) {
            return;
        }
        if (hasMine) {
            explode();
            return;
        }
        field.trackFrom(this);
    }

    void explode() {
        label.setTextFill(Color.RED);
        label.setText("X");
        field.reportMine();
    }

    void switchSignState() {
        signed = !signed;
        if (signed) {
            label.setText("!");
            label.setTextFill(Color.RED);
        } else {
            label.setText("");
        }
        applyVisualState();
    }

    void removeSignState() {
        if (signed) {
            signed = false;
            label.setText("");
            applyVisualState();
        }
    }

    void markDug() {
        state = State.DUG;
        applyVisualState();
    }

    void setIndicatorNumber() {
        if (nearbyMinesCount == 0) {
            return;
        }
        label.setText(String.valueOf(nearbyMinesCount));
        label.setTextFill(INDICATOR_COLORS[nearbyMinesCount - 1]);
    }

    void detonate() {
        if (hasMine) {
            label.setText("X");
        }
    }

    void sweep() {
        if (hasMine) {
            label.setTextFill(Color.GREEN);
            label.setText("√");
        }
    }

    void onReleased(MouseEvent event) {
        if (field.isSweeping() && event.getButton() == MouseButton.SECONDARY) {
            if (!isDug()) {
                switchSignState();
            }
        }
    }

    private static class HexLabel extends javafx.scene.control.Label {
        HexLabel() {
            setFont(new Font(10));
            setAlignment(Pos.CENTER);
            setPrefSize(SIZE * 2, SIZE * 2);
        }
    }

    private void applyVisualState() {
        if (signed) {
            polygon.setFill(SIGNED_FILL);
            return;
        }
        if (state == State.DUG) {
            polygon.setFill(DUG_FILL);
            return;
        }
        polygon.setFill(DEF_FILL);
    }

    @Override
    public boolean contains(double localX, double localY) {
        Point2D point = polygon.parentToLocal(localX, localY);
        return polygon.contains(point);
    }

    private enum State {
        VIRGIN,
        DUG
    }
}