package com.javacook.dddchess.domain;


/**
 * Value Object
 */
public class PositionValueObject {

    public enum HorCord {
        A, B, C, D, E, F, G, H;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public enum VertCord {
        _1, _2, _3, _4, _5, _6, _7, _8;

        @Override
        public String toString() {
            return String.valueOf(ordinal()+1);
        }
    }

    public final HorCord horCord;
    public final VertCord vertCord;

    public PositionValueObject(HorCord horCoord, VertCord vertCoord) {
        this.horCord = horCoord;
        this.vertCord = vertCoord;
    }

    public PositionValueObject(String coordEncoded) {
        horCord = HorCord.valueOf(coordEncoded.substring(0,1).toUpperCase());
        vertCord = VertCord.valueOf("_" + coordEncoded.substring(1,2));
    }


    @Override
    public String toString() {
        return horCord.toString() + vertCord.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PositionValueObject positionValueObject = (PositionValueObject) o;

        if (horCord != positionValueObject.horCord) return false;
        return vertCord == positionValueObject.vertCord;
    }

    @Override
    public int hashCode() {
        int result = horCord != null ? horCord.hashCode() : 0;
        result = 31 * result + (vertCord != null ? vertCord.hashCode() : 0);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(new PositionValueObject("a1"));
    }

}
