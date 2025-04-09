package com.example.BlockPuzzle.servicetests.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.service.jdbc.CommentServiceJDBC;
import sk.tuke.gamestudio.service.exceptions.CommentException;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CommentServiceJDBCTest {

    private CommentServiceJDBC commentService;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private Statement mockStatementForReset;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        mockStatementForReset = mock(Statement.class);

        commentService = new CommentServiceJDBC() {
            @Override
            protected Connection getConnection() throws SQLException {
                return mockConnection;
            }
        };
    }

    @Test
    void testAddComment() throws SQLException {
        Player player = new Player("testPlayer", "testPassword");
        Comment comment = new Comment("TestGame", player, "Great game!", new java.util.Date());

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        commentService.addComment(comment);

        verify(mockStatement, times(1)).setString(eq(1), eq("TestGame"));
        verify(mockStatement, times(1)).setString(eq(2), eq("testPlayer"));
        verify(mockStatement, times(1)).setString(eq(3), eq("Great game!"));
        verify(mockStatement, times(1)).setTimestamp(eq(4), any(Timestamp.class));
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    void testAddCommentInvalidComment() {
        Player player = new Player("testPlayer", "testPassword");
        Comment comment = new Comment("TestGame", player, null, new java.util.Date());

        assertThrows(CommentException.class, () -> commentService.addComment(comment));
    }

    @Test
    void testGetComments() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString(1)).thenReturn("TestGame");
        when(mockResultSet.getString(2)).thenReturn("testPlayer");
        when(mockResultSet.getString(3)).thenReturn("Great game!");
        when(mockResultSet.getTimestamp(4)).thenReturn(new Timestamp(System.currentTimeMillis()));

        List<Comment> comments = commentService.getComments("TestGame");

        assertEquals(1, comments.size());
        assertEquals("TestGame", comments.get(0).getGame());
        assertEquals("testPlayer", comments.get(0).getPlayer().getNickname());
        assertEquals("Great game!", comments.get(0).getComment());
    }

    @Test
    void testGetCommentsNoComments() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        List<Comment> comments = commentService.getComments("TestGame");

        assertTrue(comments.isEmpty());
    }

    @Test
    void testReset() throws SQLException {
        when(mockConnection.createStatement()).thenReturn(mockStatementForReset);

        commentService.reset();

        verify(mockStatementForReset, times(1)).executeUpdate("DELETE FROM comment");
    }

    @Test
    void testAddCommentThrowsException() throws SQLException {
        Player player = new Player("testPlayer", "testPassword");
        Comment comment = new Comment("TestGame", player, "Great game!", new java.util.Date());

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(CommentException.class, () -> commentService.addComment(comment));
    }
}
