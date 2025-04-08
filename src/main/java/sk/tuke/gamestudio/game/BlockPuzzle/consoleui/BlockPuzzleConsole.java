package sk.tuke.gamestudio.game.BlockPuzzle.consoleui;

import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.inputHandler.AuthAction;
import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.inputHandler.InputHandler;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Board;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.game.BlockPuzzle.core.Position;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Block;
import sk.tuke.gamestudio.game.BlockPuzzle.core.GameState;
import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.inputHandler.Move;
import sk.tuke.gamestudio.game.BlockPuzzle.levels.GameLevels;
import sk.tuke.gamestudio.game.BlockPuzzle.levels.Level;
import sk.tuke.gamestudio.game.BlockPuzzle.levels.LevelFactory;
import sk.tuke.gamestudio.service.jdbc.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class BlockPuzzleConsole {
    public final static String GAME_NAME = "Block Puzzle";
    private GameState gameState;
    private final InputHandler inputHandler;
    private Player player;
    private final PlayerService playerService = new PlayerServiceJDBC();

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

        handleAuth();
        playerService.updatePlayer(player);

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

        this.playerService.updatePlayer(this.player);
    }

    private void runGame(Board board, List<Block> blocks) {

        this.gameState = GameState.PLAYING;
        this.player.increaseGamesPlayed();
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
            GamePrinter.printScore(this.player.getScore());
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

    private void handleAuth() {
        boolean successAuth = false;

        while (!successAuth) {
            AuthAction authAction = this.inputHandler.getAuthAction();

            if (authAction == AuthAction.EXIT) {
                this.gameState = GameState.EXITING;
                return;
            }

            String nickname = this.inputHandler.enterPlayerNickname();
            String password = this.inputHandler.enterPlayerPassword();

            if (authAction == AuthAction.LOGIN) {
                if (this.playerService.login(nickname, password)){
                    this.player = playerService.getPlayer(nickname);
                    GamePrinter.playerAlreadyExists(player.getNickname(), player.getScore());
                    successAuth = true;
                    player.setLastPlayed(new Date());
                }
            }
            else if(authAction == AuthAction.REGISTER) {
                if (this.playerService.register(nickname, password)){
                    this.player = playerService.getPlayer(nickname);
                    GamePrinter.greetNewPlayer(player.getNickname());
                    successAuth = true;
                    player.setLastPlayed(new Date());
                }
            }
        }
    }

    private void getFeedback() {
        GamePrinter.askForRating();
        if (this.inputHandler.getYesNoAnswer()) {
            String rating = this.inputHandler.getRating();
            this.player.setRating(GAME_NAME, rating);
        }

        GamePrinter.askForComment();
        if (this.inputHandler.getYesNoAnswer()) {

            String comment = this.inputHandler.getComment();
            this.player.addComment(GAME_NAME, comment);
        }

        GamePrinter.thankYouForFeedback();
    }

}
