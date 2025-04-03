package com.example.BlockPuzzle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.ConsoleColor;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Cell;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.CellState;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CellTest {

    private Cell cell;

    @BeforeEach
    void setUp() {
        cell = new Cell(CellState.FILLED.getSymbol(), ConsoleColor.BLUE);
    }

    @Test
    void testCellInitialization() {
        assertEquals(CellState.FILLED.getSymbol(), cell.getSymbol());
        assertEquals(ConsoleColor.BLUE, cell.getColor());
    }

    @Test
    void testCreateEmptyBlockCell() {
        Cell emptyBlock = Cell.createEmptyBlockCell();
        assertEquals(CellState.EMPTY_BLOCK.getSymbol(), emptyBlock.getSymbol());
        assertEquals(ConsoleColor.RESET, emptyBlock.getColor());
    }

    @Test
    void testCreateEmptyBoardCell() {
        Cell emptyBoard = Cell.createEmptyBoardCell();
        assertEquals(CellState.EMPTY_BOARD.getSymbol(), emptyBoard.getSymbol());
        assertEquals(ConsoleColor.RESET, emptyBoard.getColor());
    }

    @Test
    void testSetSymbol() {
        cell.setSymbol('.');
        assertEquals('.', cell.getSymbol());
    }

    @Test
    void testSetColor() {
        cell.setColor(ConsoleColor.RED);
        assertEquals(ConsoleColor.RED, cell.getColor());
    }

    @Test
    void testPrintCell() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        cell.printCell();

        String expectedOutput = ConsoleColor.BLUE.toString() + cell.getSymbol() + ConsoleColor.RESET + " ";
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }
}
