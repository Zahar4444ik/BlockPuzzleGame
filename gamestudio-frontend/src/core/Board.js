import CellState from "./cell/CellState";
import Cell from "./cell/Cell";
import Position from "./Position";

class Board {
    constructor(rows, cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = Array(rows).fill().map(() => Array(cols).fill(new Cell(CellState.EMPTY, [0, 0, 0])));
        this.blockMap = new Map(); // Map<Position, Block> using Position with equals
    }

    getRows() {
        return this.rows;
    }

    getCols() {
        return this.cols;
    }

    getGrid() {
        return this.grid;
    }

    canPlaceBlock(block, row, col, offsetRow, offsetCol) {
        const shape = block.getShape();
        for (let i = 0; i < shape.length; i++) {
            for (let j = 0; j < shape[0].length; j++) {
                if (shape[i][j].getState() === CellState.FILLED) {
                    const newRow = row + i - offsetRow;
                    const newCol = col + j - offsetCol;
                    if (newRow < 0 || newRow >= this.rows || newCol < 0 || newCol >= this.cols) {
                        return false;
                    }
                    if (this.grid[newRow][newCol].getState() === CellState.FILLED) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    placeBlock(block, row, col, offsetRow, offsetCol) {
        const shape = block.getShape();
        for (let i = 0; i < shape.length; i++) {
            for (let j = 0; j < shape[0].length; j++) {
                if (shape[i][j].getState() === CellState.FILLED) {
                    const newRow = row + i - offsetRow;
                    const newCol = col + j - offsetCol;
                    if (newRow >= 0 && newRow < this.rows && newCol >= 0 && newCol < this.cols) {
                        this.grid[newRow][newCol] = new Cell(CellState.FILLED, shape[i][j].getColor());
                        const pos = new Position(newRow, newCol);
                        this.blockMap.set(pos, block); // Use Position as key
                        console.log(`Placed block at (${pos.getRow()}, ${pos.getCol()}) in blockMap`);
                    }
                }
            }
        }
    }

    isFull() {
        return this.grid.every(row => row.every(cell => cell.getState() === CellState.FILLED));
    }

    canRemoveBlock(row, col) {
        const pos = new Position(row, col);
        console.log(`Checking removal at (${pos.getRow()}, ${pos.getCol()}), blockMap has:`, Array.from(this.blockMap.keys()).map(p => `(${p.getRow()}, ${p.getCol()})`));
        return this.blockMap.has(pos);
    }

    removeBlock(row, col) {
        const pos = new Position(row, col);
        console.log(`Attempting to remove block at (${pos.getRow()}, ${pos.getCol()}), blockMap size: ${this.blockMap.size}`);
        if (!this.blockMap.has(pos)) {
            console.log(`No block found at (${pos.getRow()}, ${pos.getCol()})`);
            return null; // No block at this position
        }

        const block = this.blockMap.get(pos);

        // Clear only the cells associated with this specific position's block
        for (let rowIdx = 0; rowIdx < this.rows; rowIdx++) {
            for (let colIdx = 0; colIdx < this.cols; colIdx++) {
                const currentPos = new Position(rowIdx, colIdx);
                if (this.blockMap.get(currentPos) === block) {
                    this.grid[rowIdx][colIdx].setState(CellState.EMPTY);
                    this.grid[rowIdx][colIdx].setColor([0, 0, 0]);
                    this.blockMap.delete(currentPos);
                    console.log(`Removed cell at (${rowIdx}, ${colIdx})`);
                }
            }
        }

        console.log(`Successfully removed block, new blockMap size: ${this.blockMap.size}`);
        return block; // Return the removed block
    }
}

export default Board;