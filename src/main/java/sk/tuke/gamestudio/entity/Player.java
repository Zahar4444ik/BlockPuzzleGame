package sk.tuke.gamestudio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sk.tuke.gamestudio.service.jdbc.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@ToString(exclude = "password")
@NamedQuery(name = "Player.getPlayer",
        query = "SELECT p FROM Player p WHERE p.nickname = :nickname")
@NamedQuery(name = "Player.updatePlayer",
        query = "UPDATE Player p SET p.score = :score, p.gamesPlayed = :gamesPlayed, p.lastPlayed = :lastPlayed WHERE p.nickname = :nickname")

public class Player implements Serializable {
    @Id
    private String nickname;

    private int score;
    private int gamesPlayed;
    private Date lastPlayed;
    private String password;

    public Player(String nickname, String password){
        this.nickname = nickname;
        this.password = password;
        this.score = 0;
        this.gamesPlayed = 0;
        this.lastPlayed = new Date();
    }

    public Player() {

    }

    public void increaseGamesPlayed(){
        this.gamesPlayed++;
    }

    public void increaseScore(int score){
        this.score += score;
    }

    public void decreaseScore(int score){
        this.score -= score;
    }

    public void saveScore(String game){
        try {
            final ScoreServiceJDBC scoreService = new ScoreServiceJDBC();
            scoreService.addAndSetScore(new Score(game, this, this.getScore(), new Date()));
        } catch (ScoreException ignored) {
        }
    }

    public void setRating(String game, String rating){
        int rate;
        try {
            rate = Integer.parseInt(rating);
        } catch (NumberFormatException e) {
            return;
        }
        if(rate < 1 || rate > 5){
            System.err.println("Rating must be between 1 and 5");
            return;
        }

        try {
            final RatingServiceJDBC ratingService = new RatingServiceJDBC();
            ratingService.setAndAddRating(new Rating(game, this, rate, new Date()));
        } catch (RatingException e){
            System.err.println("Error saving rating: " + e.getMessage());
        }
    }

    public void addComment(String game, String comment){
        try{
            final CommentServiceJDBC commentService = new CommentServiceJDBC();
            commentService.addComment(new Comment(game, this, comment, new Date()));
        } catch (CommentException e){
            System.err.println("Error saving comment: " + e.getMessage());
        }
    }

}
