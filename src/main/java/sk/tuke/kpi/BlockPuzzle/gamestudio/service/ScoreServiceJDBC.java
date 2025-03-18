package sk.tuke.kpi.BlockPuzzle.gamestudio.service;

import sk.tuke.kpi.BlockPuzzle.gamestudio.entity.Score;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ScoreServiceJDBC implements ScoreService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "Zahar19%03";
    public static final String SELECT = "SELECT game, player, points, playedOn FROM score WHERE game = ? ORDER BY points DESC LIMIT 10";
    public static final String DELETE = "DELETE FROM score";
    public static final String INSERT = "INSERT INTO score (game, player, points, playedOn) VALUES (?, ?, ?, ?)";

    public static final String PLAYER_EXISTS = "SELECT 1 FROM score WHERE player = ?";

    @Override
    public void addScore(Score score) {
        if (score == null || score.getPlayer() == null || score.getGame() == null || score.getPoints() == 0 || score.getPlayedOn() == null) {
            throw new ScoreException("Invalid score");
        }

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)
        ) {
            if (!playerExists(connection, score.getPlayer())) {
                addNewPlayer(statement, score);
            } else {
                updateExistingPlayer(connection, score);
            }
        } catch (SQLException e) {
            throw new ScoreException("Problem inserting score", e);
        }
    }

    @Override
    public List<Score> getTopScores(String game) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT);
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                List<Score> scores = new ArrayList<>();
                while (rs.next()) {
                    scores.add(new Score(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4)));
                }
                return scores;
            }
        } catch (SQLException e) {
            throw new ScoreException("Problem selecting score", e);
        }
    }

    @Override
    public void reset() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new ScoreException("Problem deleting score", e);
        }
    }

    public boolean playerExists(Connection connection, String player) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(PLAYER_EXISTS)) {
            statement.setString(1, player);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void addNewPlayer(PreparedStatement statement, Score score) throws SQLException {
            statement.setString(1, score.getGame());
            statement.setString(2, score.getPlayer());
            statement.setInt(3, score.getPoints());
            statement.setTimestamp(4, new Timestamp(score.getPlayedOn().getTime()));
            statement.executeUpdate();
    }

    private void updateExistingPlayer(Connection connection, Score score) throws SQLException {
        final String UPDATE_PLAYER = "UPDATE score SET points = ? WHERE player = ?";

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_PLAYER)) {
            statement.setInt(1, score.getPoints());
            statement.setString(2, score.getPlayer());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ScoreException("Problem updating score of existing player", e);
        }
    }

    protected Connection getConnection()  throws SQLException{
        return DriverManager.getConnection(URL, USER, PASSWORD);
    };
}