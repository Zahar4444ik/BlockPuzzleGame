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
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.exceptions.ScoreException;
import sk.tuke.gamestudio.service.jpa.ScoreServiceJPA;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ScoreServiceJPATest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ScoreServiceJPA scoreServiceJPA;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TestAddScore() throws ScoreException{
        var player = new Player("testPlayer", "testPassword");
        var score = new Score("testGame", player, 100, new Date());

        var typedQuery = mock(TypedQuery.class);
        when(this.entityManager.createNamedQuery(eq("Score.getExistingPlayerScore"), eq(Score.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter("player_nickname", player)).thenReturn(typedQuery);
        when(typedQuery.setParameter("game", score.getGame())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenThrow(NoResultException.class);

        this.scoreServiceJPA.addAndSetScore(score);
        verify(this.entityManager).persist(score);
    }

    @Test
    public void TestGetTopScores() throws ScoreException{
        var game = "testGame";
        var player1 = new Player("testPlayer1", "testPassword");
        var player2 = new Player("testPlayer2", "testPassword");
        var expectedScores = List.of(new Score(game, player1, 100, new Date()),
                new Score(game, player2, 200, new Date()));

        var typedQuery = mock(TypedQuery.class);

        when(this.entityManager.createNamedQuery(eq("Score.getTopScores"), eq(Score.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedScores);

        var topScores = this.scoreServiceJPA.getTopScores(game);

        assertEquals(expectedScores, topScores);

    }

    @Test
    public void TestReset() throws ScoreException {
        var mockQuery = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(eq("Score.resetScores"))).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);

        scoreServiceJPA.reset();

        verify(entityManager).createNamedQuery(eq("Score.resetScores"));
        verify(mockQuery).executeUpdate();
    }

    @Test
    public void TestAddScoreNullGame() {
        var player = new Player("player1", "password");
        var score = new Score(null, player, 100, new Date());
        assertThrows(ScoreException.class, () -> scoreServiceJPA.addAndSetScore(score));
        verify(entityManager, never()).persist(any());
    }

    @Test
    public void TestAddScoreNegativePoints() {
        var player = new Player("player1", "password");
        var score = new Score("testGame", player, -50, new Date());
        assertThrows(ScoreException.class, () -> scoreServiceJPA.addAndSetScore(score));
        verify(entityManager, never()).persist(any());
    }
}
