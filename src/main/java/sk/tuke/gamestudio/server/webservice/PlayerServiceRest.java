package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.DTO.LoginDTO;
import sk.tuke.gamestudio.DTO.PlayerDTO;
import sk.tuke.gamestudio.DTO.RegisterDTO;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.service.PlayerService;

@RestController
@RequestMapping("api/player")
public class PlayerServiceRest {

    @Autowired
    private PlayerService playerService;

    @GetMapping("{nickname}")
    public Player getPlayer(@PathVariable String nickname) {
        return playerService.getPlayer(nickname);
    }

    @PostMapping("/register")
    public boolean register(@RequestBody RegisterDTO registerRequest) {
        return playerService.register(registerRequest.getNickname(), registerRequest.getPassword());
    }

    @PostMapping("/login")
    public boolean login(@RequestBody LoginDTO loginRequest) {
        return playerService.login(loginRequest.getNickname(), loginRequest.getPassword());
    }

    @PutMapping("/update")
    public void updatePlayer(@RequestBody PlayerDTO playerDTO) {
        Player player = new Player(playerDTO.getNickname(), playerDTO.getPassword());
        player.setGamesPlayed(playerDTO.getGamesPlayed());
        player.setLastPlayed(player.getLastPlayed());
        player.setScore(playerDTO.getScore());
        this.playerService.updatePlayer(player);
    }
}
