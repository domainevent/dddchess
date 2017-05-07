package com.javacook.dddchess.domain;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class FigureValueObject extends ValueObject {

    @XmlEnum
    public enum FigureEnum {
        KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN;

        public Character abbreviation() {
            switch (this) {
                case KING: return 'K';
                case QUEEN: return 'Q';
                case ROOK: return 'R';
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


    public FigureValueObject() {
        this(null, null);
        throw new IllegalStateException("This constructor should not be called here.");
    }


    public FigureValueObject(FigureEnum figure, ColorEnum color) {
        this.figure = figure;
        this.color = color;
    }


    public String abbreviation() {
        return "" + figure.abbreviation() + color.abbreviation();
    }

}
