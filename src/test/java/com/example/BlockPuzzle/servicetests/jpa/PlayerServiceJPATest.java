package com.example.BlockPuzzle.servicetests.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.service.jpa.PlayerServiceJPA;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerServiceJPATest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PlayerServiceJPA playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() {
        when(passwordEncoder.encode("testPass")).thenReturn("hashedPass");
        when(entityManager.contains(any(Player.class))).thenReturn(false);

        boolean result = playerService.register("testUser", "testPass");
        assertTrue(result, "Registration should succeed");
        verify(entityManager).persist(any(Player.class));
    }

    @Test
    void testRegisterExistingPlayer() {
        when(playerService.playerExists("testUser")).thenReturn(true);

        boolean result = playerService.register("testUser", "testPass");
        assertFalse(result, "Registration should fail for existing player");
        verify(entityManager, never()).persist(any(Player.class));
    }

    @Test
    void testLoginSuccess() {
        Player player = new Player("testUser", "hashedPass");
        when(entityManager.createNamedQuery("Player.getPlayer", Player.class)
                .setParameter("nickname", "testUser")
                .getSingleResult()).thenReturn(player);
        when(passwordEncoder.matches("testPass", "hashedPass")).thenReturn(true);

        boolean result = playerService.login("testUser", "testPass");
        assertTrue(result, "Login should succeed");
    }

    @Test
    void testLoginFailure() {
        when(entityManager.createNamedQuery("Player.getPlayer", Player.class)
                .setParameter("nickname", "testUser")
                .getSingleResult()).thenThrow(new jakarta.persistence.NoResultException());
        boolean result = playerService.login("testUser", "testPass");
        assertFalse(result, "Login should fail for non-existent player");
    }

    @Test
    void testUpdatePlayer() {
        Player player = new Player("testUser", "testPass");
        player.setScore(100);
        when(entityManager.merge(player)).thenReturn(player);

        playerService.updatePlayer(player);
        verify(entityManager).merge(player);
        assertEquals(100, player.getScore(), "Score should remain unchanged after merge");
    }
}