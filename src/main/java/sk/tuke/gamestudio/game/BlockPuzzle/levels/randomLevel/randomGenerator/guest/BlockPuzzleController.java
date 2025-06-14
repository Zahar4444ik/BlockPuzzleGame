package sk.tuke.gamestudio.game.BlockPuzzle.levels.randomLevel.randomGenerator.guest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.DTO.game.*;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Block;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Board;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Cell;
import sk.tuke.gamestudio.game.BlockPuzzle.levels.GameLevels;
import sk.tuke.gamestudio.game.BlockPuzzle.levels.Level;
import sk.tuke.gamestudio.game.BlockPuzzle.levels.LevelFactory;
import sk.tuke.gamestudio.service.jpa.GameService;

import java.util.List;

@Controller
@RequestMapping("api/game")
public class BlockPuzzleController {
    @Autowired
    private GameService gameService;

    @GetMapping("/data")
    @ResponseBody
    public GameStateDTO getGameData(@RequestParam(value = "difficulty", defaultValue = "EASY") String difficulty) {
        GameLevels levelDifficulty;
        try {
            levelDifficulty = GameLevels.valueOf(difficulty.toUpperCase());
        } catch (IllegalArgumentException e) {
            levelDifficulty = GameLevels.EASY; // Default to EASY if invalid
        }

        Level level = LevelFactory.createLevel(levelDifficulty);
        if (level == null) {
            level = LevelFactory.createLevel(GameLevels.EASY);
        }

        if (level == null) {
            throw new IllegalStateException("Level not found for difficulty: " + difficulty);
        }
        Board board = level.generateBoard();
        List<Block> blocks = level.generateBlocks();

        return gameService.convertToGameStateDTO(board, blocks);
    }

    @PostMapping("/update")
    @ResponseBody
    public GameStateDTO updateGame(@RequestBody GameUpdateRequestDTO request){
        return gameService.updateGameState(request);
    }
}