package cn.itscloudy.joybox.joys;

import cn.itscloudy.joybox.joys.game.minesweeper.MineSweeper;
import cn.itscloudy.joybox.joys.game.sudoku.Sudoku;

import java.util.ArrayList;
import java.util.List;

public class Joys {

    private Joys() {
    }

    public static List<Joy> getAllJoys(Runnable onClose) {
        List<Joy> joys = new ArrayList<>();
        joys.add(new Sudoku(onClose));
        joys.add(new MineSweeper(onClose));
        return joys;
    }
}
