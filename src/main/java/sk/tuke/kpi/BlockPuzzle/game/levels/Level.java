package sk.tuke.kpi.BlockPuzzle.game.levels;

import sk.tuke.kpi.BlockPuzzle.game.core.blocks.Block;
import sk.tuke.kpi.BlockPuzzle.game.core.Board;

import java.util.List;

public interface Level {
    Board generateBoard();
    List<Block> generateBlocks();
}
