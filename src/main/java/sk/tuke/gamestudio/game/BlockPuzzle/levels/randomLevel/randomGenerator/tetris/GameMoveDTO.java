package sk.tuke.gamestudio.game.BlockPuzzle.levels.randomLevel.randomGenerator.tetris;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor
@Data
public class GameMoveDTO {
    private String move;
    private Integer row;
    private Integer col;
    private Integer blockIndex;
    private Integer offsetRow;
    private Integer offsetCol;
}
