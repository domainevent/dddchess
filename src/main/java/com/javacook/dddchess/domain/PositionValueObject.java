package com.javacook.dddchess.domain;


import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * Value Object
 */
@XmlType
public class PositionValueObject {

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
    }


    public PositionValueObject(String coordEncoded) {
        horCoord = HorCoord.valueOf(coordEncoded.substring(0,1).toUpperCase());
        vertCoord = VertCoord.valueOf("_" + coordEncoded.substring(1,2));
    }


    @Override
    public String toString() {
        return horCoord.toString() + vertCoord.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PositionValueObject positionValueObject = (PositionValueObject) o;

        if (horCoord != positionValueObject.horCoord) return false;
        return vertCoord == positionValueObject.vertCoord;
    }

    @Override
    public int hashCode() {
        int result = horCoord != null ? horCoord.hashCode() : 0;
        result = 31 * result + (vertCoord != null ? vertCoord.hashCode() : 0);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(new PositionValueObject("a1"));
    }

}
