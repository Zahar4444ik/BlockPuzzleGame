package sk.tuke.gamestudio.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Board;
import sk.tuke.gamestudio.game.BlockPuzzle.levels.GameLevels;
import sk.tuke.gamestudio.game.BlockPuzzle.levels.Level;
import sk.tuke.gamestudio.game.BlockPuzzle.levels.LevelFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/game")
public class BlockPuzzleController {
    @GetMapping("/data")
    @ResponseBody
    public Map<String, Object> getGameData(@RequestParam(value = "difficulty", defaultValue = "EASY") String difficulty) {
        GameLevels levelDifficulty = GameLevels.valueOf(difficulty.toUpperCase());
        Level level = LevelFactory.createLevel(levelDifficulty);
        if (level == null) {
            level = LevelFactory.createLevel(GameLevels.EASY); // Default to EASY if invalid
        }

        Map<String, Object> gameState = new HashMap<>();
        gameState.put("board", level.generateBoard());
        gameState.put("blocks", level.generateBlocks());
        return gameState;
    }

//    @PostMapping("/check-win")
//    @ResponseBody
//    public Map<String, Object> checkWin(@RequestBody BoardDto boardDto) {
//        Board board = BoardMapper.fromDto(boardDto); // convert DTO to real Board
//        boolean isFull = board.isFull();
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("isFull", isFull);
//        return response;
//    }

    private Board parseBoardFromJson(String boardJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(boardJson, Board.class);
    }

}