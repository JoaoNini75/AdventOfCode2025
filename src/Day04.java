import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day04 {

    private static final int SIZE = 135;
    private static final char[][] diagram = new char[SIZE][SIZE];
    private static int inputIdx = 0;

    public static void main(String[] args) {
        readFile(Day04.class.getSimpleName().toLowerCase() + ".txt");
        System.out.println("Puzzle 1 solution: " + solve1());
        System.out.println("Puzzle 2 solution: " + solve2());
    }

    private static int solve1() {
        int paperRolls = 0;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (diagram[i][j] == '@' && isAccessible(i, j))
                    paperRolls++;

                System.out.println("paperRolls: " + paperRolls);
            }
        }

        return paperRolls;
    }

    private static boolean isAccessible(int row, int col) {
        int adjacent = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int currRow = row + i;
                int currCol = col + j;

                if ((i != 0 || j != 0) && currRow >= 0 && currRow < SIZE &&
                        currCol >= 0 && currCol < SIZE &&
                        diagram[currRow][currCol] == '@') {
                    adjacent++;
                }
            }
        }

        System.out.println("row: " + row + " col: " + col + " adjacent: " + adjacent);
        return adjacent < 4;
    }

    private static long solve2() {
        int paperRolls = 0;
        int lastRollCount;

        do {
            lastRollCount = paperRolls;

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (diagram[i][j] == '@' && isAccessible(i, j)) {
                        paperRolls++;
                        diagram[i][j] = '.';
                    }

                    System.out.println("paperRolls: " + paperRolls);
                }
            }

        } while (paperRolls != lastRollCount);

        return paperRolls;
    }

    private static void treatInput(String line) {
        diagram[inputIdx++] = line.toCharArray();
    }

    private static void readFile(String filename) {
        File myObj = new File(System.getProperty("user.dir") + "/inputs/" + filename);

        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine())
                treatInput(myReader.nextLine());

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while trying to read " + filename);
            e.printStackTrace();
        }
    }
}