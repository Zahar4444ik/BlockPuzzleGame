import Cell from "./cell/Cell";
import CellState from "./cell/CellState";

class Board {
    constructor(rows, cols, shape = null) {
        this.rows = rows;
        this.cols = cols;
        if (shape) {
            this.grid = shape;
        } else {
            this.grid = Array(rows)
                .fill()
                .map(() => Array(cols).fill().map(() => new Cell(CellState.EMPTY)));
        }
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

    drawBoard(p, cellSize, xOffset, yOffset) {
        for (let row = 0; row < this.rows; row++) {
            for (let col = 0; col < this.cols; col++) {
                const cell = this.grid[row][col];
                const x = xOffset + col * cellSize;
                const y = yOffset + row * cellSize;

                if (cell.getState() === CellState.FILLED && cell.getColor()) {
                    p.fill(cell.getColor());
                    p.noStroke();
                } else {
                    p.fill(28, 37, 65); // #1C2541 for empty cells
                    p.stroke(111, 255, 233); // #6FFFE9 for borders
                }
                p.rect(x, y, cellSize, cellSize);
            }
        }
    }

    isFull() {
        for (let row = 0; row < this.rows; row++) {
            for (let col = 0; col < this.cols; col++) {
                if (this.grid[row][col].getState() === CellState.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    canPlaceBlock(block, anchorRow, anchorCol, offsetRow, offsetCol) {
        const shape = block.getShape();
        const height = block.getHeight();
        const width = block.getWidth();

        const topLeftRow = anchorRow - offsetRow;
        const topLeftCol = anchorCol - offsetCol;

        if (topLeftRow < 0 || topLeftCol < 0 || topLeftRow + height > this.rows || topLeftCol + width > this.cols) {
            return false;
        }

        for (let rowIdx = 0; rowIdx < height; rowIdx++) {
            for (let colIdx = 0; colIdx < width; colIdx++) {
                if (
                    shape[rowIdx][colIdx].getState() === CellState.FILLED &&
                    this.grid[topLeftRow + rowIdx][topLeftCol + colIdx].getState() !== CellState.EMPTY
                ) {
                    return false;
                }
            }
        }

        return true;
    }

    placeBlock(block, anchorRow, anchorCol, offsetRow, offsetCol) {
        const topLeftRow = anchorRow - offsetRow;
        const topLeftCol = anchorCol - offsetCol;

        if (!this.canPlaceBlock(block, anchorRow, anchorCol, offsetRow, offsetCol)) {
            return false;
        }

        const shape = block.getShape();
        const width = block.getWidth();
        const height = block.getHeight();

        for (let rowIdx = 0; rowIdx < height; rowIdx++) {
            for (let colIdx = 0; colIdx < width; colIdx++) {
                if (shape[rowIdx][colIdx].getState() === CellState.FILLED) {
                    this.grid[topLeftRow + rowIdx][topLeftCol + colIdx].setState(CellState.FILLED);
                    this.grid[topLeftRow + rowIdx][topLeftCol + colIdx].setColor(block.getColor());
                }
            }
        }

        return true;
    }
}

export default Board;