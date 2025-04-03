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

@Setter
@Getter
@ToString
@Entity
@NamedQuery(name = "Score.getTopScores",
            query = "SELECT s FROM Score s WHERE s.game = :game ORDER BY s.points DESC")
@NamedQuery(name = "Score.resetScores",
            query = "DELETE FROM Score")
@NamedQuery(name = "Score.updateExistingPlayerScore",
            query = "UPDATE Score s SET s.points = :points, s.playedOn = :playedOn WHERE s.game = :game AND s.player = :player")
@NamedQuery(name = "Score.getExistingPlayerScore",
            query = "SELECT s FROM Score s WHERE s.game = :game AND s.player = :player")
public class Score implements Serializable {
    private String game;

    private String player;

    private int points;

    private Date playedOn;

    @Id
    @GeneratedValue
    private int ident;

    public Score() {}

    public Score(String game, String player, int points, Date playedOn) {
        this.game = game;
        this.player = player;
        this.points = points;
        this.playedOn = playedOn;
    }
}
