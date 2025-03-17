package sk.tuke.kpi.BlockPuzzle.game.consoleui;

import sk.tuke.kpi.BlockPuzzle.game.core.Board;
import sk.tuke.kpi.BlockPuzzle.game.core.Player;
import sk.tuke.kpi.BlockPuzzle.game.core.Position;
import sk.tuke.kpi.BlockPuzzle.game.core.blocks.Block;
import sk.tuke.kpi.BlockPuzzle.game.GameState;
import sk.tuke.kpi.BlockPuzzle.game.Move;
import sk.tuke.kpi.BlockPuzzle.game.levels.GameLevels;
import sk.tuke.kpi.BlockPuzzle.game.levels.Level;
import sk.tuke.kpi.BlockPuzzle.game.levels.LevelFactory;
import sk.tuke.kpi.BlockPuzzle.gamestudio.entity.Score;
import sk.tuke.kpi.BlockPuzzle.gamestudio.service.CommentServiceJDBC;
import sk.tuke.kpi.BlockPuzzle.gamestudio.service.RatingServiceJDBC;
import sk.tuke.kpi.BlockPuzzle.gamestudio.service.ScoreServiceJDBC;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class BlockPuzzleConsole {
    private final static String GAME_NAME = "Block Puzzle";
    private GameState gameState;
    private final InputHandler inputHandler;
    private Player player;

    public BlockPuzzleConsole() {
        this.inputHandler = new InputHandler();
        this.gameState = GameState.MENU;
        this.player = null;
    }

    public void start() {
        GamePrinter.welcome();
        GamePrinter.leaderboard(new ScoreServiceJDBC().getTopScores(GAME_NAME));
        GamePrinter.printAverageRating(new RatingServiceJDBC().getAverageRating(GAME_NAME));
        GamePrinter.printComments(new CommentServiceJDBC().getComments(GAME_NAME));

        this.player = createPlayer();

        while(!this.isExit()){

            GameLevels levelDifficulty = this.inputHandler.chooseLevel();

            if (levelDifficulty == GameLevels.EXIT){
                this.gameState = GameState.EXITING;
                continue;
            }

            Level level = LevelFactory.createLevel(levelDifficulty);

            Board board = Objects.requireNonNull(level).generateBoard();
            List<Block> blocks = level.generateBlocks();

            this.runGame(board, blocks);
        }

        this.getFeedback();
        GamePrinter.goodbye();

    }

    private void runGame(Board board, List<Block> blocks) {

        this.gameState = GameState.PLAYING;
        Move move = Move.NONE;

        while (!board.isFull() && move != Move.GIVEN_UP) {

            GamePrinter.printScore(this.player.getScore());

            GamePrinter.printBoard(board);
            GamePrinter.printBlocksInRows(blocks, 3);

            move = this.inputHandler.chooseMove();

            if (move == Move.PLACE_BLOCK) {
                this.handlePlaceBlock(board, blocks);
            }
            else if (move == Move.REMOVE_BLOCK) {
                this.handleRemoveBlock(board, blocks);
            }
        }

        if (board.isFull()) {
            this.player.increaseScore(100);
            GamePrinter.printBoard(board);
            GamePrinter.levelWin();
        }
        this.player.saveScore(GAME_NAME);
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
            this.player.increaseScore(10);
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
            this.player.decreaseScore(15);
        } else {
            GamePrinter.noBlockFound();
        }
    }

    private Player createPlayer() {
        String nickname = this.inputHandler.enterPlayerNickname();
        return new Player(nickname);
    }

    private void getFeedback() {
        GamePrinter.askForRating();
        if (this.inputHandler.getYesNoAnswer() == 'y') {
            String rating = this.inputHandler.getRating();
            this.player.setRating(GAME_NAME, rating);
        }

        GamePrinter.askForComment();
        if (this.inputHandler.getYesNoAnswer() == 'y') {

            String comment = this.inputHandler.getComment();
            this.player.addComment(GAME_NAME, comment);
        }

        GamePrinter.thankYouForFeedback();
    }

}
