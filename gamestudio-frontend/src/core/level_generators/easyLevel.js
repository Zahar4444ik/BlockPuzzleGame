import Block from '../Block';
import Cell from '../cell/Cell';
import colors from '../cell/Colors';
import CellState from '../cell/CellState';

const generateEasyBlocks = () => {
    return [
        new Block(
            [
                [new Cell(CellState.FILLED, colors.RED), new Cell(CellState.FILLED, colors.RED), new Cell(CellState.FILLED, colors.RED), new Cell(CellState.FILLED, colors.RED)],
                [new Cell(CellState.FILLED, colors.RED), new Cell(CellState.EMPTY), new Cell(CellState.EMPTY), new Cell(CellState.EMPTY)],
            ],
            colors.RED
        ),
        new Block(
            [
                [new Cell(CellState.EMPTY), new Cell(CellState.FILLED, colors.BLUE), new Cell(CellState.FILLED, colors.BLUE)],
                [new Cell(CellState.FILLED, colors.BLUE), new Cell(CellState.FILLED, colors.BLUE), new Cell(CellState.EMPTY)],
            ],
            colors.BLUE
        ),
        new Block(
            [
                [new Cell(CellState.FILLED, colors.GREEN), new Cell(CellState.FILLED, colors.GREEN)],
                [new Cell(CellState.FILLED, colors.GREEN), new Cell(CellState.FILLED, colors.GREEN)],
            ],
            colors.GREEN
        ),
        new Block(
            [
                [new Cell(CellState.EMPTY), new Cell(CellState.FILLED, colors.CYAN)],
                [new Cell(CellState.FILLED, colors.CYAN), new Cell(CellState.FILLED, colors.CYAN)],
                [new Cell(CellState.FILLED, colors.CYAN), new Cell(CellState.EMPTY)],
            ],
            colors.CYAN
        ),
        new Block(
            [
                [new Cell(CellState.FILLED, colors.YELLOW), new Cell(CellState.EMPTY)],
                [new Cell(CellState.FILLED, colors.YELLOW), new Cell(CellState.FILLED, colors.YELLOW)],
                [new Cell(CellState.FILLED, colors.YELLOW), new Cell(CellState.FILLED, colors.YELLOW)],
            ],
            colors.YELLOW
        ),
        new Block(
            [
                [new Cell(CellState.FILLED, colors.PURPLE)],
                [new Cell(CellState.FILLED, colors.PURPLE)],
                [new Cell(CellState.FILLED, colors.PURPLE)],
            ],
            colors.PURPLE
        ),
    ];
};

export default generateEasyBlocks;