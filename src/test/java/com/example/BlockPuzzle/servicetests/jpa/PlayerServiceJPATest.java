package com.example.BlockPuzzle.servicetests.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.service.jpa.PlayerServiceJPA;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerServiceJPATest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private PlayerServiceJPA playerServiceJPA;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TestRegister() {
        String nickname = "testPlayer";
        String password = "testPassword";

        Player player = new Player(nickname, password);
        TypedQuery<Player> mockQuery = mock(TypedQuery.class);

        when(entityManager.createNamedQuery("Player.getPlayer", Player.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("nickname", nickname)).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(player);

        boolean resultAlreadyExists = playerServiceJPA.register(nickname, password);
        assertFalse(resultAlreadyExists);
    }

    @Test
    public void TestLogin() {
        String nickname = "testPlayer";
        String rawPassword = "testPassword";

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(rawPassword);

        Player player = mock(Player.class);
        when(player.getNickname()).thenReturn(nickname);
        when(player.getPassword()).thenReturn(hashedPassword);

        TypedQuery<Player> mockQuery = mock(TypedQuery.class);
        when(entityManager.createNamedQuery("Player.getPlayer", Player.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("nickname", nickname)).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(player);

        boolean result = playerServiceJPA.login(nickname, rawPassword);
        assertTrue(result);
    }

    @Test
    public void TestPlayerExists() {
        String nickname = "testPlayer";

        TypedQuery<Player> mockQuery = mock(TypedQuery.class);
        Player player = mock(Player.class);
        when(entityManager.createNamedQuery("Player.getPlayer", Player.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("nickname", nickname)).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(player);


        assertTrue(playerServiceJPA.playerExists(nickname));

        when(entityManager.createNamedQuery("Player.getPlayer", Player.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("nickname", nickname)).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenThrow(NoResultException.class);
        assertFalse(playerServiceJPA.playerExists(nickname));
    }

    @Test
    public void TestGetPlayer() {
        String nickname = "testPlayer";
        Player player = new Player(nickname, "password");

        TypedQuery<Player> mockQuery = mock(TypedQuery.class);
        when(entityManager.createNamedQuery("Player.getPlayer", Player.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("nickname", nickname)).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(player);

        Player retrievedPlayer = playerServiceJPA.getPlayer(nickname);
        assertEquals(player, retrievedPlayer);

        when(mockQuery.getSingleResult()).thenThrow(NoResultException.class);

        Player noPlayer = playerServiceJPA.getPlayer(nickname);
        assertNull(noPlayer);
    }

    @Test
    public void TestUpdatePlayer() {
        Player player = new Player("testPlayer", "newPassword");

        when(entityManager.merge(player)).thenReturn(player);
        playerServiceJPA.updatePlayer(player);
        verify(entityManager).merge(player);
    }

    @Test
    public void TestReset() {
        var mockQuery = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(eq("Player.resetPlayers"))).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);

        playerServiceJPA.reset();

        verify(entityManager).createNamedQuery(eq("Player.resetPlayers"));
        verify(mockQuery).executeUpdate();
    }

    @Test
    public void TestInvalidRegister() {
        assertFalse(playerServiceJPA.register("", "password"));
        assertFalse(playerServiceJPA.register("validPlayer", ""));
    }
}