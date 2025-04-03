package sk.tuke.gamestudio.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@NamedQuery(name = "Rating.getAverageRating",
            query = "SELECT AVG(r.rating) FROM Rating r WHERE r.game = :game")
@NamedQuery(name = "Rating.getRating",
            query = "SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player")
@NamedQuery(name = "Rating.resetRatings",
            query = "DELETE FROM Rating")
@NamedQuery(name = "Rating.updateExistingPlayerRating",
            query = "UPDATE Rating r SET r.rating = :rating, r.ratedOn = :ratedOn WHERE r.game = :game AND r.player = :player")
@NamedQuery(name = "Rating.getExistingPlayerRating",
            query = "SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player")
public class Rating implements Serializable {

    @Id
    @GeneratedValue
    private int ident;

    private String game;
    private String player;
    private int rating; // 1 to 5 stars
    private Date ratedOn;

    public Rating() {
    }

    public Rating(String game, String player, int rating, Date ratedOn) {
        this.game = game;
        this.player = player;
        this.rating = rating;
        this.ratedOn = ratedOn;
    }
}
