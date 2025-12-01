import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day01 {

    private static final int INPUT_LEN = 4780;

    private static final char[] directions = new char[INPUT_LEN];
    private static final int[] distances = new int[INPUT_LEN];
    private static int inputIdx = 0;

    public static void main(String[] args) {
        readFile("day01.txt");
        System.out.println("Puzzle 1 solution: " + solve1());
        System.out.println("Puzzle 2 solution: " + solve2());
    }

    private static int solve1() {
        int password = 0;
        int dial = 50;

        for (int i = 0; i < INPUT_LEN; i++) {
            int direction = directions[i] == 'R' ? 1 : -1;
            int rotation = (distances[i] * direction) % 100;
            dial += rotation;

            if (dial < 0)
                dial = 100 + dial;
            if (dial > 99)
                dial -= 100;

            if (dial == 0)
                password++;

            /*System.out.println("direction: " + direction +
                    "\t rotation: " + rotation +
                    "\t dial: " + dial +
                    "\t password: " + password);*/
        }

        return password;
    }

    private static int solve2() {
        int password = 0;
        int dial = 50;

        for (int i = 0; i < INPUT_LEN; i++) {
            int prev = dial;
            char dir = directions[i];
            int d = distances[i];
            int rawRotation = (dir == 'R') ? d : -d;

            int firstHit;
            if (dir == 'R')
                firstHit = (100 - prev) % 100;
            else
                firstHit = prev % 100;

            if (firstHit == 0)
                firstHit = 100;

            int hitsDuring = 0;
            if (d >= firstHit)
                hitsDuring = 1 + (d - firstHit) / 100;

            password += hitsDuring;
            dial = ((prev + rawRotation) % 100 + 100) % 100;

            System.out.println("rawRotation: " + rawRotation +
                    "\t dial: " + dial +
                    "\t password: " + password);
        }

        return password;
    }

    private static void treatInput(String line) {
        directions[inputIdx] = line.charAt(0);
        distances[inputIdx++] = Integer.parseInt(line.substring(1));
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