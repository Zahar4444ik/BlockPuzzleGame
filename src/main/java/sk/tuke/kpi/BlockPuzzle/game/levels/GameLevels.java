package sk.tuke.kpi.BlockPuzzle.game.levels;

import lombok.Getter;

@Getter
public enum GameLevels {
    EASY(5, 5, "Beginner-friendly, small board."),
    MEDIUM(7, 7, "More challenging, larger board."),
    HARD(10, 10, "Big board, requires strategy."),
//    RANDOM(0,0, "Random board size, good luck!"),
    EXIT(-1, -1, "Type 'exit' to leave");


    private final int rows;
    private final int cols;
    private final String description;

    GameLevels(int rows, int cols, String description){
        this.rows = rows;
        this.cols = cols;
        this.description = description;
    }

    public static boolean isValidLevel(String input) {
        for (GameLevels level : GameLevels.values()) {
            if (level.name().equals(input)) {
                return true;
            }
        }
        return false;
    }
}
