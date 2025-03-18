package sk.tuke.kpi.BlockPuzzle.game.core.board;

import lombok.Getter;

@Getter
public enum CellState {
    EMPTY_BOARD('.'),
    EMPTY_BLOCK(' '),
    FILLED('█');

    private final char symbol;

    CellState(char symbol) {
        this.symbol = symbol;
    }

}