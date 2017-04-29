package com.javacook.dddchess.domain;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class FigureValueObject {

    @XmlEnum
    public enum FigureEnum {
        KING, QUENN, ROCK, BISHOP, KNIGHT, PAWN
    };

    @XmlEnum
    public enum ColorEnum {
        WHITE, BLACK
    };

    public final FigureEnum figure;
    public final ColorEnum color;


    public FigureValueObject() {
        this(null, null);
    }


    public FigureValueObject(FigureEnum figure, ColorEnum color) {
        this.figure = figure;
        this.color = color;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FigureValueObject that = (FigureValueObject) o;

        if (figure != that.figure) return false;
        return color == that.color;
    }


    @Override
    public int hashCode() {
        int result = figure != null ? figure.hashCode() : 0;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "FigureValueObject{" +
                "figure=" + figure +
                ", color=" + color +
                '}';
    }
}
