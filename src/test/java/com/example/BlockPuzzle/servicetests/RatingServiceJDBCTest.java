package com.example.BlockPuzzle.servicetests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.jdbc.RatingServiceJDBC;
import sk.tuke.gamestudio.service.exceptions.RatingException;

import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RatingServiceJDBCTest {

    private RatingServiceJDBC ratingService;
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

        ratingService = new RatingServiceJDBC() {
            @Override
            protected Connection getConnection() throws SQLException {
                return mockConnection;
            }
        };
    }

    @Test
    void testSetAndAddRatingForNewPlayer() throws SQLException {
        Player player = new Player("testPlayer", "testPassword");
        Rating rating = new Rating("TestGame", player, 5, new java.util.Date());

        PreparedStatement mockStatement_temp = mock(PreparedStatement.class);
        ResultSet mockResultSet_temp = mock(ResultSet.class);
        when(mockConnection.prepareStatement(RatingServiceJDBC.PLAYER_EXISTS)).thenReturn(mockStatement_temp);
        when(mockStatement_temp.executeQuery()).thenReturn(mockResultSet_temp);
        when(mockResultSet_temp.next()).thenReturn(false); // Simulating that the player does not exist

        when(mockConnection.prepareStatement(RatingServiceJDBC.INSERT)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        ratingService.setAndAddRating(rating);

        verify(mockConnection).prepareStatement(RatingServiceJDBC.PLAYER_EXISTS);
        verify(mockStatement_temp, times(1)).setString(eq(1), eq("testPlayer"));
        verify(mockResultSet_temp, times(1)).next();

        verify(mockStatement, times(1)).setString(eq(1), eq("TestGame"));
        verify(mockStatement, times(1)).setString(eq(2), eq("testPlayer"));
        verify(mockStatement, times(1)).setInt(eq(3), eq(5));
        verify(mockStatement, times(1)).setTimestamp(eq(4), any(Timestamp.class));
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    void testSetAndAddRatingForExistingPlayer() throws SQLException {
        Player player = new Player("testPlayer", "testPassword");
        Rating rating = new Rating("TestGame", player, 4, new java.util.Date());

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);  // Ensure playerExists method will be called

        // Simulate the case where player already exists
        when(mockConnection.prepareStatement("SELECT 1 FROM rating WHERE player = ?")).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Simulating that the player exists

        ratingService.setAndAddRating(rating);

        verify(mockStatement, times(1)).setInt(1, 4);
        verify(mockStatement, times(1)).setTimestamp(eq(2), any(Timestamp.class));
        verify(mockStatement, times(1)).setString(3, "testPlayer");
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    void testGetAverageRating() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getDouble(1)).thenReturn(4.5);

        int averageRating = ratingService.getAverageRating("TestGame");

        assertEquals(5, averageRating);
        verify(mockStatement, times(1)).setString(1, "TestGame");
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    void testGetRating() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(3);

        int rating = ratingService.getRating("TestGame", "Player1");

        assertEquals(3, rating);
        verify(mockStatement, times(1)).setString(1, "TestGame");
        verify(mockStatement, times(1)).setString(2, "Player1");
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    void testGetRatingForNonExistentPlayer() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        int rating = ratingService.getRating("TestGame", "NonExistentPlayer");

        assertEquals(-1, rating);
        verify(mockStatement, times(1)).setString(eq(1), eq("TestGame"));
        verify(mockStatement, times(1)).setString(eq(2), eq("NonExistentPlayer"));
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    void testReset() throws SQLException {
        when(mockConnection.createStatement()).thenReturn(mockStatementForReset);

        ratingService.reset();

        verify(mockStatementForReset, times(1)).executeUpdate("DELETE FROM rating");
    }

    @Test
    void testSetAndAddRatingThrowsException() throws SQLException {
        Player player = new Player("testPlayer", "testPassword");
        Rating rating = new Rating("TestGame", player, 5, new java.util.Date());

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(RatingException.class, () -> ratingService.setAndAddRating(rating));
    }
}