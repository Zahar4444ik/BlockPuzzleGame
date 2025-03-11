package sk.tuke.kpi.BlockPuzzle.core;

import lombok.Getter;
import sk.tuke.kpi.BlockPuzzle.consoleui.ConsoleColor;
import sk.tuke.kpi.BlockPuzzle.core.blocks.Block;

import java.util.HashMap;
import java.util.Map;

public class Board {
    @Getter
    private final int rows;
    @Getter
    private final int cols;
    @Getter
    private final Cell[][] grid;
    private final Map<Position, Block> blockMap;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
        this.blockMap = new HashMap<>();
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.grid[i][j] = Cell.createEmptyBoardCell();
            }
        }
    }

    public boolean canPlaceBlock(Block block, int row, int col){
        Cell[][] shape = block.getShape();
        int width = block.getWidth();
        int height = block.getHeight();

        if (col+width > this.cols || row+height > this.rows){
            return false; // Block placing goes out of the board
        }

        for (int i = 0; i < height; i++){ // Check if the block do not interact with another one
            for (int j = 0; j < width; j++){
                if (shape[i][j].getSymbol() == CellState.FILLED.getSymbol() && this.grid[row + i][col + j].getSymbol() != CellState.EMPTY_BOARD.getSymbol()){
                    return false;
                }
            }
        }

        return true;
    }

    public boolean placeBlock(Block block, int row, int col) {
        if (!this.canPlaceBlock(block, row, col)){
            return false;
        }

        Cell[][] shape = block.getShape();
        ConsoleColor color = block.getColor();
        int width = block.getWidth();
        int height = block.getHeight();

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                if (shape[i][j].getSymbol() == CellState.FILLED.getSymbol()){
                    this.grid[row+i][col+j].setSymbol(CellState.FILLED.getSymbol());
                    this.grid[row+i][col+j].setColor(color);
                    blockMap.put(new Position(row + i, col + j), block);
                }
            }
        }
        return true;
    }

    public boolean isFull() {
        for (Cell[] row : this.grid) {
            for (Cell cell : row) {
                if (cell.getSymbol() == CellState.EMPTY_BOARD.getSymbol()) {
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

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (grid[i][j].getSymbol() == CellState.FILLED.getSymbol() && blockMap.get(new Position(i, j)) == block) {
                    grid[i][j].setSymbol(CellState.EMPTY_BOARD.getSymbol());
                    grid[i][j].setColor(ConsoleColor.RESET);
                }
            }
        }

        // Remove all instances of this block from the board
        blockMap.entrySet().removeIf(entry -> entry.getValue() == block);

        return block;
    }
}