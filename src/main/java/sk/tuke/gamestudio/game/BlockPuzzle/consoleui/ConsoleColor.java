package sk.tuke.gamestudio.game.BlockPuzzle.consoleui;

import lombok.Getter;

public enum ConsoleColor {
    RESET("\u001B[0m", new int[]{255, 255, 255}),
    BLACK("\u001B[30m", new int[]{0, 0, 0}),
    RED("\u001B[31m", new int[]{255, 0, 0}),
    GREEN("\u001B[32m", new int[]{0, 255, 0}),
    YELLOW("\u001B[33m", new int[]{255, 255, 0}),
    BLUE("\u001B[34m", new int[]{0, 0, 255}),
    PURPLE("\u001B[35m", new int[]{128, 0, 128}),
    CYAN("\u001B[36m", new int[]{0, 255, 255}),
    WHITE("\u001B[37m", new int[]{255, 255, 255}),

    BOLD("\u001B[1m", new int[]{255, 255, 255}),
    UNDERLINE("\u001B[4m", new int[]{255, 255, 255}),;

    private final String code;
    @Getter
    private final int[] rgb;

    ConsoleColor(String code, int[] rgb) {
        this.code = code;
        this.rgb = rgb;
    }

    @Override
    public String toString() {
        return code;
    }

    public static ConsoleColor fromRgb(int[] rgb) {
        if (rgb == null || rgb.length != 3) return RESET;
        for (ConsoleColor color : ConsoleColor.values()) {
            if (color != BOLD && color != UNDERLINE && // Exclude style modifiers
                    rgb[0] == color.rgb[0] && rgb[1] == color.rgb[1] && rgb[2] == color.rgb[2]) {
                return color;
            }
        }
        return RESET; // Default to RESET if no match
    }
}
