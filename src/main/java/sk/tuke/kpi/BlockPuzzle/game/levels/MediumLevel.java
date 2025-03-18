package sk.tuke.kpi.BlockPuzzle.game.levels;

import sk.tuke.kpi.BlockPuzzle.game.consoleui.ConsoleColor;
import sk.tuke.kpi.BlockPuzzle.game.core.board.Board;
import sk.tuke.kpi.BlockPuzzle.game.core.board.Cell;
import sk.tuke.kpi.BlockPuzzle.game.core.board.Block;

import java.util.ArrayList;
import java.util.List;

public class MediumLevel implements Level{
    @Override
    public Board generateBoard() {
        return new Board(GameLevels.MEDIUM.getRows(), GameLevels.MEDIUM.getCols());
    }

    @Override
    public List<Block> generateBlocks() {
        List<Block> blocks = new ArrayList<>();

        blocks.add(new Block( new Cell[][]{
                {new Cell(ConsoleColor.RED), new Cell(ConsoleColor.RED), Cell.createEmptyBlockCell()},     // ##
                {new Cell(ConsoleColor.RED), new Cell(ConsoleColor.RED), new Cell(ConsoleColor.RED)}, // ###
                {Cell.createEmptyBlockCell(), new Cell(ConsoleColor.RED), Cell.createEmptyBlockCell()}          //  #
        }, ConsoleColor.RED));

        blocks.add(new Block( new Cell[][]{
                {new Cell(ConsoleColor.GREEN), new Cell(ConsoleColor.GREEN), new Cell(ConsoleColor.GREEN)}, // ###
                {Cell.createEmptyBlockCell(), new Cell(ConsoleColor.GREEN), Cell.createEmptyBlockCell()},             //  #
                {Cell.createEmptyBlockCell(), new Cell(ConsoleColor.GREEN), Cell.createEmptyBlockCell()}              //  #
        }, ConsoleColor.GREEN));

        blocks.add(new Block( new Cell[][]{
                {new Cell(ConsoleColor.BLUE), new Cell(ConsoleColor.BLUE), Cell.createEmptyBlockCell()}, // ##
                {Cell.createEmptyBlockCell(), new Cell(ConsoleColor.BLUE), new Cell(ConsoleColor.BLUE)} //  ##
        }, ConsoleColor.BLUE));

        blocks.add(new Block( new Cell[][]{
                {new Cell(ConsoleColor.CYAN), Cell.createEmptyBlockCell(), new Cell(ConsoleColor.CYAN)},      // # #
                {new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN)} // ###
        }, ConsoleColor.CYAN));

        blocks.add(new Block( new Cell[][]{
                {new Cell(ConsoleColor.YELLOW), new Cell(ConsoleColor.YELLOW)}, // ##
                {new Cell(ConsoleColor.YELLOW), new Cell(ConsoleColor.YELLOW)}, // ##
                {new Cell(ConsoleColor.YELLOW), new Cell(ConsoleColor.YELLOW)}  // ##
        }, ConsoleColor.YELLOW));

        blocks.add(new Block( new Cell[][]{
                {new Cell(ConsoleColor.PURPLE), new Cell(ConsoleColor.PURPLE), Cell.createEmptyBlockCell()},        // ##
                {new Cell(ConsoleColor.PURPLE), new Cell(ConsoleColor.PURPLE), new Cell(ConsoleColor.PURPLE)}, // ###
        }, ConsoleColor.PURPLE));

        blocks.add(new Block( new Cell[][]{
                {new Cell(ConsoleColor.RED)}, // #
                {new Cell(ConsoleColor.RED)}, // #
                {new Cell(ConsoleColor.RED)}, // #
        }, ConsoleColor.RED));

        blocks.add(new Block( new Cell[][]{
                {new Cell(ConsoleColor.GREEN), new Cell(ConsoleColor.GREEN), new Cell(ConsoleColor.GREEN)}, // ###
                {new Cell(ConsoleColor.GREEN), new Cell(ConsoleColor.GREEN), new Cell(ConsoleColor.GREEN)}, // ###
        }, ConsoleColor.GREEN));

        blocks.add(new Block( new Cell[][]{
                {new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN)}, // ##
                {new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN)} // ##
        }, ConsoleColor.CYAN));

        blocks.add(new Block( new Cell[][]{
                {new Cell(ConsoleColor.YELLOW), new Cell(ConsoleColor.YELLOW)}, // ##
        }, ConsoleColor.YELLOW));

        blocks.add(new Block( new Cell[][]{
                {new Cell(ConsoleColor.PURPLE), new Cell(ConsoleColor.PURPLE), new Cell(ConsoleColor.PURPLE)}, // ###
        }, ConsoleColor.PURPLE));

        return blocks;
    }
}
