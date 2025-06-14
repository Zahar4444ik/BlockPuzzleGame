import CellState from "./cell/CellState";
import Cell from "./cell/Cell";

class Board {
    constructor(rows, cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = Array(rows).fill().map(() => Array(cols).fill(new Cell(CellState.EMPTY, [0, 0, 0])));
        this.placedBlocks = new Map();
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

    setGrid(grid) {
        this.grid = grid;
    }

    getPlacedBlocks() {
        return this.placedBlocks;
    }

    setPlacedBlocks(placedBlocks) {
        this.placedBlocks = placedBlocks;
    }
}

export default Board;