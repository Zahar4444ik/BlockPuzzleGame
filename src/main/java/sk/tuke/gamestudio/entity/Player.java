package sk.tuke.gamestudio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sk.tuke.gamestudio.service.exceptions.CommentException;
import sk.tuke.gamestudio.service.exceptions.RatingException;
import sk.tuke.gamestudio.service.exceptions.ScoreException;
import sk.tuke.gamestudio.service.jdbc.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@NamedQuery(name = "Player.getPlayer",
        query = "SELECT p FROM Player p WHERE p.nickname = :nickname")
@NamedQuery(name = "Player.updatePlayer",
        query = "UPDATE Player p SET p.score = :score, p.gamesPlayed = :games_played, p.lastPlayed = :last_played WHERE p.nickname = :nickname")
@NamedQuery(name = "Player.resetPlayers",
        query = "DELETE FROM Player")
public class Player implements Serializable {
    @Id
    private String nickname;

    private int score;

    @Column(name = "games_played")
    private int gamesPlayed;

    @Column(name = "last_played")
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
        if (this.score < score) {
            this.score = 0;
            return;
        }
        this.score -= score;
    }

}
