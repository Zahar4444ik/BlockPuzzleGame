package sk.tuke.kpi.BlockPuzzle.consoleui;

import sk.tuke.kpi.BlockPuzzle.core.Board;
import sk.tuke.kpi.BlockPuzzle.core.Cell;
import sk.tuke.kpi.BlockPuzzle.core.blocks.Block;
import sk.tuke.kpi.BlockPuzzle.game.levels.GameLevels;

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
    }

    public static void goodbye(){
        System.out.println("\nGoodbye:) Waiting for you again!");
    }

    public static void displayGameLevels(){
        System.out.println("\nTake a look at available levels we have:\n");
        for (GameLevels level: GameLevels.values()){
            String size =(level != GameLevels.EXIT && level != GameLevels.RANDOM) ? " (" + level.getRows() + "x" + level.getCols() + ")" : "";
            System.out.println(level.name() + " - " + level.getDescription() + size);
        }
        System.out.println();
    }

    public static void askForLevel(){
        System.out.print("Enter level (EASY, MEDIUM, HARD, EXPERT): ");
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

    public static void printBlocks(List<Block> blocks) {
        System.out.println("\n     AVAILABLE BLOCKS\n");

        int index = 1;
        for (Block block : blocks) {
            System.out.println("Block " + index + ":");
            printBlockShape(block);
            System.out.println();
            index++;
        }
    }

    private static void printBlockShape(Block block) {
        for (Cell[] row : block.getShape()) {
            System.out.print("   "); // Indentation for alignment
            for (Cell cell : row) {
                cell.printCell();
            }
            System.out.println();
        }
    }

    public static void askForBlock(List<Block> blocks){
        System.out.println("Select a block (1-" + blocks.size() + "): ");
    }

    public static void askForPosition(){
        System.out.println("Enter position (row col): ");
    }

    public static void successfullyPlaced(){
        System.out.println("✅ Block placed successfully!");
    }

    public static void successfullyRemoved(){
        System.out.println("✅ Block removed successfully!");
    }

    public static void noBlockFound(){
        System.out.println("❌ No block found at that location!");
    }

    public static void askForNextMove(){
        System.out.println("1 - Place block");
        System.out.println("2 - Remove block");
        System.out.println("3 - Give up");
        System.out.println("Enter your next move (1-3): ");
    }
}
