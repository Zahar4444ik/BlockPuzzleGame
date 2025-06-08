package sk.tuke.gamestudio.game.BlockPuzzle.core.board;

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

    public static CellState fromSymbol(char symbol) {
        for (CellState state : CellState.values()) {
            if (state.symbol == symbol) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown cell state symbol: " + symbol);
    }
}