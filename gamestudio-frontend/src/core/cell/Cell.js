import CellState from "./CellState";

class Cell {
    constructor(state = CellState.EMPTY, color = null) {
        this.state = state;
        this.color = color;
    }

    getState() {
        return this.state;
    }

    setState(state) {
        this.state = state;
    }

    getColor() {
        return this.color;
    }

    setColor(color) {
        this.color = color;
    }
}

export default Cell;