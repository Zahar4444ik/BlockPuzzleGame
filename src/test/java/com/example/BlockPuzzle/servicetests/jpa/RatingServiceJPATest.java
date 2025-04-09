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
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.exceptions.RatingException;
import sk.tuke.gamestudio.service.jpa.RatingServiceJPA;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RatingServiceJPATest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private RatingServiceJPA ratingServiceJPA;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TestSetRating() throws RatingException {
        var player = new Player("testPlayer", "testPassword");
        var rating = new Rating("testGame", player, 5, new Date());

        var typedQuery = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(eq("Rating.getPlayerRating"), eq(Rating.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenThrow(NoResultException.class);

        ratingServiceJPA.setAndAddRating(rating);

        verify(entityManager).persist(rating);
    }

    @Test
    public void TestSetRatingException() {
        var player = new Player("testPlayer", "testPassword");
        var rating = new Rating("testGame", player, 5, null);

        var typedQuery = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(eq("Rating.getPlayerRating"), eq(Rating.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenThrow(NoResultException.class);

        assertThrows(RatingException.class, () -> ratingServiceJPA.setAndAddRating(rating));
    }

    @Test
    public void TestSetRatingInvalidRating() {
        var player = new Player("testPlayer", "testPassword");
        var invalidRating = new Rating("testGame", player, 6, new Date());
        assertThrows(RatingException.class, () -> ratingServiceJPA.setAndAddRating(invalidRating));
    }

    @Test
    public void TestSetRatingNullPlayerOrGame() {
        var ratingNullPlayer = new Rating("testGame", null, 4, new Date());
        var ratingNullGame = new Rating("player1", null, 3, new Date());
        assertThrows(RatingException.class, () -> ratingServiceJPA.setAndAddRating(ratingNullPlayer));
        assertThrows(RatingException.class, () -> ratingServiceJPA.setAndAddRating(ratingNullGame));
    }

    @Test
    public void TestGetAverageRating() throws RatingException {
        var game = "game1";
        var expectedAverageRating = 4.5;

        var typedQuery = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(eq("Rating.getAverageRating")))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(expectedAverageRating);

        var averageRating = ratingServiceJPA.getAverageRating(game);

        assertEquals(4, averageRating);
    }

    @Test
    public void TestReset() throws RatingException {
        var mockQuery = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(eq("Rating.resetRatings"))).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);

        ratingServiceJPA.reset();

        verify(entityManager).createNamedQuery(eq("Rating.resetRatings"));
        verify(mockQuery).executeUpdate();
    }

    @Test
    public void TestGetRating() throws RatingException {
        var game = "game1";
        var player = new Player("testPlayer", "testPassword");
        int expectedRating = 4;

        var typedQuery = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(eq("Rating.getPlayerRating"), eq(Integer.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(expectedRating);

        var playerRating = ratingServiceJPA.getRating(game, player.getNickname());

        assertEquals(expectedRating, playerRating);
    }

    @Test
    public void TestGetRatingException() {
        String game = null;
        String player = null;
        assertThrows(RatingException.class, () -> ratingServiceJPA.getRating(game, player));
    }
}
