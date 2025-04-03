package sk.tuke.gamestudio.service.jdbc;

import sk.tuke.gamestudio.entity.Player;

public interface PlayerService {
    void registerPlayer(String nickname);
    boolean playerExists(String nickname);
    Player getPlayer(String nickname);
    void updatePlayer(Player player);
    void reset();
}
