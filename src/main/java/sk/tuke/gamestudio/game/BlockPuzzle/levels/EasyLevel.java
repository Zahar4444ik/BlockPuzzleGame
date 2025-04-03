package sk.tuke.gamestudio.game.BlockPuzzle.levels;

import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.ConsoleColor;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Cell;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Block;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Board;

import java.util.ArrayList;
import java.util.List;

public class EasyLevel implements Level{
    @Override
    public Board generateBoard() {
        return new Board(GameLevels.EASY.getRows(), GameLevels.EASY.getCols());
    }

    @Override
    public List<Block> generateBlocks() {
        List<Block> blocks = new ArrayList<>();

        blocks.add(new Block( new Cell[][]{
                {new Cell(ConsoleColor.RED), new Cell(ConsoleColor.RED), new Cell(ConsoleColor.RED), new Cell(ConsoleColor.RED)},   // ####
                {new Cell(ConsoleColor.RED), Cell.createEmptyBlockCell(), Cell.createEmptyBlockCell(), Cell.createEmptyBlockCell()} // #
        }, ConsoleColor.RED));
        blocks.add(new Block( new Cell[][]{
                {Cell.createEmptyBlockCell(), new Cell(ConsoleColor.BLUE), new Cell(ConsoleColor.BLUE)}, //  ##
                {new Cell(ConsoleColor.BLUE), new Cell(ConsoleColor.BLUE), Cell.createEmptyBlockCell()}  // ##
        }, ConsoleColor.BLUE));
        blocks.add(new Block( new Cell[][]{
                {new Cell(ConsoleColor.GREEN), new Cell(ConsoleColor.GREEN)}, // ##
                {new Cell(ConsoleColor.GREEN), new Cell(ConsoleColor.GREEN)}  // ##
        }, ConsoleColor.GREEN));
        blocks.add(new Block( new Cell[][]{
                {Cell.createEmptyBlockCell(), new Cell(ConsoleColor.CYAN)},      //  #
                {new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN)},      // ##
                {new Cell(ConsoleColor.CYAN), Cell.createEmptyBlockCell()}       // #
        }, ConsoleColor.CYAN));
        blocks.add(new Block( new Cell[][]{
                {new Cell(ConsoleColor.YELLOW), Cell.createEmptyBlockCell()},   // #
                {new Cell(ConsoleColor.YELLOW), new Cell(ConsoleColor.YELLOW)}, // ##
                {new Cell(ConsoleColor.YELLOW), new Cell(ConsoleColor.YELLOW)}  // ##
        }, ConsoleColor.YELLOW));
        blocks.add(new Block( new Cell[][]{
                {new Cell(ConsoleColor.PURPLE)}, // #
                {new Cell(ConsoleColor.PURPLE)}, // #
                {new Cell(ConsoleColor.PURPLE)}  // #
        }, ConsoleColor.PURPLE));

        return blocks;
    }
}
