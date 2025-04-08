package sk.tuke.gamestudio.service.jpa;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.exceptions.CommentException;
import sk.tuke.gamestudio.service.CommentService;

import java.util.List;

@Service
@Transactional
public class CommentServiceJPA implements CommentService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addComment(Comment comment) throws CommentException {
        if (isNotValidComment(comment)) {
            throw new CommentException("Invalid comment");
        }
        this.entityManager.persist(comment);
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        return this.entityManager.createNamedQuery("Comment.getComments", Comment.class)
                .setParameter("game", game).getResultList();
    }

    @Override
    public void reset() throws CommentException {
        this.entityManager.createNamedQuery("Comment.resetComments").executeUpdate();
    }

    private boolean isNotValidComment(Comment comment) {
        return comment == null ||
                comment.getGame() == null ||
                comment.getPlayer() == null ||
                comment.getComment() == null;
    }
}
