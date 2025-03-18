package sk.tuke.kpi.BlockPuzzle.gamestudio.service;

import sk.tuke.kpi.BlockPuzzle.gamestudio.entity.Player;

public interface PlayerService {
    void registerPlayer(String nickname);
    boolean playerExists(String nickname);
    Player getPlayer(String nickname);
    void updatePlayer(Player player);
    void reset();
}
