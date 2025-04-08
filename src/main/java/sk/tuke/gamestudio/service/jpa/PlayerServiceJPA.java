package sk.tuke.gamestudio.service.jpa;

import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.service.jdbc.PlayerService;

public class PlayerServiceJPA implements PlayerService {
    @Override
    public boolean register(String nickname, String password) {
        return false;
    }

    @Override
    public boolean login(String nickname, String password) {
        return false;
    }

    @Override
    public boolean playerExists(String nickname) {
        return false;
    }

    @Override
    public Player getPlayer(String nickname) {
        return null;
    }

    @Override
    public void updatePlayer(Player player) {

    }

    @Override
    public void reset() {

    }
}