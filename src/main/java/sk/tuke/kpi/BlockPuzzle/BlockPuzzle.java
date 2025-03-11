package sk.tuke.kpi.BlockPuzzle;

import sk.tuke.kpi.BlockPuzzle.consoleui.BlockPuzzleConsole;

public class BlockPuzzle {
    public static void main(String[] args) {
        BlockPuzzleConsole game = new BlockPuzzleConsole(5, 5);
        game.start();
    }
}
