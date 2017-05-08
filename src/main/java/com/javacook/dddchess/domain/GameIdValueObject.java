package com.javacook.dddchess.domain;

import java.util.UUID;


/**
 * Created by vollmer on 05.05.17.
 */
public class GameIdValueObject extends ValueObject {

    public final String id;


    public GameIdValueObject(String id) {
        this.id = id;
    }

    public GameIdValueObject() {
        this(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return id;
    }

}
