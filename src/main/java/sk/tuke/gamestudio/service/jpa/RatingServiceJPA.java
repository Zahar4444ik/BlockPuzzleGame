package sk.tuke.gamestudio.service.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.exceptions.RatingException;
import sk.tuke.gamestudio.service.RatingService;

@Service
@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setAndAddRating(Rating rating) throws RatingException {
        if (isNotValidRating(rating)){
            throw new RatingException("Invalid rating");
        }

        try {
            Rating previousPlayerRating = this.entityManager.createNamedQuery("Rating.getPlayerRating", Rating.class)
                    .setParameter("game", rating.getGame()).setParameter("player", rating.getPlayer()).getSingleResult();

            this.entityManager.createNamedQuery("Rating.updateExistingPlayerRating")
                    .setParameter("rating", rating.getRating()).setParameter("ratedOn", rating.getRatedOn())
                    .setParameter("player", rating.getPlayer()).setParameter("game", rating.getGame()).executeUpdate();
        }
        catch (NoResultException e) {
            this.entityManager.persist(rating);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        if (isNotValidParameter(game)) {
            throw new RatingException("Invalid game name");
        }

        final var averageRating = (Double) this.entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game)
                .getSingleResult();

        if (averageRating == null) {
            return 0;
        }

        return averageRating.intValue();
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        if (isNotValidParameter(game) || isNotValidParameter(player)){
            throw new RatingException("Invalid game name or player name");
        }

        try {
            return this.entityManager.createNamedQuery("Rating.getPlayerRating", Integer.class)
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public void reset() throws RatingException {
        this.entityManager.createNamedQuery("Rating.resetRatings").executeUpdate();
    }

    private boolean isNotValidRating(Rating rating) {
        return rating == null ||
                rating.getGame() == null ||
                rating.getPlayer() == null ||
                rating.getRating() < 1 ||
                rating.getRating() > 5;
    }

    private boolean isNotValidParameter(String parameter) {
        return parameter == null || parameter.isBlank();
    }
}
