package sk.tuke.gamestudio.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class RatingDTO {
    private String game;
    private PlayerDTO playerDTO;
    private int rating;
    private Date ratedOn;
}
