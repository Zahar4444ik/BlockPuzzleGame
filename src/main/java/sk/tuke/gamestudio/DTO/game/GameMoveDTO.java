package sk.tuke.gamestudio.DTO.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GameMoveDTO {
//    private String move;          // Action type (e.g., "PLACE", "REMOVE")
    private Integer row;            // Row for the action (nullable)
    private Integer col;            // Column for the action (nullable)
    private Integer blockIndex;     // Index of the block in blocks list for placement (nullable)
    private Integer offsetRow;      // Offset row for block placement (nullable)
    private Integer offsetCol;      // Offset column for block placement (nullable)
}
