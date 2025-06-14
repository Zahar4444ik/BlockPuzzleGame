package sk.tuke.gamestudio.game.BlockPuzzle.levels.randomLevel.randomGenerator.tetris;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.ConsoleColor;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.CellState;

import java.util.Objects;

@Setter
@Getter
public class Cell {
    private CellState state;
    private char symbol;
    private ConsoleColor color;

    public Cell(CellState state, ConsoleColor color) {
        this.state = state;
        this.symbol = state.getSymbol();
        this.color = color;
    }

    public Cell(ConsoleColor color) {
        this.state = CellState.FILLED;
        this.symbol = CellState.FILLED.getSymbol();
        this.color = color;
    }

    public static Cell createEmptyBlockCell() {
        return new Cell(CellState.EMPTY_BLOCK, ConsoleColor.RESET);
    }

    public static Cell createEmptyBoardCell() {
        return new Cell(CellState.EMPTY_BOARD, ConsoleColor.RESET);
    }

    @JsonProperty("state")
    public String getState() {
        return this.state.name();
    }

    @JsonProperty("color")
    public int[] getColorAsRgb() {
        return color != null ? color.getRgb() : null;
    }

    @JsonIgnore
    public void printCell() {
        System.out.print(this.color.toString() + this.state.getSymbol() + ConsoleColor.RESET + " ");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell cell)) return false;
        return state == cell.state
                && symbol == cell.symbol
                && Objects.equals(color, cell.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, symbol, color);
    }
}