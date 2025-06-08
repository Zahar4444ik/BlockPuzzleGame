package sk.tuke.gamestudio.DTO.game;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
public class GameStateDTO {
    private CellDTO[][] grid;
    private List<BlockDTO> availableBlocks;
    private Map<String, BlockDTO> placedBlocks;
    private int score;
    private boolean hasWon;
}

