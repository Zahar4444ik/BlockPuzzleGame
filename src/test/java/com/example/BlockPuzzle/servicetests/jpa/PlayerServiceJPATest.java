package com.example.BlockPuzzle.servicetests.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.service.jpa.PlayerServiceJPA;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerServiceJPATest {
// TODO: Complete tests
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private PlayerServiceJPA playerServiceJPA;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegister() {
        String nickname = "testPlayer";
        String password = "testPassword";

        Player player = new Player(nickname, password);
        when(entityManager.createNamedQuery("Player.getPlayer", Player.class))
                .thenThrow(NoResultException.class);

        // Test successful registration
        boolean result = playerServiceJPA.register(nickname, password);
        assertTrue(result);
        verify(entityManager).persist(any(Player.class));

        // Test player already exists
        when(entityManager.createNamedQuery("Player.getPlayer", Player.class))
                .thenReturn(mock(TypedQuery.class));
        boolean resultAlreadyExists = playerServiceJPA.register(nickname, password);
        assertFalse(resultAlreadyExists);
    }

    @Test
    public void testLogin() {
        String nickname = "testPlayer";
        String password = "testPassword";

        // Mock EntityManager to return a player
        Player player = new Player(nickname, "hashedPassword");
        when(entityManager.createNamedQuery("Player.getPlayer", Player.class))
                .thenReturn(mock(TypedQuery.class));
        when(entityManager.createNamedQuery("Player.getPlayer", Player.class)
                .setParameter("nickname", nickname).getSingleResult()).thenReturn(player);

        // Test successful login
        boolean result = playerServiceJPA.login(nickname, password);
        assertTrue(result);

        // Test login with wrong password
        when(player.getPassword()).thenReturn("wrongHashedPassword");
        boolean resultWrongPassword = playerServiceJPA.login(nickname, password);
        assertFalse(resultWrongPassword);
    }

    @Test
    public void testPlayerExists() {
        String nickname = "testPlayer";

        // Mock EntityManager
        when(entityManager.createNamedQuery("Player.getPlayer", Player.class)
                .setParameter("nickname", nickname).getSingleResult())
                .thenReturn(new Player(nickname, "password"));

        // Test player exists
        assertTrue(playerServiceJPA.playerExists(nickname));

        // Test player does not exist
        when(entityManager.createNamedQuery("Player.getPlayer", Player.class)
                .setParameter("nickname", nickname).getSingleResult())
                .thenThrow(NoResultException.class);
        assertFalse(playerServiceJPA.playerExists(nickname));
    }

    @Test
    public void testGetPlayer() {
        String nickname = "testPlayer";

        // Mock EntityManager to return a player
        Player player = new Player(nickname, "password");
        when(entityManager.createNamedQuery("Player.getPlayer", Player.class)
                .setParameter("nickname", nickname).getSingleResult())
                .thenReturn(player);

        // Test retrieving player
        Player retrievedPlayer = playerServiceJPA.getPlayer(nickname);
        assertEquals(player, retrievedPlayer);

        // Test player does not exist
        when(entityManager.createNamedQuery("Player.getPlayer", Player.class)
                .setParameter("nickname", nickname).getSingleResult())
                .thenThrow(NoResultException.class);
        Player noPlayer = playerServiceJPA.getPlayer(nickname);
        assertNull(noPlayer);
    }

    @Test
    public void testUpdatePlayer() {
        Player player = new Player("testPlayer", "newPassword");

        when(entityManager.merge(player)).thenReturn(player);
        playerServiceJPA.updatePlayer(player);
        verify(entityManager).merge(player);
    }

    @Test
    public void testReset() {
        var mockQuery = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(eq("Player.resetPlayers"))).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);

        playerServiceJPA.reset();

        verify(entityManager).createNamedQuery(eq("Player.resetPlayers"));
        verify(mockQuery).executeUpdate();
    }

    @Test
    public void testInvalidRegister() {
        assertFalse(playerServiceJPA.register("", "password"));
        assertFalse(playerServiceJPA.register("validPlayer", ""));
    }
}