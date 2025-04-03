package com.example.BlockPuzzle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.ConsoleColor;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Board;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Cell;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.CellState;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Block;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    private Board board;
    private Block testBlock;

    @BeforeEach
    void setUp() {
        board = new Board(5, 5);
        testBlock = new Block(new Cell[][]{
            {new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN)},  // 2x2 block
            {new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN)},
        }, ConsoleColor.CYAN);
    }

    @Test
    void testBoardInitialization() {
        assertEquals(5, board.getRows());
        assertEquals(5, board.getCols());

        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                assertEquals(CellState.EMPTY_BOARD.getSymbol(), board.getGrid()[row][col].getSymbol());
            }
        }
    }

    @Test
    void testCanPlaceBlock_ValidPosition(){
        assertTrue(board.canPlaceBlock(testBlock, 1 , 1));
    }

    @Test
    void testCanPlaceBlock_InvalidPosition(){
        assertFalse(board.canPlaceBlock(testBlock, 4 , 4));
    }

    @Test
    void testCanPlaceBlock_Collision(){
        board.placeBlock(testBlock, 1, 1);
        assertFalse(board.canPlaceBlock(testBlock, 1, 1));
    }

    @Test
    void testPlaceBlock_Success(){
        assertTrue(board.placeBlock(testBlock, 2, 2));

        for (int row = 2; row < 4; row++) {
            for (int col = 2; col < 4; col++) {
                assertEquals(CellState.FILLED.getSymbol(), board.getGrid()[row][col].getSymbol());
            }
        }
    }

    @Test
    void testPlaceBlock_Fail(){
        board.placeBlock(testBlock, 2, 2);
        assertFalse(board.placeBlock(testBlock, 2, 2));
    }

    @Test
    void isFull_EmptyBoard(){
        assertFalse(board.isFull());
    }

    @Test
    void isFull_FullBoard(){
        Block fullBlock = new Block(new Cell[][]{
                {new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN)},
                {new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN)},
                {new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN)},
                {new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN)},
                {new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN), new Cell(ConsoleColor.CYAN)},
        }, ConsoleColor.CYAN);

        board.placeBlock(fullBlock, 0, 0);

        assertTrue(board.isFull());
    }

    @Test
    void testRemoveBlock_Success(){
        board.placeBlock(testBlock, 2, 2);
        assertNotNull(board.removeBlock(2, 2));

        for (int row = 2; row < 4; row++) {
            for (int col = 2; col < 4; col++) {
                assertEquals(CellState.EMPTY_BOARD.getSymbol(), board.getGrid()[row][col].getSymbol());
            }
        }
    }

    @Test
    void testRemoveBlock_Fail(){
        assertNull(board.removeBlock(2, 2));
    }
}
