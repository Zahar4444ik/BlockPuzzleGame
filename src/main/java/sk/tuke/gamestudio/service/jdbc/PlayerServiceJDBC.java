package sk.tuke.gamestudio.service.jdbc;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.GamePrinter;
import sk.tuke.gamestudio.service.PlayerService;
import sk.tuke.gamestudio.service.exceptions.PlayerException;

import java.sql.*;

public class PlayerServiceJDBC implements PlayerService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "Zahar19%03";

    private final BCryptPasswordEncoder passwordEncoder;

    public static final String INSERT_PLAYER = "INSERT INTO player (nickname, password, score, games_played, last_played) VALUES (?, ?, 0, 0, ?)";
    public static final String SELECT_PLAYER = "SELECT * FROM player WHERE nickname = ?";
    public static final String UPDATE_PLAYER = "UPDATE player SET score = ?, games_played = ?, last_played = ? WHERE nickname = ?";
    public static final String DELETE_PLAYERS = "DELETE FROM player";

    public PlayerServiceJDBC() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public boolean register(String nickname, String password) {
        if (nickname == null || password == null || nickname.isEmpty() || password.isEmpty()){
            GamePrinter.invalidInput();
            return false;
        }

        if (playerExists(nickname)){
            GamePrinter.playerWithNicknameAlreadyExists(nickname);
            return false;
        }

        String hashedPassword = passwordEncoder.encode(password);

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_PLAYER)){
            statement.setString(1, nickname);
            statement.setString(2, hashedPassword);
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
            return true;
        } catch (SQLException e){
            throw new PlayerException("Problem registering player", e);
        }
    }

    @Override
    public boolean login(String nickname, String password) {
        if (!playerExists(nickname)){
            GamePrinter.playerWithNicknameDoesNotExist(nickname);
            return false;
        }

        try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER)){
            statement.setString(1, nickname);
            try (ResultSet rs = statement.executeQuery()){
                if (rs.next()){
                    String storedHash = rs.getString("password");
                    boolean success = passwordEncoder.matches(password, storedHash);
                    if (!success) GamePrinter.invalidPassword();
                    return success;
                }
            }
        } catch (SQLException e){
            throw new PlayerException("Problem logging in player", e);
        }
        return false;
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
                    String password = rs.getString("password");
                    Player player = new Player(nickname, password);
                    player.setScore(rs.getInt("score"));
                    player.setLastPlayed(rs.getTimestamp("last_played"));
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

    protected Connection getConnection()  throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
