package sk.tuke.gamestudio.DTO.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BlockDTO {
    private CellDTO[][] shape;
    private int[] color;
}
