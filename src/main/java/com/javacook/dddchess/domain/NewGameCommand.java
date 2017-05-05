package com.javacook.dddchess.domain;

import com.javacook.dddchess.domain.FigureValueObject.ColorEnum;


/**
 * Created by vollmer on 04.05.17.
 */
public class NewGameCommand {

    public final ColorEnum color;

    public NewGameCommand(ColorEnum color) {
        this.color = color;
    }


    @Override
    public String toString() {
        return "NewGameCommand{" +
                "color=" + color +
                '}';
    }
}
