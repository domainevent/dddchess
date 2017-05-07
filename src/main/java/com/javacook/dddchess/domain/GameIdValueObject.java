package com.javacook.dddchess.domain;

import java.util.UUID;


/**
 * Created by vollmer on 05.05.17.
 */
public class GameIdValueObject extends ValueObject {

    public final String id = UUID.randomUUID().toString();

    @Override
    public String toString() {
        return id;
    }

}
