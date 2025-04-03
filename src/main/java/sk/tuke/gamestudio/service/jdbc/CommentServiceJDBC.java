package sk.tuke.gamestudio.service.jdbc;

import sk.tuke.gamestudio.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentServiceJDBC  implements CommentService{
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "Zahar19%03";

    public static final String INSERT = "INSERT INTO comment (game, player, comment, commented_on) VALUES (?, ?, ?, ?)";
    public static final String SELECT = "SELECT game, player, comment, commented_on FROM comment WHERE game = ? ORDER BY commented_on DESC LIMIT 10";
    public static final String DELETE = "DELETE FROM comment";

    @Override
    public void addAndSetComment(Comment comment) {
        if (!isCommentValid(comment)) {
            throw new CommentException("Invalid comment");
        }

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)
        ) {
            statement.setString(1, comment.getGame());
            statement.setString(2, comment.getPlayer());
            statement.setString(3, comment.getComment());
            statement.setTimestamp(4, new Timestamp(comment.getCommentedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new CommentException("Problem inserting comment", e);
        }
    }

    @Override
    public List<Comment> getComments(String game) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT)
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                List<Comment> comments = new ArrayList<>();
                while (rs.next()) {
                    comments.add(new Comment(rs.getString(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4)));
                }
                return comments;
            }
        } catch (SQLException e) {
            throw new CommentException("Problem selecting comments", e);
        }
    }

    @Override
    public void reset() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new CommentException("Problem deleting comments", e);
        }
    }

    private boolean isCommentValid(Comment comment) {
        return comment != null && comment.getPlayer() != null && comment.getGame() != null && comment.getComment() != null;
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
