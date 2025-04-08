package sk.tuke.gamestudio.game.BlockPuzzle.consoleui.inputHandler;

public enum AuthAction {
    LOGIN(1),
    REGISTER(2),
    EXIT(3);

    final int num;
    AuthAction(int num) {
        this.num = num;
    }

    public static AuthAction fromNumber(int number) {
        for (AuthAction action : AuthAction.values()) {
            if (action.num == number) {
                return action;
            }
        }
        return null;
    }
}
