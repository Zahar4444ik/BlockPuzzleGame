package sk.tuke.kpi.BlockPuzzle.gamestudio.service;

import sk.tuke.kpi.BlockPuzzle.gamestudio.entity.Player;
import sk.tuke.kpi.BlockPuzzle.gamestudio.entity.Score;

import java.sql.*;

public class PlayerServiceJDBC implements PlayerService{
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "Zahar19%03";

    public static final String INSERT_PLAYER = "INSERT INTO player (nickname, score, gamesPlayed, lastPlayed) VALUES (?, 0, 0, ?)";
    public static final String SELECT_PLAYER = "SELECT * FROM player WHERE nickname = ?";
    public static final String UPDATE_PLAYER = "UPDATE player SET score = ?, gamesPlayed = ?, lastPlayed = ? WHERE nickname = ?";
    public static final String DELETE_PLAYERS = "DELETE FROM player";

    @Override
    public void registerPlayer(String nickname) {
        if (playerExists(nickname)) return;

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_PLAYER)){
            statement.setString(1, nickname);
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
        } catch (SQLException e){
            throw new PlayerException("Problem registering player", e);
        }
    }

    @Override
    public boolean playerExists(String nickname) {
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER)){
            statement.setString(1, nickname);
            try (ResultSet rs = statement.executeQuery()){
                return rs.next();
            }
        } catch (SQLException e){
            throw new PlayerException("Problem checking player existence", e);
        }
    }

    @Override
    public Player getPlayer(String nickname) {
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER)){
            statement.setString(1, nickname);
            try (ResultSet rs = statement.executeQuery()){
                if (rs.next()){
                    Player player = new Player(nickname);
                    player.setScore(rs.getInt("score"));
                    player.setLastPlayed(rs.getTimestamp("lastPlayed"));
                    return player;
                }
            }
        } catch (SQLException e){
            throw new PlayerException("Problem getting player", e);
        }
        return null;
    }

    @Override
    public void updatePlayer(Player player) {
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_PLAYER)){
            statement.setInt(1, player.getScore());
            statement.setInt(2, player.getGamesPlayed());
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.setString(4, player.getNickname());
            statement.executeUpdate();
        } catch (SQLException e){
            throw new PlayerException("Problem updating player", e);
        }
    }

    @Override
    public void reset() {
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement()){
            statement.executeUpdate(DELETE_PLAYERS);
        } catch (SQLException e){
            throw new PlayerException("Problem deleting players", e);
        }
    }

    protected Connection getConnection()  throws SQLException{
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
