package sk.tuke.kpi.BlockPuzzle.game.levels;

import sk.tuke.kpi.BlockPuzzle.game.levels.randomLevel.RandomLevel;

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
