package com.javacook.dddchess.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class FigureValueObject extends ValueObject {

    @XmlEnum
    public enum FigureEnum {
        KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN;

        @JsonValue
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

        @JsonCreator
        public FigureEnum fromAbbrev(Character c) {
            switch (c) {
                case 'K': return KING;
                case 'Q': return QUEEN;
                case 'R': return ROOK;
                case 'B': return BISHOP;
                case 'N': return KNIGHT;
                case 'P': return PAWN;
            }
            throw new IllegalArgumentException("Unexpected abbreviation character " + this);
        }

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


    public String abbreviation() {
        return "" + figure.abbreviation() + color.abbreviation();
    }

}
