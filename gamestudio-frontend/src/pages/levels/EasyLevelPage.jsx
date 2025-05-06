import React, {useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import "../../assets/css/AnyLevelPage.css";
import Board from "../../core/Board";
import Cell from "../../core/cell/Cell";
import CellState from "../../core/cell/CellState";
import generateEasyBlocks from "../../core/level_generators/easyLevel";

const EasyLevelPage = () => {
    const { search } = useLocation();
    const navigate = useNavigate();
    const query = new URLSearchParams(search);
    const rows = parseInt(query.get("rows")) || 5;
    const cols = parseInt(query.get("cols")) || 5;

    const initialBoard = new Board(rows, cols);
    const [board, setBoard] = useState(initialBoard);
    const [availableBlocks, setAvailableBlocks] = useState(generateEasyBlocks().map((block, index) => ({ block, id: `block-${index}` })));
    const [hasWon, setHasWon] = useState(false);

    const getDisplayBlocks = () => {
        const leftBlocks = availableBlocks.slice(0, 2); // First two blocks (or fewer)
        const rightBlocks = availableBlocks.slice(2, 4); // Next two blocks (or fewer)
        console.log("Left blocks:", leftBlocks.map(b => b.id));
        console.log("Right blocks:", rightBlocks.map(b => b.id));
        return { leftBlocks, rightBlocks };
    };

    const { leftBlocks, rightBlocks } = getDisplayBlocks();

    const handlePlayAgain = () => {
        setBoard(new Board(rows, cols)); // Reset the board
        setAvailableBlocks(generateEasyBlocks().map((block, index) => ({ block, id: `block-${index}` }))); // Generate new blocks
        setHasWon(false); // Clear win state
        console.log("Game reset: New board and blocks generated.");
    };

    const handleExit = () => {
        navigate("/levels");
        console.log("Navigated to /levels");
    };

    const handleDragStart = (e, blockId) => {
        const blockIndex = availableBlocks.findIndex(b => b.id === blockId);
        const block = availableBlocks[blockIndex].block;
        const shape = block.getShape();
        const cellSize = 50; // Matches CSS width/height of cells
        const blockRect = e.currentTarget.getBoundingClientRect();
        const offsetX = e.clientX - blockRect.left;
        const offsetY = e.clientY - blockRect.top;

        let cellRow = -1;
        let cellCol = -1;

        // Find the cell based on mouse position
        for (let row = 0; row < shape.length; row++) {
            for (let col = 0; col < shape[0].length; col++) {
                const cellLeft = col * cellSize;
                const cellTop = row * cellSize;
                const cellRight = cellLeft + cellSize;
                const cellBottom = cellTop + cellSize;

                if (
                    offsetX >= cellLeft &&
                    offsetX <= cellRight &&
                    offsetY >= cellTop &&
                    offsetY <= cellBottom &&
                    shape[row][col].getState() === CellState.FILLED
                ) {
                    cellRow = row;
                    cellCol = col;
                    break;
                }
            }
            if (cellRow !== -1) break;
        }

        if (cellRow === -1 || cellCol === -1) {
            console.error("No filled cell found under mouse position");
            e.preventDefault();
            return;
        }

        const dragData = `${blockIndex}:${cellRow}:${cellCol}`;
        e.dataTransfer.setData("text/plain", dragData);
        console.log(`Dragging block ${blockId} (index ${blockIndex}) from cell (${cellRow}, ${cellCol})`);
    };

    const handleDragOver = (e, rowIndex, colIndex) => {
        if (hasWon) return; // Prevent interaction after winning
        e.preventDefault(); // Allow drop
        const dragData = e.dataTransfer.getData("text/plain");
        const [blockIndexStr, offsetRowStr, offsetColStr] = dragData.split(":");
        const blockIndex = parseInt(blockIndexStr, 10);
        const offsetRow = parseInt(offsetRowStr, 10);
        const offsetCol = parseInt(offsetColStr, 10);

        console.log("Drag data parsed:", { blockIndex, offsetRow, offsetCol });

        if (isNaN(blockIndex) || blockIndex < 0 || blockIndex >= availableBlocks.length || isNaN(offsetRow) || isNaN(offsetCol)) {
            console.error("Invalid drag data:", { blockIndex, offsetRow, offsetCol });
            return;
        }

        const block = availableBlocks[blockIndex].block;
        const isValid = board.canPlaceBlock(block, rowIndex, colIndex, offsetRow, offsetCol);
        console.log(`Drag over (${rowIndex}, ${colIndex}), valid: ${isValid}`);
    };

    const handleDrop = (e, rowIndex, colIndex) => {
        if (hasWon) return; // Prevent interaction after winning
        e.preventDefault();
        const dragData = e.dataTransfer.getData("text/plain");
        const [blockIndexStr, offsetRowStr, offsetColStr] = dragData.split(":");
        const blockIndex = parseInt(blockIndexStr, 10);
        const offsetRow = parseInt(offsetRowStr, 10);
        const offsetCol = parseInt(offsetColStr, 10);

        if (isNaN(blockIndex) || blockIndex < 0 || blockIndex >= availableBlocks.length || isNaN(offsetRow) || isNaN(offsetCol)) {
            console.error("Invalid drag data:", { blockIndex, offsetRow, offsetCol });
            return;
        }

        const block = availableBlocks[blockIndex].block;
        const newBoard = new Board(rows, cols);
        newBoard.grid = board.getGrid().map(row =>
            row.map(cell => {
                return new Cell(cell.getState(), cell.getColor());
            })
        );
        const isValid = newBoard.canPlaceBlock(block, rowIndex, colIndex, offsetRow, offsetCol);
        if (isValid) {
            newBoard.placeBlock(block, rowIndex, colIndex, offsetRow, offsetCol);
            setBoard(newBoard);
            // Remove the placed block from availableBlocks
            setAvailableBlocks(prevBlocks => prevBlocks.filter((_, idx) => idx !== blockIndex));
            console.log(`Placed block ${availableBlocks[blockIndex].id} (index ${blockIndex}) with cell (${offsetRow}, ${offsetCol}) at (${rowIndex}, ${colIndex})`);

            // Check if the board is full after placing the block
            if (newBoard.isFull()) {
                setHasWon(true);
                console.log("Board is full! You win!");
            }
        } else {
            console.log(`Cannot place block ${availableBlocks[blockIndex].id} at (${rowIndex}, ${colIndex})`);
        }
    };

    return (
        <div className="level-page">
            {hasWon && (
                <div className="win-overlay">
                    <div className="win-message">
                        You Win!
                    </div>
                    <div className="win-buttons">
                        <button className="win-button play-again" onClick={handlePlayAgain}>
                            Play Again
                        </button>
                        <button className="win-button exit" onClick={handleExit}>
                            Exit
                        </button>
                    </div>
                </div>
            )}
            <div className={`game-layout ${hasWon ? "disabled" : ""}`}>
                <div className="block-section left-blocks">
                    {leftBlocks.map(({ block, id }) => (
                        <div
                            key={id}
                            className="block"
                            draggable={!hasWon}
                            onDragStart={(e) => handleDragStart(e, id)}
                            style={{
                                gridTemplateColumns: `repeat(${block.getWidth()}, 50px)`,
                                gridTemplateRows: `repeat(${block.getHeight()}, 50px)`,
                            }}
                        >
                            {block.getShape().map((row, rowIndex) =>
                                row.map((cell, colIndex) => (
                                    <div
                                        key={`left-block-${id}-${rowIndex}-${colIndex}`}
                                        className={`cell ${cell.getState() === CellState.FILLED ? "filled" : "empty"}`}
                                        style={{
                                            backgroundColor:
                                                cell.getState() === CellState.FILLED && cell.getColor()
                                                    ? `rgb(${cell.getColor().join(",")})`
                                                    : "transparent",
                                            border:
                                                cell.getState() === CellState.FILLED
                                                    ? "1px solid #0B132B"
                                                    : "none",
                                        }}
                                    />
                                ))
                            )}
                        </div>
                    ))}
                </div>

                <div className="board">
                    {board.getGrid().map((row, rowIndex) =>
                        row.map((cell, colIndex) => (
                            <div
                                key={`${rowIndex}-${colIndex}`}
                                className={`cell ${cell.getState() === CellState.FILLED ? "filled" : "empty"}`}
                                onDragOver={(e) => handleDragOver(e, rowIndex, colIndex)}
                                onDrop={(e) => handleDrop(e, rowIndex, colIndex)}
                                style={{
                                    backgroundColor:
                                        cell.getState() === CellState.FILLED && cell.getColor()
                                            ? `rgb(${cell.getColor().join(",")})`
                                            : "#0B132B",
                                    border: "1px solid #0B132B",
                                }}
                            />
                        ))
                    )}
                </div>

                <div className="block-section right-blocks">
                    {rightBlocks.map(({ block, id }) => (
                        <div
                            key={id}
                            className="block"
                            draggable={!hasWon}
                            onDragStart={(e) => handleDragStart(e, id)}
                            style={{
                                gridTemplateColumns: `repeat(${block.getWidth()}, 50px)`,
                                gridTemplateRows: `repeat(${block.getHeight()}, 50px)`,
                            }}
                        >
                            {block.getShape().map((row, rowIndex) =>
                                row.map((cell, colIndex) => (
                                    <div
                                        key={`right-block-${id}-${rowIndex}-${colIndex}`}
                                        className={`cell ${cell.getState() === CellState.FILLED ? "filled" : "empty"}`}
                                        style={{
                                            backgroundColor:
                                                cell.getState() === CellState.FILLED && cell.getColor()
                                                    ? `rgb(${cell.getColor().join(",")})`
                                                    : "transparent",
                                            border:
                                                cell.getState() === CellState.FILLED
                                                    ? "1px solid #0B132B"
                                                    : "none",
                                        }}
                                    />
                                ))
                            )}
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default EasyLevelPage;