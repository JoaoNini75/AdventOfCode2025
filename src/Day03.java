import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Day03 {

    private static final List<String> banks = new LinkedList<>();

    public static void main(String[] args) {
        readFile(Day03.class.getSimpleName().toLowerCase() + ".txt");
        System.out.println("Puzzle 1 solution: " + solve1());
        System.out.println("Puzzle 2 solution: " + solve2());
    }

    private static int solve1() {
        int total = 0;

        for (String bank : banks) {
            int firstMaxIdx = findMaxIdx(bank, 0, bank.length());
            int secondMaxIdx, jolts;

            if (firstMaxIdx == bank.length() - 1) {
                secondMaxIdx = findMaxIdx(bank, 0, bank.length() - 1);
                jolts = Integer.parseInt(bank.charAt(secondMaxIdx) +
                        "" + bank.charAt(firstMaxIdx));
            } else {
                secondMaxIdx = findMaxIdx(bank, firstMaxIdx + 1, bank.length());
                jolts = Integer.parseInt(bank.charAt(firstMaxIdx) +
                        "" + bank.charAt(secondMaxIdx));
            }

            total += jolts;

            System.out.println("firstMaxIdx: " + firstMaxIdx +
                    " secondMaxIdx: " + secondMaxIdx +
                    " jolts: " + jolts +
                    " total: " + total + "\n");
        }

        return total;
    }

    private static int findMaxIdx(String bank, int start, int end) {
        System.out.println("start: " + start + " end: " + end);
        int maxIdx = start;

        for (int i = start; i < end; i++) {
            if (bank.charAt(i) - '0' == 9)
                return i;
            if (bank.charAt(i) - '0' > bank.charAt(maxIdx) - '0')
                maxIdx = i;
        }

        return maxIdx;
    }

    private static long solve2() {
        long total = 0;

        for (String bank : banks) {
            StringBuilder jolts = new StringBuilder();
            int startIdx = 0;
            int endIdx, currMaxIdx;

            for (int battery = 12; battery > 0; battery--) {
                endIdx = bank.length() - battery + 1;
                currMaxIdx = findMaxIdx(bank, startIdx, endIdx);
                startIdx = currMaxIdx + 1;
                jolts.append(bank.charAt(currMaxIdx));
            }

            total += Long.parseLong(jolts.toString());

            System.out.println("jolts: " + jolts +
                    " total: " + total + "\n");
        }

        return total;
    }

    private static void treatInput(String line) {
        banks.add(line);
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