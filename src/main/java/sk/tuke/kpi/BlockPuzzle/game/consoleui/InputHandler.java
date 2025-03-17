package sk.tuke.kpi.BlockPuzzle.game.consoleui;

import sk.tuke.kpi.BlockPuzzle.game.core.Board;
import sk.tuke.kpi.BlockPuzzle.game.core.Position;
import sk.tuke.kpi.BlockPuzzle.game.core.blocks.Block;
import sk.tuke.kpi.BlockPuzzle.game.Move;
import sk.tuke.kpi.BlockPuzzle.game.levels.GameLevels;

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

    public String enterPlayerNickname() {
        String nickname;
        do {
            GamePrinter.askForPlayerNickname();
            nickname = scanner.nextLine();
        } while (nickname.length() > 64 || nickname.isBlank());
        return nickname;
    }

    public char getYesNoAnswer() {
        String input;
        do {
            input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return 'y';
            } else if (input.equals("n") || input.equals("no")) {
                return 'n';
            } else {
                GamePrinter.invalidInput();
            }
        } while (!input.equals("y") && !input.equals("yes") && !input.equals("n") && !input.equals("no"));

        return 'n';
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
