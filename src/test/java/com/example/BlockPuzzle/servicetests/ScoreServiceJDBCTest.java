package com.example.BlockPuzzle.servicetests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.jdbc.ScoreException;
import sk.tuke.gamestudio.service.jdbc.ScoreServiceJDBC;

import java.sql.*;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ScoreServiceJDBCTest {
    private ScoreServiceJDBC scoreService;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private Statement mockStatementForReset;

    @BeforeEach
    void setUp() throws SQLException{
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        mockStatementForReset = mock(Statement.class);

        scoreService = new ScoreServiceJDBC() {
            @Override
            protected Connection getConnection() {
                return mockConnection;
            }
        };
    }

    @Test
    void TestAddAndSetScore() throws SQLException{
        Player player = new Player("testPlayer", "testPassword");
        final Score score = new Score("TestGame", player, 100, new Date());

        final PreparedStatement mockStatement_temp = mock(PreparedStatement.class);
        final ResultSet mockResultSet_temp = mock(ResultSet.class);
        when(mockConnection.prepareStatement(ScoreServiceJDBC.PLAYER_EXISTS)).thenReturn(mockStatement_temp);
        when(mockStatement_temp.executeQuery()).thenReturn(mockResultSet_temp);
        when(mockResultSet_temp.next()).thenReturn(false);

        when(mockConnection.prepareStatement(ScoreServiceJDBC.INSERT)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        scoreService.addAndSetScore(score);

        verify(mockConnection).prepareStatement(ScoreServiceJDBC.PLAYER_EXISTS);
        verify(mockStatement_temp).setString(eq(1), eq("testPlayer"));
        verify(mockResultSet_temp).next();

        verify(mockConnection).prepareStatement(ScoreServiceJDBC.INSERT);
        verify(mockStatement).setString(eq(1), eq("TestGame"));
        verify(mockStatement).setString(eq(2), eq("testPlayer"));
        verify(mockStatement).setInt(eq(3), eq(100));
        verify(mockStatement).setTimestamp(eq(4), any());
        verify(mockStatement).executeUpdate();
    }

    @Test
    void TestAddAndSetScoreInvalid() {
        Score score = new Score("TestGame", null, 0, null);

        assertThrows(ScoreException.class, () -> scoreService.addAndSetScore(score), "Invalid score");
    }

    @Test
    void TestGetTopScores() throws SQLException{
        String game = "TestGame";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString(1)).thenReturn("TestGame");
        when(mockResultSet.getString(2)).thenReturn("TestPlayer");
        when(mockResultSet.getInt(3)).thenReturn(100);
        when(mockResultSet.getTimestamp(4)).thenReturn(new Timestamp(new Date().getTime()));

        List<Score> scores = scoreService.getTopScores(game);

        assertNotNull(scores);
        assertFalse(scores.isEmpty());
        assertEquals(1, scores.size());
        assertEquals("TestGame", scores.get(0).getGame());
        assertEquals("TestPlayer", scores.get(0).getPlayer().getNickname());
        assertEquals(100, scores.get(0).getPoints());
    }

    @Test
    void TestPlayerExists() throws SQLException{
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        boolean exists = scoreService.playerExists(mockConnection, "TestPlayer");

        assertTrue(exists);
        verify(mockStatement, times(1)).setString(1, "TestPlayer");
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    void TestPlayerDoesNotExist() throws SQLException{
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        boolean exists = scoreService.playerExists(mockConnection, "TestPlayer");

        assertFalse(exists);
        verify(mockStatement, times(1)).setString(1, "TestPlayer");
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    void TestReset() throws SQLException{
        when(mockConnection.createStatement()).thenReturn(mockStatementForReset);

        scoreService.reset();

        verify(mockStatementForReset, times(1)).executeUpdate("DELETE FROM score");
    }
}

