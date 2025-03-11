package sk.tuke.kpi.BlockPuzzle.consoleui;

import sk.tuke.kpi.BlockPuzzle.core.*;
import sk.tuke.kpi.BlockPuzzle.core.blocks.Block;
import sk.tuke.kpi.BlockPuzzle.game.GameState;
import sk.tuke.kpi.BlockPuzzle.game.Move;
import sk.tuke.kpi.BlockPuzzle.game.levels.GameLevels;
import sk.tuke.kpi.BlockPuzzle.game.levels.Level;
import sk.tuke.kpi.BlockPuzzle.game.levels.LevelFactory;

import java.util.List;
import java.util.Objects;

public class BlockPuzzleConsole {
    private GameState gameState;
    private final InputHandler inputHandler;

    public BlockPuzzleConsole() {
        this.inputHandler = new InputHandler();
        this.gameState = GameState.MENU;
    }

    public void start() {
        GamePrinter.welcome();

        while(!this.isExit()){

            GameLevels levelDifficulty = this.inputHandler.chooseLevel();

            if (levelDifficulty == GameLevels.EXIT){
                GamePrinter.goodbye();
                this.gameState = GameState.EXITING;
                continue;
            }

            Level level = LevelFactory.createLevel(levelDifficulty);

            Board board = Objects.requireNonNull(level).generateBoard();
            List<Block> blocks = level.generateBlocks();

            this.runGame(board, blocks);
        }

    }

    private void runGame(Board board, List<Block> blocks) {

        this.gameState = GameState.PLAYING;
        Move move = Move.NONE;

        while (!board.isFull() && move != Move.GIVEN_UP) {
            GamePrinter.printBoard(board);
            GamePrinter.printBlocks(blocks);

            move = this.inputHandler.chooseMove();

            if (move == Move.PLACE_BLOCK) {
                this.handlePlaceBlock(board, blocks);
            }
            else if (move == Move.REMOVE_BLOCK) {
                this.handleRemoveBlock(board, blocks);
            }
        }

        if (board.isFull()) {
            GamePrinter.printBoard(board);
            GamePrinter.levelWin();
        }
        this.gameState = GameState.MENU;
    }

    private boolean isExit(){
        return this.gameState == GameState.EXITING;
    }

    private void handlePlaceBlock(Board board, List<Block> blocks){
        Block chosenBlock = this.inputHandler.chooseBlock(blocks);
        Position position = this.inputHandler.getBoardPosition(board);

        if (board.placeBlock(chosenBlock, position.getRow(), position.getCol())) {
            blocks.remove(chosenBlock);
            GamePrinter.successfullyPlaced();
        } else {
            GamePrinter.invalidPlacement();
        }
    }

    private void handleRemoveBlock(Board board, List<Block> blocks){
        Position position = this.inputHandler.getBoardPosition(board);
        Block removedBlock = board.removeBlock(position.getRow(), position.getCol());

        if (removedBlock != null) {
            blocks.add(removedBlock);
            GamePrinter.successfullyRemoved();
        } else {
            GamePrinter.noBlockFound();
        }
    }
}
