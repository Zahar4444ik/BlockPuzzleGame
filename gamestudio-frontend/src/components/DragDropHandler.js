import CellState from "../core/cell/CellState";
import { updateGameState } from "../services/GameService";

const handleDragStart = (e, blockId, availableBlocks) => {
    const blockIndex = availableBlocks.findIndex(b => b.id === blockId);
    const block = availableBlocks[blockIndex].block;
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
};

const handleDrop = async ({
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

    try {
        const currentState = {
            grid: board.getGrid(),
            availableBlocks: blocks,
            placedBlocks: new Map(),
            hasWon: hasWon
        };

        const action = {
            row: rowIndex,
            col: colIndex,
            blockIndex: blockIndex,
            offsetRow: offsetRow,
            offsetCol: offsetCol
        };

        console.log("Sending request:", { currentState, action });

        const updatedState = await updateGameState(currentState, action);
        if (updatedState.availableBlocks.length === currentState.availableBlocks.length) {
            alert("Invalid placement. Try another position.");
            return false;
        }

        console.log("Received updated state:", updatedState);

        setBoard(updatedState.board);
        setAvailableBlocks(updatedState.availableBlocks);
        setHasWon(updatedState.hasWon);
        return true;
    } catch (error) {
        console.error("Error placing block:", error);
        alert("Failed to place block. Please try again.");
        return false;
    }
};

export { handleDragStart, handleDragOver, handleDrop };