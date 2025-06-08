package com.example.BlockPuzzle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Player;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("testUser", "testPass");
    }

    @Test
    void testConstructor() {
        assertNotNull(player.getNickname(), "Nickname should be initialized");
        assertEquals("testUser", player.getNickname(), "Nickname should match");
        assertEquals("testPass", player.getPassword(), "Password should match");
        assertEquals(0, player.getScore(), "Initial score should be 0");
        assertEquals(0, player.getGamesPlayed(), "Initial games played should be 0");
        assertNotNull(player.getLastPlayed(), "Last played date should be initialized");
    }

    @Test
    void testIncreaseScore() {
        player.increaseScore(100);
        assertEquals(100, player.getScore(), "Score should increase by 100");
        player.increaseScore(50);
        assertEquals(150, player.getScore(), "Score should increase by additional 50");
    }

    @Test
    void testDecreaseScore() {
        player.increaseScore(200);
        player.decreaseScore(50);
        assertEquals(150, player.getScore(), "Score should decrease by 50");
        player.decreaseScore(200);
        assertEquals(0, player.getScore(), "Score should not go below 0");
    }

    @Test
    void testIncreaseGamesPlayed() {
        player.increaseGamesPlayed();
        assertEquals(1, player.getGamesPlayed(), "Games played should increase by 1");
        player.increaseGamesPlayed();
        assertEquals(2, player.getGamesPlayed(), "Games played should increase by another 1");
    }
}