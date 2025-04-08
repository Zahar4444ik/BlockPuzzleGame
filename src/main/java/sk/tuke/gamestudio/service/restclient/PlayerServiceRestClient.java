package sk.tuke.gamestudio.service.restclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.DTO.LoginDTO;
import sk.tuke.gamestudio.DTO.PlayerDTO;
import sk.tuke.gamestudio.DTO.RegisterDTO;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.service.PlayerService;
import sk.tuke.gamestudio.service.exceptions.PlayerException;

@Service
public class PlayerServiceRestClient implements PlayerService {
    private final String url = "http://localhost:9090/api/player";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean register(String nickname, String password) {
        RegisterDTO registerRequest = new RegisterDTO(nickname, password);
        try {
            var response = this.restTemplate.postForEntity(this.url + "/register", registerRequest, Boolean.class);
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean login(String nickname, String password) {
        LoginDTO loginRequest = new LoginDTO(nickname, password);
        try {
            Boolean success = this.restTemplate.postForObject(this.url + "/login", loginRequest, Boolean.class);
            return success != null && success;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean playerExists(String nickname) {
        try {
            Player player = this.restTemplate.getForObject(this.url + "/" + nickname, Player.class);
            return player != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Player getPlayer(String nickname) {
        try{
            return this.restTemplate.getForObject(this.url + "/" + nickname, Player.class);
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public void updatePlayer(Player player) {
        try{
            var playerDTO = new PlayerDTO(player.getNickname(), player.getScore(), player.getGamesPlayed(), player.getLastPlayed(), player.getPassword());
            this.restTemplate.put(this.url + "/update", playerDTO, Void.class);
        } catch (Exception e){
            throw new PlayerException("Error updating player data", e);
        }
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
