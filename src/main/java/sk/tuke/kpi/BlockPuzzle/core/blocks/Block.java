package sk.tuke.kpi.BlockPuzzle.core.blocks;

import lombok.Getter;
import sk.tuke.kpi.BlockPuzzle.consoleui.ConsoleColor;
import sk.tuke.kpi.BlockPuzzle.core.Cell;

@Getter
public class Block {
    private final Cell[][] shape;
    private final ConsoleColor color;

    public Block(Cell[][] shape, ConsoleColor color) {
        this.shape = shape;
        this.color = color;
    }

    public int getWidth() {
        return shape[0].length;
    }

    public int getHeight() {
        return shape.length;
    }
}