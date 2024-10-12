# JoyBox

Oh~, h¡! Come have a little fun.

> JAVA 17 required.

## How to play

Run `gradle run` to start the game.

## How to package

Run `gradle jar` to package the game.
Then run this command to start the game

```shell
java --module-path=$JAVAFX_LIB_PATH --add-module javafx.controls,javafx.web,javafx.graphics,javafx.swing -jar build/libs/JoyBox-*.jar
```