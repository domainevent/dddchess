package com.javacook.dddchess.domain;

import javax.xml.bind.annotation.XmlType;


@XmlType
public class MoveValueObject extends ValueObject {

    public final PositionValueObject from;
    public final PositionValueObject to;


    public MoveValueObject() {
        this((PositionValueObject)null, (PositionValueObject)null);
        throw new IllegalStateException("This constructor should not be called here.");
    }


    public MoveValueObject(PositionValueObject from, PositionValueObject to) {
        this.from = from;
        this.to = to;
    }

    public MoveValueObject(String from, String to) {
        this(new PositionValueObject(from), new PositionValueObject(to));
    }

    public MoveValueObject(String fromTo) {
        this(fromTo.split("-")[0], fromTo.split("-")[1]);
    }

    @Override
    public String toString() {
        return from + "-" + to;
    }
}
