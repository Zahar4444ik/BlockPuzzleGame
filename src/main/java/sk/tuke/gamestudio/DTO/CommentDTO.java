package sk.tuke.gamestudio.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
public class CommentDTO {
    private String game;
    private PlayerDTO playerDTO;
    private String comment;
    private Date commentedOn;
}
