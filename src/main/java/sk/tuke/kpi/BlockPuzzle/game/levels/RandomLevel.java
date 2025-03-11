package sk.tuke.kpi.BlockPuzzle.game.levels;

import sk.tuke.kpi.BlockPuzzle.core.blocks.Block;
import sk.tuke.kpi.BlockPuzzle.core.Board;

import java.util.List;

public class RandomLevel implements Level{
    @Override
    public Board generateBoard() {
        return null;
    }

    @Override
    public List<Block> generateBlocks() {
        return List.of();
    }
}
