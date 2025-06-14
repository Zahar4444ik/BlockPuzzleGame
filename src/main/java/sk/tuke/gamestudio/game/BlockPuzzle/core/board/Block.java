package sk.tuke.gamestudio.game.BlockPuzzle.core.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.ConsoleColor;

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

    @JsonProperty("color")
    public int[] getColorAsRgb() {
        return color != null ? color.getRgb() : null;
    }

    @Override
    public int hashCode() {
        int result = color != null ? color.hashCode() : 0;
        for (Cell[] row : shape) {
            for (Cell cell : row) {
                result = 31 * result + (cell != null ? cell.hashCode() : 0);
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block block)) return false;

        if (getWidth() != block.getWidth() || getHeight() != block.getHeight()) return false;
        if (color != block.color) return false; // ok if ConsoleColor is enum

        // Compare shape contents
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (!shape[i][j].equals(block.shape[i][j])) return false;
            }
        }

        return true;
    }
}