package sk.tuke.gamestudio.DTO.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GameMoveDTO {
    private String move;
    private Integer row;
    private Integer col;
    private Integer blockIndex;
    private Integer offsetRow;
    private Integer offsetCol;
}
