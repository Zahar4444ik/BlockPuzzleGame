package sk.tuke.gamestudio.game.BlockPuzzle.levels.randomLevel.randomGenerator.tetris;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.ConsoleColor;
import sk.tuke.gamestudio.game.BlockPuzzle.core.Position;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Block;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Cell;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.CellState;

import java.util.*;

@Data
public class Board {
    private final int rows;
    private final int cols;
    private sk.tuke.gamestudio.game.BlockPuzzle.core.board.Cell[][] grid;
    @JsonIgnore
    private Map<Position, sk.tuke.gamestudio.game.BlockPuzzle.core.board.Block> blockMap;
    private Map<Position, sk.tuke.gamestudio.game.BlockPuzzle.core.board.Block> blockPositions;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new sk.tuke.gamestudio.game.BlockPuzzle.core.board.Cell[rows][cols];
        this.blockMap = new HashMap<>();
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                this.grid[row][col] = sk.tuke.gamestudio.game.BlockPuzzle.core.board.Cell.createEmptyBoardCell();
            }
        }
    }

    public boolean canPlaceBlock(sk.tuke.gamestudio.game.BlockPuzzle.core.board.Block block, int row, int col) {
        sk.tuke.gamestudio.game.BlockPuzzle.core.board.Cell[][] shape = block.getShape();
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

    public boolean placeBlock(sk.tuke.gamestudio.game.BlockPuzzle.core.board.Block block, int row, int col) {
        if (!this.canPlaceBlock(block, row, col)) {
            return false;
        }
        this.blockPositions.put(new Position(row, col), block);

        sk.tuke.gamestudio.game.BlockPuzzle.core.board.Cell[][] shape = block.getShape();
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
        for (sk.tuke.gamestudio.game.BlockPuzzle.core.board.Cell[] row : this.grid) {
            for (Cell cell : row) {
                if (Objects.equals(cell.getState(), CellState.EMPTY_BOARD.toString())) {
                    return false;
                }
            }
        }
        return true;
    }

    public sk.tuke.gamestudio.game.BlockPuzzle.core.board.Block removeBlock(int row, int col) {
        Position pos = new Position(row, col);

        if (!blockMap.containsKey(pos)) {
            return null; // No block at this position
        }

        sk.tuke.gamestudio.game.BlockPuzzle.core.board.Block block = blockMap.get(pos);

        for (int rowIdx = 0; rowIdx < this.rows; rowIdx++) {
            for (int colIdx = 0; colIdx < this.cols; colIdx++) {
                if (Objects.equals(grid[rowIdx][colIdx].getState(), CellState.FILLED.toString()) && block.equals(blockMap.get(new Position(rowIdx, colIdx)))) {
                    grid[rowIdx][colIdx].setState(CellState.EMPTY_BOARD);
                    grid[rowIdx][colIdx].setColor(ConsoleColor.RESET);
                }
            }
        }

        blockMap.entrySet().removeIf(entry -> entry.getValue() == block);
        blockPositions.entrySet().removeIf(entry -> entry.getValue() == block);

        return block;
    }

    public void moveBlocksDown() {

        List<Map.Entry<Position, sk.tuke.gamestudio.game.BlockPuzzle.core.board.Block>> sortedBlocks = new ArrayList<>(blockPositions.entrySet());
        sortedBlocks.sort((entry1, entry2) -> Integer.compare(entry2.getKey().getRow(), entry1.getKey().getRow())); // Sort by row in descending order

        for (Map.Entry<Position, sk.tuke.gamestudio.game.BlockPuzzle.core.board.Block> entry : sortedBlocks){
            Position pos = entry.getKey();
            Block block = entry.getValue();

            int removeRow = pos.getRow();
            while (removeBlock(removeRow, pos.getCol()) == null){
                removeRow++;
            }

            int row = pos.getRow();
            int col = pos.getCol();
            while (canPlaceBlock(block, row + 1, col)) {
                row++;
            }

            placeBlock(block, row, col);
        }
    }
}