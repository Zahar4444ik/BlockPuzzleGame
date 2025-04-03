package sk.tuke.gamestudio.entity;

import lombok.Getter;
import lombok.Setter;
import sk.tuke.gamestudio.service.jdbc.*;

import java.util.Date;

@Getter
@Setter
public class Player {
    private final String nickname;
    private int score;
    private int gamesPlayed;
    private Date lastPlayed;

    public Player(String nickname){
        this.nickname = nickname;
        this.score = 0;
        this.gamesPlayed = 0;
        this.lastPlayed = new Date();
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
            scoreService.addAndSetScore(new Score(game, this.getNickname(), this.getScore(), new Date()));
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
            ratingService.setAndAddRating(new Rating(game, this.getNickname(), rate, new Date()));
        } catch (RatingException e){
            System.err.println("Error saving rating: " + e.getMessage());
        }
    }

    public void addComment(String game, String comment){
        try{
            final CommentServiceJDBC commentService = new CommentServiceJDBC();
            commentService.addAndSetComment(new Comment(game, this.getNickname(), comment, new Date()));
        } catch (CommentException e){
            System.err.println("Error saving comment: " + e.getMessage());
        }
    }

    @Override
    public String toString(){
        return "Player: " + nickname + ", Score: " + score;
    }

}
