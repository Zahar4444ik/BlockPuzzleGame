package com.example.BlockPuzzle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.ConsoleColor;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Block;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Cell;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.CellState;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BlockTest {
    private Block block;
    private Cell[][] shape;

    @BeforeEach
    void setUp() {
        shape = new Cell[][]{
                {new Cell(ConsoleColor.RED), new Cell(ConsoleColor.RED)},
                {new Cell(ConsoleColor.RED), new Cell(ConsoleColor.RED)}
        };
        block = new Block(shape, ConsoleColor.RED);
    }

    @Test
    void testBlockInitialization() {
        assertNotNull(block.getShape());
        assertEquals(ConsoleColor.RED, block.getColor());
    }

    @Test
    void testBlockDimensions() {
        assertEquals(2, block.getWidth());
        assertEquals(2, block.getHeight());
    }

    @Test
    void testBlockShapeIntegrity() {
        assertEquals(CellState.FILLED.getSymbol(), block.getShape()[0][0].getSymbol());
        assertEquals(ConsoleColor.RED, block.getShape()[0][0].getColor());
    }
}
