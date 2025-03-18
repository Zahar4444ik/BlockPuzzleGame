package sk.tuke.kpi.BlockPuzzle.game.levels;

import sk.tuke.kpi.BlockPuzzle.game.core.board.Block;
import sk.tuke.kpi.BlockPuzzle.game.core.board.Board;

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
