package sk.tuke.gamestudio.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sk.tuke.gamestudio.game.BlockPuzzle.levels.GameLevels;
import sk.tuke.gamestudio.game.BlockPuzzle.levels.Level;
import sk.tuke.gamestudio.game.BlockPuzzle.levels.LevelFactory;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("api/game")
public class BlockPuzzleController {
    @GetMapping("/data")
    @ResponseBody
    public Map<String, Object> getGameData(@RequestParam(value = "difficulty", defaultValue = "EASY") String difficulty) {
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

        Map<String, Object> gameState = new HashMap<>();
        if (level == null) {
            gameState.put("error", "Failed to create level");
            return gameState;
        }
        gameState.put("board", level.generateBoard());
        gameState.put("blocks", level.generateBlocks());
        return gameState;
    }
}