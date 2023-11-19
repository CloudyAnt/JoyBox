package cn.itscloudy.joybox.joys.game._2048;

import java.util.function.Function;

enum SlideDirection {
    UP(t -> t.below, t -> t.right, true),
    DOWN(t -> t.upper, t -> t.left, false),
    LEFT(t -> t.right, t -> t.below, true),
    RIGHT(t -> t.left, t -> t.upper, false)
    ;
    final Function<Tile, Tile> lineNextGetter;
    final Function<Tile, Tile> nextInitGetter;
    final boolean initAtZero;

    SlideDirection(Function<Tile, Tile> lineNextGetter,
                   Function<Tile, Tile> nextInitGetter,
                   boolean initAtZero) {
        this.lineNextGetter = lineNextGetter;
        this.nextInitGetter = nextInitGetter;
        this.initAtZero = initAtZero;
    }
}
