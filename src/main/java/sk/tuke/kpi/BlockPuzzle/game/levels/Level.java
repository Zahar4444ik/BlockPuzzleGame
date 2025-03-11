package sk.tuke.kpi.BlockPuzzle.game.levels;

import sk.tuke.kpi.BlockPuzzle.core.blocks.Block;
import sk.tuke.kpi.BlockPuzzle.core.Board;

import java.util.List;

public interface Level {
    Board generateBoard();
    List<Block> generateBlocks();
}
