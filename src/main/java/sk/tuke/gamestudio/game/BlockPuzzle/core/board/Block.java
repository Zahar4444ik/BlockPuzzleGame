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
}