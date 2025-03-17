package sk.tuke.kpi.BlockPuzzle.game.levels;

public class LevelFactory {
    public static Level createLevel(GameLevels level) {
        return switch (level) {
            case EASY -> new EasyLevel();
            case MEDIUM -> new MediumLevel();
            case HARD -> new HardLevel();
            default -> null;
        };
    }
}
