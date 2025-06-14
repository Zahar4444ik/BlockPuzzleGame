import React, { useState, useEffect, useCallback } from "react";
import Board from "../core/Board";
import Cell from "../core/cell/Cell";
import CellState from "../core/cell/CellState";
import Position from "../core/Position";

class Block {
    constructor(shape, color) {
        this.shape = shape;
        this.color = color;
    }

    getShape() {
        return this.shape;
    }

    getWidth() {
        return this.shape[0].length;
    }

    getHeight() {
        return this.shape.length;
    }

    getColor() {
        return this.color;
    }
}

const GameDataReceiver = ({ difficulty, onDataReceived, children }) => {
    const [boardState, setBoardState] = useState(null); // Current board state
    const [availableBlocks, setAvailableBlocks] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchGameData = useCallback(async () => {
        try {
            const response = await fetch(`http://localhost:9090/api/game/data?difficulty=${difficulty}`, { credentials: "include" });
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();

            console.log("Fetched board data:", data);
            const newBoard = new Board(data.grid.length, data.grid[0].length);
            newBoard.grid = data.grid.map(row =>
                row.map(cell => {
                    let cellState;
                    switch (cell.state) {
                        case "FILLED":
                            cellState = CellState.FILLED;
                            break;
                        default:
                            cellState = CellState.EMPTY_BOARD;
                            break;
                    }
                    return new Cell(cellState, cell.color);
                })
            );
            newBoard.setPlacedBlocks(
                new Map(
                    Object.entries(data.placedBlocks || {}).map(([key, block]) => {
                        const [row, col] = key.split(",");
                        return [
                            new Position(parseInt(row), parseInt(col)),
                            new Block(
                                block.shape.map(row => row.map(cell => new Cell(cell.state, cell.color))),
                                block.color
                            )
                        ];
                    })
                )
            );

            const blocks = data.availableBlocks.map((blockData, index) => {
                const shape = blockData.shape.map(row =>
                    row.map(cell => {
                        let cellState;
                        switch (cell.state) {
                            case "FILLED":
                                cellState = CellState.FILLED;
                                break;
                            default:
                                cellState = CellState.EMPTY_BLOCK;
                                break;
                        }
                        return new Cell(cellState, cell.color);
                    })
                );
                return { block: new Block(shape, blockData.color), id: `block-${index}` };
            });

            setBoardState(newBoard);
            setAvailableBlocks(blocks);
            if (onDataReceived) onDataReceived(newBoard, blocks);
            setIsLoading(false);
            console.log("Fetched initial board state:", newBoard.getGrid());
        } catch (err) {
            setError(err.message);
            setIsLoading(false);
            console.error("Error fetching game data:", err);
        }
    }, [difficulty, onDataReceived]);

    useEffect(() => {
        fetchGameData();
    }, [fetchGameData]);

    const setBoard = (newBoard) => {
        console.log("Updating board state:", newBoard.getGrid());
        setBoardState(newBoard);
    };

    if (isLoading) return <div className="level-page">Loading...</div>;
    if (error) return <div className="level-page">Error: {error}</div>;

    return children({ board: boardState, availableBlocks, setBoard, setAvailableBlocks, refetch: fetchGameData });
};

export default GameDataReceiver;