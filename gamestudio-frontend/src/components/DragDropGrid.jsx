import React, { useRef, useState, useEffect } from "react";

const DragDropGrid = ({ rows, cols, cellSize, children, onDrop, onDragOver, draggable, blockIndex, onMove, isBoard }) => {
    const gridRef = useRef(null);
    const [draggedItem, setDraggedItem] = useState(null);
    const [offset, setOffset] = useState({ x: 0, y: 0 });
    const [isDragging, setIsDragging] = useState(false);

    const handleMouseDown = (e) => {
        if (draggable && e.button === 0) { // Left click only
            const rect = gridRef.current.getBoundingClientRect();
            const clickX = e.clientX - rect.left;
            const clickY = e.clientY - rect.top;
            const blockWidth = cols * cellSize;
            const blockHeight = rows * cellSize;
            setOffset({
                x: clickX - (blockWidth / 2),
                y: clickY - (blockHeight / 2),
            });
            setDraggedItem({ type: "block", index: blockIndex });
            setIsDragging(true);
            console.log("Mouse down on block:", blockIndex);
        }
    };

    const handleMouseMove = (e) => {
        if (isDragging && draggedItem && onMove) {
            const rect = gridRef.current.getBoundingClientRect();
            let newX = e.clientX - rect.left - offset.x;
            let newY = e.clientY - rect.top - offset.y;
            const maxX = window.innerWidth - cols * cellSize;
            const maxY = window.innerHeight - rows * cellSize;
            newX = Math.max(0, Math.min(newX, maxX));
            newY = Math.max(0, Math.min(newY, maxY));
            onMove(draggedItem.index, newX, newY);
            console.log(`Moving block ${draggedItem.index} to x=${newX}, y=${newY}`);
        }
    };

    const handleMouseUp = (e) => {
        if (isDragging && draggedItem && onDrop) {
            const rect = gridRef.current.getBoundingClientRect();
            const dropX = e.clientX - rect.left;
            const dropY = e.clientY - rect.top;
            const col = Math.floor(dropX / cellSize);
            const row = Math.floor(dropY / cellSize);
            if (row >= 0 && row < rows && col >= 0 && col < cols) {
                console.log(`Dropped block ${draggedItem.index} at position (${row}, ${col})`);
                onDrop(draggedItem.index, row, col);
            } else {
                console.log("Drop out of bounds, reverting");
                if (onMove) onMove(draggedItem.index, null, null); // Revert to original position
            }
            setIsDragging(false);
            setDraggedItem(null);
        }
    };

    useEffect(() => {
        if (isDragging) {
            window.addEventListener("mousemove", handleMouseMove);
            window.addEventListener("mouseup", handleMouseUp);
        }
        return () => {
            window.removeEventListener("mousemove", handleMouseMove);
            window.removeEventListener("mouseup", handleMouseUp);
        };
    }, [isDragging, handleMouseMove, handleMouseUp]);

    return (
        <div
            ref={gridRef}
            className={`drag-drop-grid ${draggable ? "block" : ""} ${isBoard ? "board-grid" : ""}`}
            style={{
                display: "grid",
                gridTemplateColumns: `repeat(${cols}, ${cellSize}px)`,
                gridTemplateRows: `repeat(${rows}, ${cellSize}px)`,
                gap: "1px",
                background: "#6FFFE9",
                position: "relative",
            }}
            onMouseDown={handleMouseDown}
        >
            {children}
        </div>
    );
};

export default DragDropGrid;