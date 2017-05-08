package com.javacook.dddchess.domain;

import javax.xml.bind.annotation.XmlEnum;


/**
 * Created by vollmer on 08.05.17.
 */
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
