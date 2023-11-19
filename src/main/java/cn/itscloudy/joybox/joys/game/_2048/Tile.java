package cn.itscloudy.joybox.joys.game._2048;

import cn.itscloudy.joybox.util.JoyConst;
import cn.itscloudy.joybox.util.JoyUtil;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

class Tile extends Label {

    Tile left;
    Tile right;
    Tile upper;
    Tile below;

    int number;
    int level;

    private static final Background DEF_BG = JoyUtil.bgOf(0, 0, 0, .1);
    public static final CornerRadii CORNER_RADII = new CornerRadii(.2);
    private static final Background[] BGS = new Background[]{
            JoyUtil.bgOf(new Color(0, 0, 0, .1), CORNER_RADII),
            JoyUtil.bgOf(new Color(.5, .5, .5, .1), CORNER_RADII),
            JoyUtil.bgOf(new Color(1, 1, 1, .1), CORNER_RADII)
    };
    private static final Color[] FG_COLORS = new Color[] {
            new Color(1, 1, 1, 1),
            new Color(.5, .5, .5, 1),
            new Color(0, 0, 0, 1),
    };

    Tile() {
        setFont(Font.font(20));
        setAlignment(Pos.CENTER);
        setPrefSize(_2048.TILE_SIDE_LEN, _2048.TILE_SIDE_LEN);
    }

    void init() {
        int randomI = JoyConst.RANDOM.nextInt(10);
        if (randomI < 7) {
            number = 2;
            level = 1;
        } else {
            number = 4;
            level = 2;
        }
        reset();
    }

    void clear() {
        number = 0;
        level = 0;
        setBackground(DEF_BG);
        setText("");
    }

    void upgrade() {
        number = number * 2;
        level += 1;
        reset();
    }

    void reset() {
        int i = level / BGS.length;
        if (i >= BGS.length) {
            i = BGS.length - 1;
        }
        setBackground(BGS[i]);
        setTextFill(FG_COLORS[i]);
        setText(number + "");
    }
}
