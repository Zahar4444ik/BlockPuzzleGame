package sk.tuke.gamestudio.DTO.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CellDTO {
    private String state;
    private int[] color;
}
