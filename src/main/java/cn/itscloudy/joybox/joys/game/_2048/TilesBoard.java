package cn.itscloudy.joybox.joys.game._2048;

import cn.itscloudy.joybox.log.LogType;
import cn.itscloudy.joybox.log.Logger;
import cn.itscloudy.joybox.util.JoyConst;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.function.Function;

class TilesBoard extends GridPane {
    private static final Logger LOGGER = LogType._2048.getLogger();
    private final _2048 host;
    private final BoardSize boardSize;
    private final Tile[][] tiles;
    private Tile lastTilePlaceNumber;
    private int emptyTilesNum = 0;
    private boolean finished = false;
    private int maxCombination = 0;

    private static final Border NEW_VALUE_TILE_BORDER = new Border(new BorderStroke(Color.DARKBLUE,
            BorderStrokeStyle.SOLID, null, null));

    TilesBoard(_2048 host, BoardSize boardSize) {
        this.host = host;
        this.boardSize = boardSize;
        this.tiles = new Tile[boardSize.rowsNum][boardSize.colsNum];
        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        setVgap(1);
        setHgap(1);

        for (int r = 0; r < boardSize.rowsNum; r++) {
            for (int c = 0; c < boardSize.colsNum; c++) {
                tiles[r][c] = new Tile();
                add(tiles[r][c], c, r);
            }
        }
        for (int r = 0; r < boardSize.rowsNum; r++) {
            for (int c = 0; c < boardSize.colsNum; c++) {
                Tile t = tiles[r][c];
                t.left = c > 0 ? tiles[r][c - 1] : null;
                t.right = c < boardSize.colsNum - 1 ? tiles[r][c + 1] : null;
                t.upper = r > 0 ? tiles[r - 1][c] : null;
                t.below = r < boardSize.rowsNum - 1 ? tiles[r + 1][c] : null;
            }
        }
    }

    void prepare() {
        for (Tile[] tile : tiles) {
            for (Tile value : tile) {
                value.clear();
            }
        }
        emptyTilesNum = boardSize.tilesNem;
        maxCombination = 0;
        finished = false;
        if (lastTilePlaceNumber != null) {
            lastTilePlaceNumber.setBorder(null);
            lastTilePlaceNumber = null;
        }
        placeNewNumber();
    }

    void slide(SlideDirection direction) {
        if (finished) {
            return;
        }

        int iRow = direction.initAtZero ? 0 : boardSize.rowsNum - 1;
        int iCol = direction.initAtZero ? 0 : boardSize.colsNum - 1;
        Tile lineInit = tiles[iRow][iCol];

        do {
            throughLine(lineInit, direction.lineNextGetter);
            lineInit = direction.nextInitGetter.apply(lineInit);
        } while (lineInit != null);

        placeNewNumber();
    }

    void placeNewNumber() {
        if (emptyTilesNum == 0) {
            return;
        }
        if (lastTilePlaceNumber != null) {
            lastTilePlaceNumber.setBorder(null);
        }

        Tile[] candidates = new Tile[emptyTilesNum];
        int i = 0;
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile.number == 0) {
                    candidates[i++] = tile;
                }
            }
        }
        Tile chosen = candidates[JoyConst.RANDOM.nextInt(emptyTilesNum)];
        chosen.init();
        chosen.setBorder(NEW_VALUE_TILE_BORDER);
        lastTilePlaceNumber = chosen;
        emptyTilesNum--;
        finishingCheck();
    }

    void throughLine(Tile tile, Function<Tile, Tile> nextGetter) {
        if (invertSwapNextNon0Tile(tile, nextGetter)) {
            return;
        }

        Tile next = nextGetter.apply(tile);
        if (next == null) {
            return;
        }
        if (invertSwapNextNon0Tile(next, nextGetter)) {
            return;
        }

        if (tile.number == next.number) {
            tile.upgrade();
            if (tile.number > maxCombination) {
                maxCombination = tile.number;
            }
            next.clear();
            emptyTilesNum++;
        }

        throughLine(next, nextGetter);
    }

    private void finishingCheck() {
        if (emptyTilesNum != 0) {
            return;
        }

        // check rows
        for (Tile[] row : tiles) {
            for (int i = 0; i < row.length - 1; i++) {
                if (row[i].number == row[i + 1].number) {
                    return;
                }
            }
        }

        // check cols
        for (int i = 0; i < boardSize.colsNum; i++) {
            for (int j = 0; j < boardSize.rowsNum - 1; j++) {
                if (tiles[j][i].number == tiles[j + 1][i].number) {
                    return;
                }
            }
        }

        finished = true;
        host.showAlert("No more possibilities", "Max combination: " + maxCombination);
    }

    boolean invertSwapNextNon0Tile(Tile current, Function<Tile, Tile> nextGetter) {
        if (current.number != 0) {
            return false;
        }

        Tile next = nextGetter.apply(current);
        while (next != null && next.number == 0) {
            next = nextGetter.apply(next);
        }
        if (next == null) {
            return true;
        }
        current.number = next.number;
        current.level = next.level;
        current.reset();
        next.clear();
        return false;
    }

}
