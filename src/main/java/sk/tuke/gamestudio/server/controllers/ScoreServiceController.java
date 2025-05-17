package sk.tuke.gamestudio.server.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.DTO.PlayerDTO;
import sk.tuke.gamestudio.DTO.ScoreDTO;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.PlayerService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreServiceController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private PlayerService playerService;

    @GetMapping("/{game}")
    public List<Score> getTopScores(@PathVariable String game) {
        return scoreService.getTopScores(game);
    }

    @PostMapping
    public void addAndSetScore(@RequestBody ScoreDTO scoreDTO) {
        PlayerDTO playerDTO = scoreDTO.getPlayerDTO();

        if(playerDTO != null && this.playerService.playerExists(playerDTO.getNickname())) {
            var player = new Player(playerDTO.getNickname(), playerDTO.getPassword());
            player.setLastPlayed(playerDTO.getLastPlayed());
            player.setScore(playerDTO.getScore());
            player.setGamesPlayed(playerDTO.getGamesPlayed());
            var score = new Score(scoreDTO.getGame(), player, scoreDTO.getPoints(), scoreDTO.getPlayedOn());
            scoreService.addAndSetScore(score);
        }
        else {
            throw new IllegalArgumentException("Player does not exist");
        }
    }

}
