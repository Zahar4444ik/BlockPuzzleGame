package sk.tuke.gamestudio.game.BlockPuzzle.core.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.ConsoleColor;
import sk.tuke.gamestudio.game.BlockPuzzle.core.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class Board {
    private final int rows;
    private final int cols;
    private Cell[][] grid;
    @JsonIgnore
    private Map<Position, Block> blockMap;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
        this.blockMap = new HashMap<>();
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                this.grid[row][col] = Cell.createEmptyBoardCell();
            }
        }
    }

    public boolean canPlaceBlock(Block block, int row, int col) {
        Cell[][] shape = block.getShape();
        int width = block.getWidth();
        int height = block.getHeight();

        if (col + width > this.cols || row + height > this.rows) {
            return false; // Block is out of board bounds
        }

        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            for (int colIdx = 0; colIdx < width; colIdx++) {
                if (Objects.equals(shape[rowIdx][colIdx].getState(), CellState.FILLED.toString()) &&
                        !Objects.equals(this.grid[row + rowIdx][col + colIdx].getState(), CellState.EMPTY_BOARD.toString())) {
                    return false; // Block is colliding with another block
                }
            }
        }

        return true;
    }

    public boolean placeBlock(Block block, int row, int col) {
        if (!this.canPlaceBlock(block, row, col)) {
            return false;
        }

        Cell[][] shape = block.getShape();
        ConsoleColor color = block.getColor();
        int width = block.getWidth();
        int height = block.getHeight();

        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            for (int colIdx = 0; colIdx < width; colIdx++) {
                if (shape[rowIdx][colIdx].getSymbol() == CellState.FILLED.getSymbol()) {
                    this.grid[row + rowIdx][col + colIdx].setState(CellState.FILLED);
                    this.grid[row + rowIdx][col + colIdx].setColor(color);
                    blockMap.put(new Position(row + rowIdx, col + colIdx), block);
                }
            }
        }
        return true;
    }

    public boolean isFull() {
        for (Cell[] row : this.grid) {
            for (Cell cell : row) {
                if (Objects.equals(cell.getState(), CellState.EMPTY_BOARD.toString())) {
                    return false;
                }
            }
        }
        return true;
    }

    public Block removeBlock(int row, int col) {
        Position pos = new Position(row, col);

        if (!blockMap.containsKey(pos)) {
            return null; // No block at this position
        }

        Block block = blockMap.get(pos);

        for (int rowIdx = 0; rowIdx < this.rows; rowIdx++) {
            for (int colIdx = 0; colIdx < this.cols; colIdx++) {
                if (Objects.equals(grid[rowIdx][colIdx].getState(), CellState.FILLED.toString()) && block.equals(blockMap.get(new Position(rowIdx, colIdx)))) {
                    grid[rowIdx][colIdx].setState(CellState.EMPTY_BOARD);
                    grid[rowIdx][colIdx].setColor(ConsoleColor.RESET);
                }
            }
        }

        blockMap.entrySet().removeIf(entry -> entry.getValue() == block);

        return block;
    }
}