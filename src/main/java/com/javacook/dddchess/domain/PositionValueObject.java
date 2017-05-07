package com.javacook.dddchess.domain;


import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType
public class PositionValueObject extends ValueObject {

    @XmlEnum
    public enum HorCoord {
        A, B, C, D, E, F, G, H;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }


    @XmlEnum
    public enum VertCoord {
        _1, _2, _3, _4, _5, _6, _7, _8;

        public static VertCoord[] valuesInverted() {
            return new VertCoord[]{_8, _7, _6, _5, _4, _3, _2, _1};
        }

        @Override
        public String toString() {
            return String.valueOf(ordinal()+1);
        }
    }


    public final HorCoord horCoord;
    public final VertCoord vertCoord;

    public PositionValueObject(HorCoord horCoord, VertCoord vertCoord) {
        this.horCoord = horCoord;
        this.vertCoord = vertCoord;
    }


    public PositionValueObject() {
        this(null, null);
        throw new IllegalStateException("This constructor should not be called here.");
    }


    public PositionValueObject(String coordEncoded) {
        horCoord = HorCoord.valueOf(coordEncoded.substring(0,1).toUpperCase());
        vertCoord = VertCoord.valueOf("_" + coordEncoded.substring(1,2));
    }


    @Override
    public String toString() {
        return horCoord.toString() + vertCoord.toString();
    }

}
