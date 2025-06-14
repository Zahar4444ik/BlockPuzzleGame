package sk.tuke.gamestudio.game.BlockPuzzle.core;

import lombok.Data;
import lombok.Getter;

import java.util.Objects;

@Data
public class Position {
    private final int row;
    private final int col;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Position position)) return false;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    public Position(int row, int col) {
        this.row = row;
        this.col = col;

    }
}
