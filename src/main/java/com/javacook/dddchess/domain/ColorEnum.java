package com.javacook.dddchess.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.xml.bind.annotation.XmlEnum;


/**
 * Created by vollmer on 08.05.17.
 */
@XmlEnum
public enum ColorEnum {
    WHITE, BLACK;

    @JsonValue
    public Character abbreviation() {
        switch (this) {
            case WHITE: return 'w';
            case BLACK: return 'b';
            default:
                throw new IllegalArgumentException("Unexpected enum " + this);
        }
    }

    @JsonCreator
    public ColorEnum fromAbbrev(Character c) {
        switch (c) {
            case 'w': return WHITE;
            case 'b': return BLACK;
        }
        throw new IllegalArgumentException("Unexpected abbreviation character " + this);
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
