package sk.tuke.gamestudio.game.BlockPuzzle.core.board;

import lombok.Getter;
import lombok.Setter;
import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.ConsoleColor;

@Setter
@Getter
public class Cell {
    private char symbol;
    private ConsoleColor color;

    public Cell(char symbol, ConsoleColor color) {
        this.symbol = symbol;
        this.color = color;
    }

    public Cell(ConsoleColor color){
        this.symbol = CellState.FILLED.getSymbol();
        this.color = color;
    }

    public static Cell createEmptyBlockCell() {
        return new Cell(CellState.EMPTY_BLOCK.getSymbol(), ConsoleColor.RESET);
    }

    public static Cell createEmptyBoardCell() {
        return new Cell(CellState.EMPTY_BOARD.getSymbol(), ConsoleColor.RESET);
    }

    public void printCell(){
        System.out.print(this.color.toString() + this.symbol + ConsoleColor.RESET + " ");
    }
}
