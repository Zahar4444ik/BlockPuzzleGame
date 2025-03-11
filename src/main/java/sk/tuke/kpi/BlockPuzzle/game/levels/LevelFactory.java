package sk.tuke.kpi.BlockPuzzle.game.levels;

public class LevelFactory {
    public static Level createLevel(GameLevels level) {
        switch (level){
            case EASY:
                return new EasyLevel();
            case MEDIUM:
                return new MediumLevel();
            case HARD:
                return new HardLevel();
            case EXPERT:
                return new EasyLevel();
            default:
                return null;
        }
    }
}
