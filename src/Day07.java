import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Day07 {

    private static final int ROW_COUNT = 142;
    private static final int COL_COUNT = 141;

    private static final char[][] diagram = new char[ROW_COUNT][COL_COUNT];
    private static int inputIdx = 0;
    private static int splitCount;

    public static void main(String[] args) {
        readFile(Day07.class.getSimpleName().toLowerCase() + ".txt");
        System.out.println("Part 1 result: " + solve1());
        System.out.println("Part 2 result: " + solve2());
    }

    private static void printInput() {
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COL_COUNT; j++)
                System.out.print(diagram[i][j]);
            System.out.println();
        }
    }

    private static void printXCount() {
        int xCount = 0;

        for (int i = 0; i < ROW_COUNT; i++)
            for (int j = 0; j < COL_COUNT; j++)
                if (diagram[i][j] == 'x')
                    xCount++;

        System.out.println(xCount);
    }

    private static int[] getStartCoordinates() {
        for (int i = 0; i < ROW_COUNT; i++)
            for (int j = 0; j < COL_COUNT; j++)
                if (diagram[i][j] == 'S')
                    return new int[] {i, j};

        return new int[] {-1, -1};
    }

    private static int solve1() {
        //printInput();
        splitCount = 0;
        int[] startPos = getStartCoordinates();
        split1(startPos[0], startPos[1]);
        //printInput();
        //printXCount();
        return splitCount;
    }

    private static void split1(int row, int col) {
        do {
            //diagram[row][col] = '|';
            row++;
        } while (row < ROW_COUNT && diagram[row][col] == '.');

        if (row == ROW_COUNT || diagram[row][col] == 'x' || diagram[row][col] == '|')
            return;

        diagram[row][col] = 'x';
        splitCount++;

        if (col > 0)
            split1(row, col - 1);

        if (col + 1 < COL_COUNT)
            split1(row, col + 1);
    }

    private static void restoreInput() {
        for (int i = 0; i < ROW_COUNT; i++)
            for (int j = 0; j < COL_COUNT; j++)
                if (diagram[i][j] == 'x')
                    diagram[i][j] = '^';
    }

    private static long solve2() {
        restoreInput();
        int[] startPos = getStartCoordinates();
        return split2(startPos[0], startPos[1], new HashMap<>());
    }

    private static long split2(int i, int j, Map<Pair<Integer, Integer>, Long> cache) {
        if (j < 0 || j >= COL_COUNT)
            return 0L;

        if (i == ROW_COUNT - 1)
            return 1L;

        Pair<Integer, Integer> key = new Pair<>(i, j);
        if (!cache.containsKey(key)) {
            long result = 0L;
            int[] offsets = {-1, 1};

            for (int offset : offsets) {
                int col = j + offset;

                // skip out-of-bounds sideways moves
                if (col < 0 || col >= COL_COUNT)
                    continue;

                // find the first '^' in the rows below
                int foundRow = -1;
                for (int idx = i + 1; idx < ROW_COUNT; idx++) {
                    if (diagram[idx][col] == '^') {
                        foundRow = idx;
                        break;
                    }
                }

                if (foundRow != -1)
                    result += split2(foundRow, col, cache);
                else
                    result += 1L;
            }

            cache.put(key, result);
        }

        return cache.get(key);
    }

    private record Pair<A, B>(A first, B second) {
        @Override
        public String toString() {
            return "(" + first + "," + second + ")";
        }
    }

    private static void treatInputLine(String line) {
        diagram[inputIdx++] = line.toCharArray();
    }

    private static void readFile(String filename) {
        File myObj = new File(System.getProperty("user.dir") + "/inputs/" + filename);

        try (Scanner reader = new Scanner(myObj)) {
            while (reader.hasNextLine())
                treatInputLine(reader.nextLine());

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while trying to read " + filename);
            e.printStackTrace();
        }
    }
}