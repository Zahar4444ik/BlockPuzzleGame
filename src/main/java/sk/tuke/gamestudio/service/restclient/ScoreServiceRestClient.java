package sk.tuke.gamestudio.service.restclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.DTO.PlayerDTO;
import sk.tuke.gamestudio.DTO.ScoreDTO;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.exceptions.ScoreException;

import java.util.Arrays;
import java.util.List;

@Service
public class ScoreServiceRestClient implements ScoreService {
    private final String url = "http://localhost:9090/api/score";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addAndSetScore(Score score) throws ScoreException {
        var player = score.getPlayer();
        var playerDTO = new PlayerDTO(player.getNickname(), player.getScore(), player.getGamesPlayed(), player.getLastPlayed(), player.getPassword());
        var scoreDTO = new ScoreDTO(score.getGame(), playerDTO, score.getPoints(), score.getPlayedOn());
        this.restTemplate.postForEntity(this.url, scoreDTO, Score.class);
    }

    @Override
    public List<Score> getTopScores(String game) throws ScoreException {
        final var scores = this.restTemplate.getForEntity(this.url + "/" + game, Score[].class).getBody();
        return Arrays.asList(scores != null ? scores : new Score[0]);
    }

    @Override
    public void reset() throws ScoreException {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
