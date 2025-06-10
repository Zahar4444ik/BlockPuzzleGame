package sk.tuke.gamestudio.game.BlockPuzzle.levels.randomLevel.randomGenerator.tetris;

import lombok.AllArgsConstructor;
import lombok.Data;
import sk.tuke.gamestudio.DTO.game.BlockDTO;
import sk.tuke.gamestudio.DTO.game.CellDTO;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
public class GameStateDTO {
    private CellDTO[][] grid;
    private List<BlockDTO> availableBlocks;
    private Map<String, BlockDTO> placedBlocks;
    private Map<String, BlockDTO> blockPositions;
    private boolean hasWon;
}

