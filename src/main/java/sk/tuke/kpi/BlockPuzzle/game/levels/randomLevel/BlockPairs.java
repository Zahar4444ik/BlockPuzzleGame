package sk.tuke.kpi.BlockPuzzle.game.levels.randomLevel;

import sk.tuke.kpi.BlockPuzzle.game.consoleui.ConsoleColor;
import sk.tuke.kpi.BlockPuzzle.game.core.board.Block;
import sk.tuke.kpi.BlockPuzzle.game.core.board.Cell;

import java.util.ArrayList;
import java.util.List;

public class BlockPairs {

    public static List<Block> create2x2BlockPair() {
        List<Block> blocks = new ArrayList<>();

        Block block1 = new Block(new Cell[][]{
                {new Cell(ConsoleColor.BLUE), new Cell(ConsoleColor.BLUE)}, // ##
        }, ConsoleColor.BLUE);

        Block block2 = new Block(new Cell[][]{
                {new Cell(ConsoleColor.RED), new Cell(ConsoleColor.RED)},  // ##
        }, ConsoleColor.RED);

        Block block3 = new Block(new Cell[][]{
                {new Cell(ConsoleColor.GREEN)},// #
                {new Cell(ConsoleColor.GREEN)} // #
        }, ConsoleColor.GREEN);

        Block block4 = new Block(new Cell[][]{
                {new Cell(ConsoleColor.CYAN)},// #
                {new Cell(ConsoleColor.CYAN)} // #
        }, ConsoleColor.CYAN);


        // Return both blocks as a pair (you can add more pairs if needed)
        return List.of(block1, block2);
    }


}
