package sk.tuke.kpi.BlockPuzzle.core;

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