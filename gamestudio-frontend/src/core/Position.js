class Position {
    constructor(row, col) {
        this.row = row;
        this.col = col;
    }

    getRow() {
        return this.row;
    }

    getCol() {
        return this.col;
    }

    equals(other) {
        if (!(other instanceof Position)) return false;
        return this.row === other.row && this.col === other.col;
    }
}

export default Position;