package sk.tuke.kpi.BlockPuzzle.gamestudio.service;

import sk.tuke.kpi.BlockPuzzle.gamestudio.entity.Rating;
import sk.tuke.kpi.BlockPuzzle.gamestudio.entity.Score;

import java.sql.*;

public class RatingServiceJDBC implements RatingService{
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "Zahar19%03";

    public static final String INSERT = "INSERT INTO rating (game, player, rating, rated_on) VALUES (?, ?, ?, ?)";
    public static final String SELECT = "SELECT rating FROM rating WHERE game = ? AND player = ?";
    public static final String DELETE = "DELETE FROM rating";

    public static final String SELECT_AVERAGE = "SELECT AVG(rating) FROM rating WHERE game = ?";
    public static final String PLAYER_EXISTS = "SELECT 1 FROM rating WHERE player = ?";

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)
        ) {
            if (playerExists(connection, rating.getPlayer())) {
                updateExistingPlayer(connection, rating);
            } else {
                addNewPlayerRating(statement, rating);
            }

        } catch (SQLException e) {
            throw new RatingException("Problem inserting rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_AVERAGE)
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return (int) Math.round(rs.getDouble(1));
                }
            }
            return 0;
        } catch (SQLException e) {
            throw new RatingException("Problem retrieving average rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT)
        ) {
            statement.setString(1, game);
            statement.setString(2, player);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        } catch (SQLException e) {
            throw new RatingException("Problem retrieving rating for player", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new RatingException("Problem deleting ratings", e);
        }
    }

    private boolean playerExists(Connection connection, String player) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(PLAYER_EXISTS)) {
            statement.setString(1, player);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void addNewPlayerRating(PreparedStatement statement, Rating rating) throws SQLException {
        statement.setString(1, rating.getGame());
        statement.setString(2, rating.getPlayer());
        statement.setInt(3, rating.getRating());
        statement.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
        statement.executeUpdate();
    }

    private void updateExistingPlayer(Connection connection, Rating rating) throws SQLException {
        final String UPDATE_RATING = "UPDATE rating SET rating = ?, rated_on = ? WHERE player = ?";

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_RATING)) {
            statement.setInt(1, rating.getRating());
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            statement.setString(3, rating.getPlayer());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ScoreException("Problem updating score of existing player", e);
        }
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
