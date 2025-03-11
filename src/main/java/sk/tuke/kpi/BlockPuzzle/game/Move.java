package sk.tuke.kpi.BlockPuzzle.game;

import lombok.Getter;

@Getter
public enum Move {
    PLACE_BLOCK(1),
    REMOVE_BLOCK(2),
    GIVEN_UP(3),
    NONE(-1);

    final int num;

    Move(int num) {
        this.num = num;
    }

    public static Move fromNumber(int number) {
        for (Move move : Move.values()) {
            if (move.num == number) {
                return move;
            }
        }
        return NONE;
    }
}
