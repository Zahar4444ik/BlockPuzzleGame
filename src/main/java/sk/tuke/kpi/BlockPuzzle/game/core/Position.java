package sk.tuke.kpi.BlockPuzzle.game.core;

import lombok.Getter;

import java.util.Objects;

@Getter
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
