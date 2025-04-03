package sk.tuke.gamestudio.service.jdbc;

import sk.tuke.gamestudio.entity.Rating;

public interface RatingService {
    void setAndAddRating(Rating rating) throws RatingException;
    int getAverageRating(String game) throws RatingException;
    int getRating(String game, String player) throws RatingException;
    void reset() throws RatingException;
}
