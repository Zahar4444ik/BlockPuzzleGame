package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.DTO.RatingDTO;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.PlayerService;
import sk.tuke.gamestudio.service.RatingService;

@RestController
@RequestMapping("api/rating")
public class RatingServiceRest {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private PlayerService playerService;

    @GetMapping("{game}/{player}")
    public int getRating(@PathVariable String game, @PathVariable String player) {
        return ratingService.getRating(game, player);
    }

    @GetMapping("{game}")
    public int getAverageRating(@PathVariable String game) {
        return ratingService.getAverageRating(game);
    }

    @PostMapping
    public void setAndAddRating(@RequestBody RatingDTO ratingDTO) {
        var playerDTO = ratingDTO.getPlayerDTO();

        if (playerDTO != null && this.playerService.playerExists(playerDTO.getNickname())) {
            var player = new Player(playerDTO.getNickname(), playerDTO.getPassword());
            player.setLastPlayed(playerDTO.getLastPlayed());
            player.setScore(playerDTO.getScore());
            player.setGamesPlayed(playerDTO.getGamesPlayed());
            var rating = new Rating(ratingDTO.getGame(), player, ratingDTO.getRating(), ratingDTO.getRatedOn());
            ratingService.setAndAddRating(rating);
        }
        else {
            throw new IllegalArgumentException("Player does not exist");
        }
    }
}
