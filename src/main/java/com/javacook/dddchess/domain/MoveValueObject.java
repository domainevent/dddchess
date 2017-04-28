package com.javacook.dddchess.domain;

public class MoveValueObject {

    public final PositionValueObject from;
    public final PositionValueObject to;


    public MoveValueObject(PositionValueObject from, PositionValueObject to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return from + "-" + to;
    }
}
