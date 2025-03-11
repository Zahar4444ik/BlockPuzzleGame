package sk.tuke.kpi.BlockPuzzle.core;

import sk.tuke.kpi.BlockPuzzle.consoleui.GamePrinter;
import sk.tuke.kpi.BlockPuzzle.core.blocks.Block;
import sk.tuke.kpi.BlockPuzzle.game.Move;
import sk.tuke.kpi.BlockPuzzle.game.levels.GameLevels;

import java.util.List;
import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;

    public InputHandler(){
        this.scanner = new Scanner(System.in);
    }

    public int getIntInput(int min, int max){
        int value;
        while (true){
            if (this.scanner.hasNextInt()){
                value = this.scanner.nextInt();
                if (value >= min && value <= max){
                    return value;
                }
            }
            this.scanner.nextLine();
            GamePrinter.invalidInput();
        }
    }

    public Block chooseBlock(List<Block> blocks){
        GamePrinter.askForBlock(blocks);
        return blocks.get(getIntInput(1, blocks.size()) - 1);
    }

    public Position getBoardPosition(Board board){
        GamePrinter.askForPosition();
        int row = getIntInput(0, board.getRows() - 1);
        int col = getIntInput(0, board.getCols() - 1);
        return new Position(row, col);
    }

    public GameLevels chooseLevel(){
        GamePrinter.displayGameLevels();

        String input = null;
        while (!GameLevels.isValidLevel(input)){
            GamePrinter.askForLevel();
            input = scanner.next().trim().toUpperCase();

            if (!GameLevels.isValidLevel(input)){
                GamePrinter.invalidInput();
            }
        }

        return GameLevels.valueOf(input);
    }

    public Move chooseMove(){
        GamePrinter.askForNextMove();
        int move = getIntInput(1, 3);
        return Move.fromNumber(move);
    }
}
