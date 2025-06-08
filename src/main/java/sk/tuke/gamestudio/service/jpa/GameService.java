package sk.tuke.gamestudio.service.jpa;

import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.DTO.game.*;
import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.ConsoleColor;
import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.GamePrinter;
import sk.tuke.gamestudio.game.BlockPuzzle.core.Position;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Block;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Board;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.Cell;
import sk.tuke.gamestudio.game.BlockPuzzle.core.board.CellState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GameService {
    public GameStateDTO updateGameState(GameUpdateRequestDTO request) {
        GameStateDTO currentState = request.getCurrentState();
        GameMoveDTO action = request.getMove();

        // Convert DTO to Board
        Cell[][] grid = convertToCellArray(currentState.getGrid());
        List<Block> availableBlocks = convertToBlockList(currentState.getAvailableBlocks());
        Map<Position, Block> placedBlocks = convertToPositionBlockMap(currentState.getPlacedBlocks());
        Board board = new Board(grid.length, grid[0].length);
        board.setGrid(grid);
        board.setBlockMap(placedBlocks);

        // Process action
        boolean stateChanged = false;
        int blockIndex = action.getBlockIndex();
        if (blockIndex >= 0 && blockIndex < availableBlocks.size()) {
            Block block = availableBlocks.get(blockIndex);
            int row = action.getRow();
            int col = action.getCol();
            int offsetRow = action.getOffsetRow();
            int offsetCol = action.getOffsetCol();

            // Adjust starting position based on offset to match the top-left of the block shape
            int adjustedRow = row - offsetRow;
            int adjustedCol = col - offsetCol;

            if (board.canPlaceBlock(block, adjustedRow, adjustedCol)) {
                board.placeBlock(block, adjustedRow, adjustedCol);
                availableBlocks.remove(blockIndex);
                stateChanged = true;
            }
        }

        // Check win condition
        boolean hasWon = board.isFull();

        // Convert back to DTO
        return new GameStateDTO(
                convertToCellArrayDTO(board.getGrid()),
                convertToBlockListDTO(availableBlocks),
                convertToPlacedBlocksDTO(board.getBlockMap()),
                0, // Score can be implemented later
                hasWon
        );
    }

    public GameStateDTO convertToGameStateDTO(Board board, List<Block> availableBlocks) {
        return new GameStateDTO(
                convertToCellArrayDTO(board.getGrid()),
                convertToBlockListDTO(availableBlocks),
                convertToPlacedBlocksDTO(board.getBlockMap()),
                0,
                board.isFull()
        );
    }

    private Cell[][] convertToCellArray(CellDTO[][] dtoGrid) {
        if (dtoGrid == null) return null;
        Cell[][] grid = new Cell[dtoGrid.length][dtoGrid[0].length];
        for (int i = 0; i < dtoGrid.length; i++) {
            for (int j = 0; j < dtoGrid[0].length; j++) {
                CellDTO dto = dtoGrid[i][j];
                grid[i][j] = new Cell(CellState.valueOf(dto.getState()), ConsoleColor.fromRgb(dto.getColor()));
            }
        }
        return grid;
    }

    private List<Block> convertToBlockList(List<BlockDTO> dtoBlocks) {
        if (dtoBlocks == null) return null;
        return dtoBlocks.stream()
                .map(dto -> new Block(convertToCellArray(dto.getShape()), ConsoleColor.fromRgb(dto.getColor())))
                .collect(Collectors.toList());
    }

    private Map<Position, Block> convertToPositionBlockMap(Map<String, BlockDTO> dtoMap) {
        if (dtoMap == null) return null;
        Map<Position, Block> blockMap = new HashMap<>();
        for (Map.Entry<String, BlockDTO> entry : dtoMap.entrySet()) {
            String[] parts = entry.getKey().split(",");
            Position pos = new Position(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            Block block = new Block(convertToCellArray(entry.getValue().getShape()), ConsoleColor.fromRgb(entry.getValue().getColor()));
            blockMap.put(pos, block);
        }
        return blockMap;
    }

    private CellDTO[][] convertToCellArrayDTO(Cell[][] grid) {
        if (grid == null) return null;
        CellDTO[][] dtoGrid = new CellDTO[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                Cell cell = grid[i][j];
                dtoGrid[i][j] = new CellDTO(cell.getState(), cell.getColor().getRgb());
            }
        }
        return dtoGrid;
    }

    private List<BlockDTO> convertToBlockListDTO(List<Block> blocks) {
        if (blocks == null) return null;
        return blocks.stream()
                .map(block -> new BlockDTO(
                        convertToCellArrayDTO(block.getShape()),
                        block.getColor().getRgb()
                ))
                .collect(Collectors.toList());
    }

    private Map<String, BlockDTO> convertToPlacedBlocksDTO(Map<Position, Block> blockMap) {
        if (blockMap == null) return null;
        Map<String, BlockDTO> dtoMap = new HashMap<>();
        for (Map.Entry<Position, Block> entry : blockMap.entrySet()) {
            Position pos = entry.getKey();
            Block block = entry.getValue();
            dtoMap.put(pos.getRow() + "," + pos.getCol(), new BlockDTO(
                    convertToCellArrayDTO(block.getShape()),
                    block.getColor().getRgb()
            ));
        }
        return dtoMap;
    }
}