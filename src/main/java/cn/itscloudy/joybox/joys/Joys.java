package cn.itscloudy.joybox.joys;

import cn.itscloudy.joybox.joys.game._2048._2048;
import cn.itscloudy.joybox.joys.game.minesweeper.MineSweeper;
import cn.itscloudy.joybox.joys.game.sudoku.Sudoku;

import java.util.ArrayList;
import java.util.List;

public class Joys {

    private Joys() {
    }

    public static List<JoyEntrance<?>> getAllJoys(Runnable onClose) {
        List<JoyEntrance<?>> entrances = new ArrayList<>();
        entrances.add(new JoyEntrance<>(Sudoku.NAME, () -> new Sudoku(onClose)));
        entrances.add(new JoyEntrance<>(MineSweeper.NAME, () -> new MineSweeper(onClose)));
        entrances.add(new JoyEntrance<>(_2048.NAME, () -> new _2048(onClose)));
        return entrances;
    }
}
