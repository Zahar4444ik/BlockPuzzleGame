package sk.tuke.gamestudio.service.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.exceptions.ScoreException;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.List;

@Transactional
@Service
public class ScoreServiceJPA implements ScoreService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addAndSetScore(Score score) throws ScoreException {
        if (isNotValidScore(score)){
            throw new ScoreException("Invalid score");
        }

        try {
            Score previousScore = this.entityManager.createNamedQuery("Score.getExistingPlayerScore", Score.class)
                    .setParameter("game", score.getGame()).setParameter("player_nickname", score.getPlayer()).getSingleResult();

            entityManager.createNamedQuery("Score.updateExistingPlayerScore")
                    .setParameter("points", score.getPoints())
                    .setParameter("played_on", score.getPlayedOn())
                    .setParameter("player_nickname", score.getPlayer())
                    .setParameter("game", score.getGame())
                    .executeUpdate();
        }catch (NoResultException e) {
            this.entityManager.persist(score);
        }
    }

    @Override
    public List<Score> getTopScores(String game) throws ScoreException {
        return this.entityManager.createNamedQuery("Score.getTopScores", Score.class)
                .setParameter("game", game).setMaxResults(10).getResultList();
    }

    @Override
    public void reset() throws ScoreException {
        this.entityManager.createNamedQuery("Score.resetScores").executeUpdate();
    }

    private boolean isNotValidScore(Score score) {
        return score == null || score.getGame() == null || score.getPlayer() == null || score.getPoints() < 0 || score.getPlayedOn() == null;
    }
}
