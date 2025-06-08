package sk.tuke.gamestudio.DTO.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GameUpdateRequestDTO {
    private GameStateDTO currentState;
    private GameMoveDTO move;
}
