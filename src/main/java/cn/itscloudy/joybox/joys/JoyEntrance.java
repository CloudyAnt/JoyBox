package cn.itscloudy.joybox.joys;

import javafx.scene.control.Button;

import java.util.function.Supplier;

public class JoyEntrance<J extends Joy> {

    private final String name;
    private final Supplier<J> joySupplier;
    JoyEntrance(String name, Supplier<J> joySupplier) {
        this.name = name;
        this.joySupplier = joySupplier;
    }

    public String getName() {
        return name;
    }

    public J getJoy() {
        return joySupplier.get();
    }

    public Button getDefaultEntrance() {
        return new Button(name);
    }
}
