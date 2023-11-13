package cn.itscloudy.joybox.game.sudoku;

class Group {

    private final GroupRecord prepRecord;
    private final GroupRecord fillingRecord;


    Group() {
        prepRecord = new GroupRecord();
        fillingRecord = new GroupRecord();
    }
    void reset() {
        prepRecord.reset();
        fillingRecord.reset();
    }

    GroupRecord getPrepRecord() {
        return prepRecord;
    }

    GroupRecord getFillingRecord() {
        return fillingRecord;
    }
}
