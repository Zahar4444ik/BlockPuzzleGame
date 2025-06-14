import Cell from "../core/cell/Cell";
import Block from "../core/Block";
import Position from "../core/Position";
import Board from "../core/Board";
import board from "../core/Board";

const API_BASE_URL = 'http://localhost:9090/api/game';

export const updateGameState = async (currentState, action) => {
    try {
        const move = action.move || "PLACE";

        const response = await fetch(`${API_BASE_URL}/update`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                currentState: {
                    grid: currentState.grid.map(row => row.map(cell => ({
                        state: cell.getState(),
                        color: cell.getColor() || [0, 0, 0]
                    }))),
                    availableBlocks: currentState.availableBlocks.map(b => ({
                        shape: b.block.getShape().map(row => row.map(cell => ({
                            state: cell.getState(),
                            color: cell.getColor() || [0, 0, 0]
                        }))),
                        color: b.block.getColor() || [0, 0, 0]
                    })),
                    placedBlocks: Array.from(currentState.placedBlocks.entries()).reduce((map, [key, value]) => {
                        map[`${key.getRow()},${key.getCol()}`] = {
                            shape: value.getShape().map(row => row.map(cell => ({
                                state: cell.getState(),
                                color: cell.getColor() || [0, 0, 0]
                            }))),
                            color: value.getColor() || [0, 0, 0]
                        };
                        return map;
                    }, {}),
                    hasWon: currentState.hasWon
                },
                move: {
                    move: move,
                    row: action.row,
                    col: action.col,
                    blockIndex: action.blockIndex,
                    offsetRow: action.offsetRow,
                    offsetCol: action.offsetCol
                }
            }),
            credentials: "include",
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Failed to update game state: ${response.status} - ${errorText}`);
        }

        const updatedState = await response.json();
        if (!updatedState.grid || !updatedState.availableBlocks) {
            throw new Error("Invalid response format from server");
        }

        const newBoard = new Board(updatedState.grid.length, updatedState.grid[0].length);
        newBoard.setGrid(
            updatedState.grid.map(row =>
                row.map(cell => new Cell(cell.state, cell.color))
            )
        );

        const newPlacedBlocks = new Map(
            Object.entries(updatedState.placedBlocks || []).map(([key, block]) => {
                const [row, col] = key.split(",");
                return [
                    new Position(parseInt(row), parseInt(col)),
                    new Block(
                        block.shape.map(row => row.map(cell => new Cell(cell.state, cell.color))),
                        block.color
                    )
                ];
            })
        );
        newBoard.setBlockMap(newPlacedBlocks);

        return {
            board: newBoard,
            availableBlocks: updatedState.availableBlocks.map((b, index) => ({
                block: new Block(
                    b.shape.map(row => row.map(cell => new Cell(cell.state, cell.color))),
                    b.color
                ),
                id: `block-${index}`
            })),
            placedBlocks: newBoard.getBlockMap(),

            hasWon: updatedState.hasWon
        };
    } catch (error) {
        console.error("Error updating game state:", error);
        throw error;
    }
};