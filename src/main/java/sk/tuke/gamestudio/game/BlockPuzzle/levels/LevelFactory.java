package sk.tuke.gamestudio.game.BlockPuzzle.levels;

public class LevelFactory {
    public static Level createLevel(GameLevels level) {
        return switch (level) {
            case EASY -> new EasyLevel();
            case MEDIUM -> new MediumLevel();
            case HARD -> new HardLevel();
//            case RANDOM -> new RandomLevel();
            default -> null;
        };
    }
}
