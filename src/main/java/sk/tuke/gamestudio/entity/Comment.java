package sk.tuke.gamestudio.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@NamedQuery(name = "Comment.getComments",
            query = "SELECT c FROM Comment c WHERE c.game = :game ORDER BY c.commentedOn DESC")
@NamedQuery(name = "Comment.resetComments",
            query = "DELETE FROM Comment")
public class Comment implements Serializable {

    @Id
    @GeneratedValue
    int ident;

    private String game;
    @ManyToOne
    @JoinColumn(name = "player")
    private Player player;
    private String comment;
    private Date commentedOn;

    public Comment() {}

    public Comment(String game, Player player, String comment, Date commentedOn) {
        this.game = game;
        this.player = player;
        this.comment = comment;
        this.commentedOn = commentedOn;
    }

}
