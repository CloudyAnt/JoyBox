package cn.itscloudy.joybox.joys.game.hexsweeper;

import java.util.ArrayList;
import java.util.List;

record HexCoord(int q, int r) {

    static final HexCoord[] DIRECTIONS = {
            new HexCoord(1, 0),
            new HexCoord(1, -1),
            new HexCoord(0, -1),
            new HexCoord(-1, 0),
            new HexCoord(-1, 1),
            new HexCoord(0, 1)
    };

    List<HexCoord> neighbors() {
        List<HexCoord> result = new ArrayList<>();
        for (HexCoord dir : DIRECTIONS) {
            result.add(new HexCoord(q + dir.q(), r + dir.r()));
        }
        return result;
    }

    @Override
    public String toString() {
        return "(" + q + "," + r + ")";
    }
}