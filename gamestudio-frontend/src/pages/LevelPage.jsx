import React, {useCallback, useEffect, useState} from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "../assets/css/AnyLevelPage.css";
import GameDataReceiver from "../components/GameDataReceiver";
import CellState from "../core/cell/CellState";
import {handleDragStart, handleDragOver, handleDrop, handleRemoveBlock} from "../components/DragDropHandler";
import { addAndSetScore } from "../services/ScoreService";
import { getCurrentNickname, getPlayerData, updatePlayer } from "../services/PlayerService";
import {usePlayerStats} from "../context/PlayerStatsContext";


const LevelPage = () => {
    const { search } = useLocation();
    const [scoreSaved, setScoreSaved] = useState(false);
    const navigate = useNavigate();
    const query = new URLSearchParams(search);
    const difficulty = query.get("difficulty") || "EASY";

    const [hasWon, setHasWon] = useState(false);
    const { setStats } = usePlayerStats();

    const [currScore, setCurrScore] = useState(0);

    // Save score when hasWon becomes true
    useEffect(() => {
        if (hasWon && !scoreSaved) {
            saveScore();
            setScoreSaved(true);
        }
    }, [hasWon, scoreSaved]);

    const handleDataReceived = useCallback(async (newBoard, newBlocks) => {
        console.log("Data received - Initial board:", newBoard.getGrid(), "Blocks:", newBlocks);
        const score = await getCurrentScore();
        setCurrScore(score);
    }, []);

    const getDisplayBlocks = (blocks) => {
        const leftBlocks = blocks.slice(0, 3); // First 3 blocks
        const rightBlocks = blocks.slice(3, 6); // Next 3 blocks
        return { leftBlocks, rightBlocks };
    };

    const handlePlayAgain = (refetch) => {
        setHasWon(false);
        setScoreSaved(false); // Reset scoreSaved for new game
        refetch();
    };

    const handleExit = () => {
        navigate("/levels");
    };

    const getCurrentScore = async () => {
        const nickname = await getCurrentNickname();
        if (!nickname) {
            console.error("No nickname found");
            return 0;
        }

        try {
            const playerData = await getPlayerData(nickname);
            return playerData.score || 0;
        } catch (error) {
            console.error("Failed to fetch player data:", error);
            return 0;
        }
    }

    // Function to save score when won
    const saveScore = async () => {
        const nickname = await getCurrentNickname();
        if (!nickname) {
            console.error("No nickname found");
            return;
        }

        // Fetch current player data
        let playerData;
        try {
            playerData = await getPlayerData(nickname);
        } catch (error) {
            console.error("Failed to fetch player data:", error);
            alert("Failed to fetch player data. Please try again.");
            return;
        }

        let winBonus = 0;
        switch (difficulty) {
            case "EASY":
                winBonus = 100;
                break;
            case "MEDIUM":
                winBonus = 150;
                break;
            case "HARD":
                winBonus = 200;
                break;
            default:
                winBonus = 100; // Default to EASY bonus
        }

        // Add winBonus to existing score
        const newScore = currScore + winBonus;
        setCurrScore(newScore);

        // Update player with new score and incremented gamesPlayed
        const updatedPlayerDTO = {
            nickname: playerData.nickname,
            score: newScore,
            gamesPlayed: (playerData.gamesPlayed || 0) + 1,
            lastPlayed: new Date().toISOString(),
        };

        try {
            await updatePlayer(updatedPlayerDTO);
        } catch (error) {
            console.error("Error updating player:", error);
            alert("Failed to update player score. Please try again.");
            return;
        }

        // Save the score entry (winBonus) to the scores table
        const scoreDTO = {
            game: "Block Puzzle",
            playerDTO: updatedPlayerDTO,
            points: newScore,
            playedOn: new Date().toISOString(),
        };

        try {
            await addAndSetScore(scoreDTO);
            setStats({
                score: newScore,
                gamesPlayed: updatedPlayerDTO.gamesPlayed,
            });
        } catch (error) {
            console.error("Error saving score:", error);
            alert("Failed to save score. Please try again.");
        }
    };

    return (
        <GameDataReceiver difficulty={difficulty} onDataReceived={handleDataReceived}>
            {({ board, availableBlocks, setBoard, setAvailableBlocks, refetch }) => {
                // console.log("Rendering board state in LevelPage:", board.getGrid());
                const { leftBlocks, rightBlocks } = getDisplayBlocks(availableBlocks);

                return (
                    <div className="level-page">
                        <h1>{difficulty} Level</h1>
                        <div className="score-display">
                            <span className="score-label">Score: </span>
                            <span className="score-value">{currScore}</span>
                        </div>
                        {hasWon && (
                            <div className="win-overlay">
                                <div className="win-message">
                                    You Win!
                                </div>
                                <div className="win-buttons">
                                    <button className="win-button play-again" onClick={() => handlePlayAgain(refetch)}>
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
                                    block && (
                                        <div
                                            key={id}
                                            className="block"
                                            draggable={!hasWon}
                                            onDragStart={(e) => handleDragStart(e, id, availableBlocks)}
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
                                    )
                                ))}
                            </div>

                            <div
                                className="board"
                                style={{
                                    gridTemplateColumns: `repeat(${board.getCols()}, 50px)`,
                                    gridTemplateRows: `repeat(${board.getRows()}, 50px)`,
                                }}
                            >
                                {board.getGrid().map((row, rowIndex) =>
                                    row.map((cell, colIndex) => (
                                        <div
                                            key={`${rowIndex}-${colIndex}`}
                                            className={`cell ${cell.getState() === CellState.FILLED ? "filled" : "empty"}`}
                                            onDragOver={(e) => handleDragOver(e, rowIndex, colIndex, board, availableBlocks, hasWon)}
                                            onDrop={(e) => handleDrop({
                                                e,
                                                rowIndex,
                                                colIndex,
                                                board,
                                                blocks: availableBlocks,
                                                setBoard,
                                                setAvailableBlocks,
                                                hasWon,
                                                setHasWon,
                                                currScore,
                                                setCurrScore,
                                            })}
                                            onContextMenu={(e) => {
                                                e.preventDefault();
                                                handleRemoveBlock({
                                                    rowIndex,
                                                    colIndex,
                                                    board,
                                                    blocks: availableBlocks,
                                                    setBoard,
                                                    setAvailableBlocks,
                                                    hasWon,
                                                    setHasWon,
                                                    currScore,
                                                    setCurrScore,
                                                });
                                            }}
                                            style={{
                                                backgroundColor:
                                                    cell.getState() === CellState.FILLED && cell.getColor()
                                                        ? `rgb(${cell.getColor().join(",")})`
                                                        : "#0B132B",
                                                border: "1px solid #0B132B",
                                                cursor: hasWon ? "not-allowed" : "pointer",
                                            }}
                                        />
                                    ))
                                )}
                            </div>

                            <div className="block-section right-blocks">
                                {rightBlocks.map(({ block, id }) => (
                                    block && (
                                        <div
                                            key={id}
                                            className="block"
                                            draggable={!hasWon}
                                            onDragStart={(e) => handleDragStart(e, id, availableBlocks)}
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
                                    )
                                ))}
                            </div>
                        </div>
                    </div>
                );
            }}
        </GameDataReceiver>
    );
};

export default LevelPage;