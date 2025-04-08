package sk.tuke.gamestudio.game.BlockPuzzle.consoleui.inputHandler;

import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.GamePrinter;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Board;
import sk.tuke.gamestudio.game.BlockPuzzle.core.Position;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Block;
import sk.tuke.gamestudio.game.BlockPuzzle.levels.GameLevels;

import java.util.List;
import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public int getIntInput(int min, int max) {
        boolean validInput = false;
        int value = -1;

        while (!validInput) {
            if (this.scanner.hasNextInt()) {
                value = this.scanner.nextInt();
                if (value >= min && value <= max) {
                    validInput = true;
                    continue;
                }
            }
            this.scanner.nextLine();
            GamePrinter.invalidInput();
        }
        return value;
    }

    public Block chooseBlock(List<Block> blocks) {
        GamePrinter.askForBlock(blocks);
        return blocks.get(getIntInput(1, blocks.size()) - 1);
    }

    public Position getBoardPosition(Board board) {
        GamePrinter.askForPosition();
        int row = getIntInput(0, board.getRows() - 1);
        int col = getIntInput(0, board.getCols() - 1);
        return new Position(row, col);
    }

    public GameLevels chooseLevel() {
        GamePrinter.displayGameLevels();

        String input = null;
        while (!GameLevels.isValidLevel(input)) {
            GamePrinter.askForLevel();
            input = scanner.next().trim().toUpperCase();

            if (!GameLevels.isValidLevel(input)) {
                GamePrinter.invalidInput();
            }
        }

        return GameLevels.valueOf(input);
    }

    public Move chooseMove() {
        GamePrinter.askForNextMove();
        int move = getIntInput(1, 3);
        return Move.fromNumber(move);
    }

    public AuthAction getAuthAction(){
        GamePrinter.askForAuthAction();
        int action = getIntInput(1, 3);
        return AuthAction.fromNumber(action);
    }

    public String enterPlayerNickname() {
        String nickname;
        GamePrinter.askForPlayerNickname();
        do {
            nickname = scanner.nextLine();
        } while (nickname.length() > 64 || nickname.isBlank());
        return nickname;
    }

    public String enterPlayerPassword() {
        String password;
        GamePrinter.askForPlayerPassword();
        do {
            password = scanner.nextLine();
        } while (password.length() > 64 || password.isBlank());
        return password;
    }

    public boolean getYesNoAnswer() {
        String input;
        do {
            input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }
        } while (!input.equals("y") && !input.equals("yes") && !input.equals("n") && !input.equals("no"));

        return false;
    }

    public String getComment() {
        String input;
        do {
            GamePrinter.enterComment();
            input = scanner.nextLine().trim();
        } while (input.isBlank());
        return input;
    }

    public String getRating() {
        String input;
        do {
            GamePrinter.enterRating();
            input = scanner.nextLine().trim();
        } while (input.isBlank());
        return input;
    }
}
