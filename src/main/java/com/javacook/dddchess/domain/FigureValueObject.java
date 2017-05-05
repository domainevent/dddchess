package com.javacook.dddchess.domain;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class FigureValueObject {

    @XmlEnum
    public enum FigureEnum {
        KING, QUEEN, ROCK, BISHOP, KNIGHT, PAWN;

        public Character abbreviation() {
            switch (this) {
                case KING: return 'K';
                case QUEEN: return 'Q';
                case ROCK: return 'R';
                case BISHOP: return 'B';
                case KNIGHT: return 'N';
                case PAWN: return 'P';
            }
            throw new IllegalArgumentException("Unexpected enum " + this);
        }
    };

    @XmlEnum
    public enum ColorEnum {
        WHITE, BLACK;

        public Character abbreviation() {
            switch (this) {
                case WHITE: return 'w';
                case BLACK: return 'b';
                default:
                    throw new IllegalArgumentException("Unexpected enum " + this);
            }
        }

        public ColorEnum swap() {
            switch (this) {
                case WHITE: return BLACK;
                case BLACK: return WHITE;
                default:
                    throw new IllegalArgumentException("Unexpected enum " + this);
            }
        }
    };

    public final FigureEnum figure;
    public final ColorEnum color;


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

    public String abbreviation() {
        return "" + figure.abbreviation() + color.abbreviation();
    }
}
