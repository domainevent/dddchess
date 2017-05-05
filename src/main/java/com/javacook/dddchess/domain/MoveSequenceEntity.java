package com.javacook.dddchess.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Created by javacook on 30.04.17.
 */
public class MoveSequenceEntity {

    public final GameIdValueObject id;
    protected final List<MoveValueObject> moves;

    public MoveSequenceEntity(GameIdValueObject id) {
        this.id = id;
        moves = new ArrayList<>();
    }


    public void addMove(MoveValueObject move) {
        moves.add(move);
    }

    public int noMoves() {
        return moves.size();
    }

    public Optional<MoveValueObject> getMove(int moveIndex) {
        return Optional.ofNullable(
        (moveIndex >= 0 && moveIndex < moves.size())?  moves.get(moveIndex) : null);
    }

    public void initialize() {
        moves.clear();
    }

}
