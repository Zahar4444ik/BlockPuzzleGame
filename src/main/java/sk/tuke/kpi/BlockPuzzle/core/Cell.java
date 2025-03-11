package sk.tuke.kpi.BlockPuzzle.core;

import sk.tuke.kpi.BlockPuzzle.consoleui.ConsoleColor;

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

    public char getSymbol() {
        return symbol;
    }

    public ConsoleColor getColor() {
        return color;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public void setColor(ConsoleColor color) {
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
