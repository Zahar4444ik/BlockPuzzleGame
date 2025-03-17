package sk.tuke.kpi.BlockPuzzle.game.core;

import lombok.Getter;
import lombok.Setter;
import sk.tuke.kpi.BlockPuzzle.gamestudio.entity.Comment;
import sk.tuke.kpi.BlockPuzzle.gamestudio.entity.Rating;
import sk.tuke.kpi.BlockPuzzle.gamestudio.entity.Score;
import sk.tuke.kpi.BlockPuzzle.gamestudio.service.*;

import java.util.Date;

@Getter
@Setter
public class Player {
    private final String nickname;
    private int score;

    public Player(String nickname){
        this.nickname = nickname;
        this.score = 0;
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
            scoreService.addScore(new Score(game, this.getNickname(), this.getScore(), new Date()));
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
            ratingService.setRating(new Rating(game, this.getNickname(), rate, new Date()));
        } catch (RatingException e){
            System.err.println("Error saving rating: " + e.getMessage());
        }
    }

    public void addComment(String game, String comment){
        try{
            final CommentServiceJDBC commentService = new CommentServiceJDBC();
            commentService.addComment(new Comment(game, this.getNickname(), comment, new Date()));
        } catch (CommentException e){
            System.err.println("Error saving comment: " + e.getMessage());
        }
    }

    @Override
    public String toString(){
        return "Player: " + nickname + ", Score: " + score;
    }

}
