package com.example.BlockPuzzle.servicetests.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.service.exceptions.CommentException;
import sk.tuke.gamestudio.service.jpa.CommentServiceJPA;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CommentServiceJPATest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CommentServiceJPA commentServiceJPA;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TestAddComment() throws CommentException {
        var player = new Player("testPlayer", "testPassword");
        final var comment = new Comment("testGame", player, "Great game!", new Date());

        commentServiceJPA.addComment(comment);

        verify(entityManager).persist(comment);
    }

    @Test
    public void TestAddCommentNullPlayer() {
        final var comment = new Comment("testGame", null, "Great game!", new Date());

        assertThrows(CommentException.class, () -> commentServiceJPA.addComment(comment));
        verify(entityManager, never()).persist(any());
    }

    @Test
    public void TestAddCommentNullGame() {
        var player = new Player("testPlayer", "testPassword");
        var comment = new Comment(null, player, "Great game!", new Date());

        assertThrows(CommentException.class, () -> commentServiceJPA.addComment(comment));
        verify(entityManager, never()).persist(any());
    }

    @Test
    public void TestAddCommentNullComment() {
        var player = new Player("testPlayer", "testPassword");
        var comment = new Comment("testGame", player, null, new Date());

        assertThrows(CommentException.class, () -> commentServiceJPA.addComment(comment));
        verify(entityManager, never()).persist(any());
    }

    @Test
    public void TestAddCommentNullCommentedOn() {
        var player = new Player("testPlayer", "testPassword");
        var comment = new Comment("testGame", player, "Great game!", null);

        assertThrows(CommentException.class, () -> commentServiceJPA.addComment(comment));
        verify(entityManager, never()).persist(any());
    }

    @Test
    public void TestGetComments() throws CommentException {
        var game = "game1";
        var player1 = new Player("testPlayer1", "testPassword");
        var player2 = new Player("testPlayer2", "testPassword");

        var expectedComments = new ArrayList<>();
        expectedComments.add(new Comment("testGame1", player1, "Great game!", new Date()));
        expectedComments.add(new Comment("testGame2", player2, "Awesome!", new Date()));

        var typedQuery = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(eq("Comment.getComments"), eq(Comment.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedComments);

        var comments = commentServiceJPA.getComments(game);

        assertEquals(expectedComments.size(), comments.size());
        assertEquals(expectedComments, comments);
    }

    @Test
    public void TestReset() throws CommentException {
        var mockQuery = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(eq("Comment.resetComments"))).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);

        commentServiceJPA.reset();

        verify(entityManager).createNamedQuery(eq("Comment.resetComments"));
        verify(mockQuery).executeUpdate();
    }
}