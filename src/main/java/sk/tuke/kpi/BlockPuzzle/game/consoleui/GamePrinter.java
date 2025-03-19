package sk.tuke.kpi.BlockPuzzle.game.consoleui;

import sk.tuke.kpi.BlockPuzzle.game.core.board.Board;
import sk.tuke.kpi.BlockPuzzle.game.core.board.Cell;
import sk.tuke.kpi.BlockPuzzle.game.core.board.Block;
import sk.tuke.kpi.BlockPuzzle.game.levels.GameLevels;
import sk.tuke.kpi.BlockPuzzle.gamestudio.entity.Comment;
import sk.tuke.kpi.BlockPuzzle.gamestudio.entity.Score;

import java.util.List;

public class GamePrinter {

    public static void welcome(){
        String title = """
            
            ██████╗ ██╗      ██████╗  ██████╗██╗  ██╗    ██████╗ ██╗   ██╗███████╗███████╗██║     ███████╗
            ██╔══██╗██║     ██╔═══██╗██╔════╝██║ ██╔╝    ██╔══██╗██║   ██║     ██║     ██║██║     ██╔════╝
            ██████╔╝██║     ██║   ██║██║     █████║      ██████╔╝██║   ██║  ███╔═╝  ███╔═╝██║     █████╗
            ██╔══██╗██║     ██║   ██║██║     ██╔═██║     ██╔═══╝ ██║   ██║██╔══╝  ██╔══╝  ██║     ██╔══╝
            ██████╔╝███████╗╚██████╔╝╚██████╗██║  ██║    ██║     ╚██████╔╝███████╗███████╗███████╗███████╗
            ╚═════╝ ╚══════╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝    ╚═╝      ╚═════╝ ╚══════╝╚══════╝╚══════╝╚══════╝
            """;
        System.out.println(ConsoleColor.PURPLE + title + ConsoleColor.RESET);
        System.out.println(ConsoleColor.CYAN + "===============================" + ConsoleColor.RESET);
        System.out.println(ConsoleColor.BOLD + ConsoleColor.YELLOW.toString() + "     WELCOME TO BLOCK PUZZLE" + ConsoleColor.RESET);
        System.out.println(ConsoleColor.CYAN + "===============================" + ConsoleColor.RESET);

        System.out.println(ConsoleColor.CYAN +
                "\n🧩 Block Puzzle is a fun and challenging game where you must fill the board completely using different-shaped blocks." +
                "\n📌 You can place and remove blocks strategically to find the perfect fit." +
                "\n🎯 Your goal? Fill the entire board without leaving gaps!" +
                "\n\n🌟 Choose from 3 levels of difficulty: EASY, MEDIUM, and HARD. Feeling adventurous? Try a RANDOM puzzle!" +
                "\n💬 After playing, you can leave a COMMENT and RATE the game!" +
                "\n\n🎮 Get ready to play and test your puzzle-solving skills!" +
                "\n" + ConsoleColor.RESET);
    }

    public static void goodbye(){
        System.out.println("\nGoodbye:) Waiting for you again!");
    }

    public static void leaderboard(List<Score> topScores){
        System.out.println("\n🏆 Leaderboard:\n");
        for (Score s : topScores) {
            System.out.println("🔹 " + s.getPlayer() + ": " + s.getPoints());
        }
        System.out.println();
    }

    public static void askForPlayerNickname(){
        System.out.print(ConsoleColor.BOLD + "Enter your nickname:" + ConsoleColor.RESET + " ");
    }

    public static void displayGameLevels(){
        System.out.println();
//        String[] emoji = {ConsoleColor.GREEN + "\uD83D\uDFE2" + ConsoleColor.RESET, "\uD83D\uDD35", "\uD83D\uDD34","\uD83C\uDFB2", "❌",};
        String[] emoji = {ConsoleColor.GREEN + "\uD83D\uDFE2" + ConsoleColor.RESET, "\uD83D\uDD35", "\uD83D\uDD34", "❌",};
        int emojiIndex = 0;

        for (GameLevels level: GameLevels.values()){
//            String size =(level != GameLevels.EXIT && level != GameLevels.RANDOM) ? " (" + level.getRows() + "x" + level.getCols() + ")" : "";
            String size =(level != GameLevels.EXIT) ? " (" + level.getRows() + "x" + level.getCols() + ")" : "";
            System.out.println(emoji[emojiIndex++] + level.name() + " - " + level.getDescription() + size);
        }
        System.out.println();
    }

    public static void askForLevel(){
        System.out.print(ConsoleColor.BOLD + ConsoleColor.UNDERLINE.toString() + "Enter level (EASY, MEDIUM, HARD):" + ConsoleColor.RESET + " ");
    }

    public static void invalidInput(){
        System.out.println("❌ Invalid input!");
    }

    public static void invalidPlacement(){
        System.out.println("❌ Invalid placement! Try again.");
    }

    public static void levelWin(){
        System.out.println("\n🎉 Game Over! You filled the board! 🎉");
    }

    public static void printBoard(Board board) {
        int rows = board.getRows();
        int cols = board.getCols();
        Cell[][] grid = board.getGrid();

        System.out.println(ConsoleColor.BLUE + "\n     BLOCK PUZZLE BOARD\n" + ConsoleColor.RESET);

        // Print column numbers
        System.out.print("    ");
        for (int col = 0; col < cols; col++) {
            System.out.print(ConsoleColor.YELLOW.toString() + col + ConsoleColor.RESET + " ");
        }
        System.out.println();

        // Print top border
        System.out.print("   ┌");
        for (int col = 0; col < cols * 2 - 1; col++) {
            System.out.print("─");
        }
        System.out.println("┐");

        // Print board rows with row numbers
        for (int row = 0; row < rows; row++) {
            System.out.print(ConsoleColor.YELLOW.toString() + row + ConsoleColor.RESET + " │ ");
            for (int col = 0; col < cols; col++) {
                grid[row][col].printCell();
            }
            System.out.println("│"); // Right border
        }

        System.out.print("   └");
        for (int col = 0; col < cols * 2 - 1; col++) {
            System.out.print("─");
        }
        System.out.println("┘\n");
    }

    public static void printBlocksInRows(List<Block> blocks, int blocksPerRow) {
        System.out.println("\n     AVAILABLE BLOCKS\n");

        int totalBlocks = blocks.size();

        for (int blockIdx = 0; blockIdx < totalBlocks; blockIdx += blocksPerRow) {
            int end = Math.min(blockIdx + blocksPerRow, totalBlocks); // end of row
            List<Block> rowBlocks = blocks.subList(blockIdx, end);

            for (int blockNumber = blockIdx; blockNumber < end; blockNumber++) { // Print block numbers
                Block block = blocks.get(blockNumber);
                int blockWidth = block.getWidth();
                int padding = Math.max((blockWidth * 2 - 3) / 2, 0); // Adjust padding for centering

                System.out.print(" ".repeat(padding) + "[" + (blockNumber + 1) + "]  " + " ".repeat(padding) + "  ");
            }
            System.out.println();

            int maxHeight = rowBlocks.stream()
                    .mapToInt(Block::getHeight)
                    .max()
                    .orElse(0);

            for (int row = 0; row < maxHeight; row++) {
                for (Block block : rowBlocks) {
                    Cell[][] shape = block.getShape();
                    int width = block.getWidth();

                    for (int col = 0; col < width; col++) {
                        if (row < shape.length && col < shape[row].length) {
                            System.out.print(shape[row][col].getColor().toString() + shape[row][col].getSymbol() + " " + ConsoleColor.RESET);
                        } else {
                            System.out.print("  ");
                        }
                    }
                    System.out.print("   ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static void askForBlock(List<Block> blocks){
        System.out.println(ConsoleColor.BOLD + "Select a block (1-" + blocks.size() + "):" + ConsoleColor.RESET + " ");
    }

    public static void askForPosition(){
        System.out.println(ConsoleColor.BOLD + "Enter position (row col):" + ConsoleColor.RESET + " ");
    }

    public static void successfullyPlaced(){
        System.out.println("\n✅ Block placed successfully!");
    }

    public static void successfullyRemoved(){
        System.out.println("\n✅ Block removed successfully!");
    }

    public static void noBlockFound(){
        System.out.println("\n❌ No block found at that location!");
    }

    public static void askForNextMove(){
        System.out.println(ConsoleColor.BOLD + "What is your next move?" + ConsoleColor.RESET);
        System.out.println("1 - Place block");
        System.out.println("2 - Remove block");
        System.out.println("3 - Give up");
        System.out.println(ConsoleColor.BOLD + "Enter your next move (1-3):" + ConsoleColor.RESET + " ");
    }

    public static void printScore(int score){
        System.out.println("Your score: " + score);
    }

    public static void askForComment(){
        System.out.println(ConsoleColor.BOLD + "Would you like to leave a comment? ("+ ConsoleColor.GREEN +"y" +
                ConsoleColor.RESET + "/" + ConsoleColor.RED + "n"+ ConsoleColor.RESET +"):" + ConsoleColor.RESET + " ");
    }

    public static void enterComment(){
        System.out.println(ConsoleColor.BOLD + "Enter your comment:" + ConsoleColor.RESET + " ");
    }

    public static void askForRating(){
        System.out.println(ConsoleColor.BOLD + "Would you like to rate this game? ("+ ConsoleColor.GREEN +"y" +
                ConsoleColor.RESET + "/" + ConsoleColor.RED + "n"+ ConsoleColor.RESET +"):" + ConsoleColor.RESET + " ");
    }

    public static void enterRating(){
        System.out.println(ConsoleColor.BOLD + "Enter your rate (1-5): " + ConsoleColor.RESET);
    }

    public static void printAverageRating(int averageRating) {
        System.out.println(ConsoleColor.PURPLE + "Average rating: " + averageRating + "★" + ConsoleColor.RESET);
    }

    public static void printComments(List<Comment> comments) {
        System.out.println(ConsoleColor.PURPLE + "Comments:" + ConsoleColor.RESET);
        for (int i = 0; i < comments.size(); i++) {
            if (i == 5) {
                break;
            }
            var comment = comments.get(i);
            System.out.println(ConsoleColor.BLUE + comment.getPlayer() + ": " + ConsoleColor.RESET + comment.getComment());
        }
        System.out.println();
    }

    public static void thankYouForFeedback(){
        System.out.println("Thank you for your feedback!");
    }

    public static void greetNewPlayer(String nickname){
        System.out.println("Welcome, " + nickname + "!");
    }

    public static void playerAlreadyExists(String nickname, int score){
        System.out.println("Welcome back, " + nickname + "!" + ConsoleColor.PURPLE + " Your previous score: " + score + ConsoleColor.RESET);
    }
}
