package sk.tuke.gamestudio.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class PlayerDTO {
    private String nickname;
    private int score;
    private int gamesPlayed;
    private Date lastPlayed;
    private String password;
}
