package sk.tuke.gamestudio.game.BlockPuzzle.levels.randomLevel.randomGenerator.hint;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class HintDTO {
    private int blockIndex;
    private int row;
    private int col;
}
