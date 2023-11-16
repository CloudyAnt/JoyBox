package cn.itscloudy.joybox.joys.game.sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

enum CellValue {
    _1,
    _2,
    _3,
    _4,
    _5,
    _6,
    _7,
    _8,
    _9;

    final int flagValue;
    final String display;
    static List<CellValue> valueList;

    CellValue() {
        flagValue = (int) Math.pow(2, ordinal());
        display = (ordinal() + 1) + "";
    }

    static List<CellValue> getRandomList() {
        if (valueList == null) {
            valueList = new ArrayList<>(Arrays.asList(values()));
        }
        Collections.shuffle(valueList);
        return valueList;
    }
}
