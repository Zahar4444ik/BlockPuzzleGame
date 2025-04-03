package sk.tuke.gamestudio.service.jpa;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.jdbc.RatingException;
import sk.tuke.gamestudio.service.jdbc.RatingService;

@Service
@Transactional
public class RatingServiceJPA implements RatingService {
    @Override
    public void setAndAddRating(Rating rating) throws RatingException {

    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        return 0;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        return 0;
    }

    @Override
    public void reset() throws RatingException {

    }

    private boolean isNotValidRating(Rating rating) {
        return rating == null || rating.getGame() == null || rating.getPlayer() == null || rating.getRating() < 1 || rating.getRating() > 5;
    }
}
