package sk.tuke.gamestudio.game.BlockPuzzle.core.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.ConsoleColor;

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
}