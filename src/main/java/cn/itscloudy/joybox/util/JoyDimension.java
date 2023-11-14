package cn.itscloudy.joybox.util;

public record JoyDimension(int width, int height) {

    public JoyDimension(double width, double height) {
        this((int) width, (int) height);
    }
}
