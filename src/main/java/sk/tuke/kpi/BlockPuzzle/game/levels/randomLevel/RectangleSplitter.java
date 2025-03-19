package sk.tuke.kpi.BlockPuzzle.game.levels.randomLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RectangleSplitter {
    private final Random random = new Random();

    public List<int[]> splitBoard(int rows, int cols) {
        List<int[]> rectangles = new ArrayList<>();
        boolean[][] visited = new boolean[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!visited[row][col]) {
                    int width = random.nextInt(2, 5); // Random width (2-4)
                    int height = random.nextInt(2, 5); // Random height (2-4)

                    // Ensure rectangle fits within board bounds
                    if (row + height > rows) height = rows - row;
                    if (col + width > cols) width = cols - col;

                    // Mark cells as visited
                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < width; j++) {
                            visited[row + i][col + j] = true;
                        }
                    }

                    rectangles.add(new int[]{row, col, width, height});
                }
            }
        }

        return rectangles;
    }
}
