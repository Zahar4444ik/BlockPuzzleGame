package sk.tuke.gamestudio.service.restclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.DTO.CommentDTO;
import sk.tuke.gamestudio.DTO.PlayerDTO;
import sk.tuke.gamestudio.DTO.RatingDTO;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.exceptions.RatingException;

@Service
public class RatingServiceRestClient implements RatingService {
    private final String url = "http://localhost:9090/api/rating";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void setAndAddRating(Rating rating) throws RatingException {
        var player = rating.getPlayer();
        var playerDTO = new PlayerDTO(player.getNickname(), player.getScore(), player.getGamesPlayed(), player.getLastPlayed(), player.getPassword());
        var ratingDTO = new RatingDTO(rating.getGame(), playerDTO, rating.getRating(), rating.getRatedOn());
        this.restTemplate.postForEntity(this.url, ratingDTO, Rating.class);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        final var rating = this.restTemplate.getForObject(this.url + "/" + game, Double.class);
        return rating != null ? rating.intValue() : 0;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        final var rating = this.restTemplate.getForEntity(this.url + "/" + game + "/" + player, Double.class).getBody();
        return rating != null ? rating.intValue() : 0;
    }

    @Override
    public void reset() throws RatingException {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
