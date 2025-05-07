import CellState from "../core/cell/CellState";

const handleDragStart = (e, blockId, blocks) => {
    const blockIndex = blocks.findIndex(b => b.id === blockId);
    const block = blocks[blockIndex].block;
    const shape = block.getShape();
    const cellSize = 50;
    const blockRect = e.currentTarget.getBoundingClientRect();
    const offsetX = e.clientX - blockRect.left;
    const offsetY = e.clientY - blockRect.top;

    let cellRow = -1;
    let cellCol = -1;

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
        e.preventDefault();
        return;
    }

    const dragData = `${blockIndex}:${cellRow}:${cellCol}`;
    e.dataTransfer.setData("text/plain", dragData);
};

const handleDragOver = (e, rowIndex, colIndex, board, blocks, hasWon) => {
    if (hasWon) return;
    e.preventDefault();
    const dragData = e.dataTransfer.getData("text/plain");
    const [blockIndexStr, offsetRowStr, offsetColStr] = dragData.split(":");
    const blockIndex = parseInt(blockIndexStr, 10);
    const offsetRow = parseInt(offsetRowStr, 10);
    const offsetCol = parseInt(offsetColStr, 10);


    if (isNaN(blockIndex) || blockIndex < 0 || blockIndex >= blocks.length || isNaN(offsetRow) || isNaN(offsetCol)) {
        console.error("Invalid drag data:", { blockIndex, offsetRow, offsetCol });
        return;
    }

    const block = blocks[blockIndex].block;
    const isValid = board.canPlaceBlock(block, rowIndex, colIndex, offsetRow, offsetCol);
    console.log(`Drag over (${rowIndex}, ${colIndex}), valid: ${isValid}`);
};

const handleDrop = ({
                        e,
                        rowIndex,
                        colIndex,
                        board,
                        blocks,
                        setBoard,
                        setAvailableBlocks,
                        hasWon,
                        setHasWon
                    }) => {
    if (hasWon) return false;
    e.preventDefault();
    const dragData = e.dataTransfer.getData("text/plain");
    const [blockIndexStr, offsetRowStr, offsetColStr] = dragData.split(":");
    const blockIndex = parseInt(blockIndexStr, 10);
    const offsetRow = parseInt(offsetRowStr, 10);
    const offsetCol = parseInt(offsetColStr, 10);

    if (isNaN(blockIndex) || blockIndex < 0 || blockIndex >= blocks.length || isNaN(offsetRow) || isNaN(offsetCol)) {
        return false;
    }

    const block = blocks[blockIndex].block;

    const isValid = board.canPlaceBlock(block, rowIndex, colIndex, offsetRow, offsetCol);
    if (isValid) {
        board.placeBlock(block, rowIndex, colIndex, offsetRow, offsetCol);
        setBoard(board); // Update the board state in GameDataReceiver
        setAvailableBlocks(prevBlocks => prevBlocks.filter((_, idx) => idx !== blockIndex));

        if (board.isFull()) {
            setHasWon(true);
        }
        return true; // Indicate successful placement
    } else {
        return false; // Indicate failed placement
    }
};

export { handleDragStart, handleDragOver, handleDrop };