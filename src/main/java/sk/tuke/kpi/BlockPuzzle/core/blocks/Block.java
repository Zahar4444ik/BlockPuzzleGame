package sk.tuke.kpi.BlockPuzzle.core.blocks;

import sk.tuke.kpi.BlockPuzzle.consoleui.ConsoleColor;
import sk.tuke.kpi.BlockPuzzle.core.Cell;

public class Block {
    private final Cell[][] shape;
    private final ConsoleColor color;

    public Block(Cell[][] shape, ConsoleColor color) {
        this.shape = shape;
        this.color = color;
    }

    public ConsoleColor getColor() {
        return color;
    }

    public Cell[][] getShape() {
        return shape;
    }

    public int getWidth() {
        return shape[0].length;
    }

    public int getHeight() {
        return shape.length;
    }
}