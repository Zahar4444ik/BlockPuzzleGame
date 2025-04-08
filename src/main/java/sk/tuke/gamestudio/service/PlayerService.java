package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Player;

public interface PlayerService {
    boolean register(String nickname, String password);
    boolean login(String nickname, String password);
    boolean playerExists(String nickname);
    Player getPlayer(String nickname);
    void updatePlayer(Player player);
    void reset();
}
