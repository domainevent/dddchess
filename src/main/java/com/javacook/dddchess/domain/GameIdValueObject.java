package com.javacook.dddchess.domain;

import java.util.UUID;


/**
 * Created by vollmer on 05.05.17.
 */
public class GameIdValueObject {

    public final String id = UUID.randomUUID().toString();

    @Override
    public String toString() {
        return id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameIdValueObject that = (GameIdValueObject) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
