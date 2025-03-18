package com.example.BlockPuzzle.servicetests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.kpi.BlockPuzzle.gamestudio.entity.Player;
import sk.tuke.kpi.BlockPuzzle.gamestudio.service.PlayerServiceJDBC;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PlayerServiceJDBCTest {
    private PlayerServiceJDBC playerService;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        playerService = new PlayerServiceJDBC() {
            @Override
            protected Connection getConnection()  throws SQLException{
                return mockConnection;
            }
        };
    }

    @Test
    void testRegisterPlayer() throws SQLException {

        PreparedStatement mockStatement_temp = mock(PreparedStatement.class);
        ResultSet mockResultSet_temp = mock(ResultSet.class);
        when(mockConnection.prepareStatement(PlayerServiceJDBC.SELECT_PLAYER)).thenReturn(mockStatement_temp);
        when(mockStatement_temp.executeQuery()).thenReturn(mockResultSet_temp);
        when(mockResultSet_temp.next()).thenReturn(false);

        when(mockConnection.prepareStatement(PlayerServiceJDBC.INSERT_PLAYER)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        playerService.registerPlayer("Player1");

        verify(mockConnection).prepareStatement(PlayerServiceJDBC.SELECT_PLAYER);
        verify(mockStatement, times(1)).setString(eq(1), eq("Player1"));

        verify(mockConnection).prepareStatement(PlayerServiceJDBC.INSERT_PLAYER);
        verify(mockStatement, times(1)).setString(eq(1), eq("Player1"));
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    void TestPlayerExists() throws SQLException{
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        boolean exists = playerService.playerExists("Player1");

        assertTrue(exists);
        verify(mockStatement, times(1)).setString(1, "Player1");
    }

    @Test
    void TestPlayerDoesNotExist() throws SQLException{
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        boolean exists = playerService.playerExists("Player1");

        assertTrue(!exists);
        verify(mockStatement, times(1)).setString(1, "Player1");
    }

    @Test
    void TestGetPlayer() throws SQLException{
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("score")).thenReturn(120);

        Player player = playerService.getPlayer("Player1");

        assertNotNull(player);
        assertEquals("Player1", player.getNickname());
        assertEquals(120, player.getScore());
    }

    @Test
    void TestUpdatePlayerScore() throws SQLException{
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        Player player = new Player("Player1");
        player.setScore(100);
        player.increaseGamesPlayed();

        playerService.updatePlayer(player);

        verify(mockStatement, times(1)).setInt(eq(1), eq(100));
        verify(mockStatement, times(1)).setString(eq(4), eq("Player1"));
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    void testReset() throws SQLException {
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        playerService.reset();

        verify(mockStatement, times(1)).executeUpdate("DELETE FROM player");
    }

}
