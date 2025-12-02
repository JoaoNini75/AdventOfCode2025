import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Day02 {

    private static final List<long[]> ranges = new ArrayList<>();

    public static void main(String[] args) {
        readFile(Day02.class.getSimpleName().toLowerCase() + ".txt");
        System.out.println("Puzzle 1 solution: " + solve1());
        System.out.println("Puzzle 2 solution: " + solve2());
    }

    private static long solve1() {
        long result = 0;

        for (long[] range : ranges)
            for (long curr = range[0]; curr <= range[1]; curr++)
                if (isIdInvalid1(curr))
                    result += curr;

        return result;
    }

    private static boolean isIdInvalid1(long id) {
        String idStr = String.valueOf(id);
        if (idStr.length() % 2 == 1)
            return false;

        return idStr.substring(0, idStr.length() / 2).equals(
                idStr.substring(idStr.length() / 2));
    }

    private static long solve2() {
        long result = 0;

        for (long[] range : ranges)
            for (long curr = range[0]; curr <= range[1]; curr++)
                if (isIdInvalid2(curr)) {
                    System.out.println("invalid: " + curr);
                    result += curr;
                }

        return result;
    }

    private static boolean isIdInvalid2(long id) {
        String idStr = String.valueOf(id);
        int numDigits = idStr.length();
        List<Integer> divisors = getDivisors(numDigits);

        /*System.out.println("id: " + id);
        System.out.println("numDigits: " + numDigits);
        for (int d : divisors)
            System.out.print(d + ", ");
        System.out.println();*/

        for (int divisor : divisors) {
            String startingPoint = idStr.substring(0, divisor);
            int index = divisor;
            boolean foundInvalid = true;

            //System.out.println("startingPoint: " + startingPoint);

            while (index < idStr.length()) {
                String currStr = idStr.substring(index, index + divisor);

                //System.out.println("currStr: " + currStr);

                if (!currStr.equals(startingPoint)) {
                    foundInvalid = false;
                    break;
                }

                index += divisor;
            }

            if (foundInvalid)
                return true;
        }

        return false;
    }

    private static List<Integer> getDivisors(int num) {
        List<Integer> divisors = new LinkedList<>();

        for (int i = 1; i < num; i++)
            if (num % i == 0)
                divisors.add(i);

        return divisors;
    }

    private static void treatInput(String line) {
        for (String rawRange : line.split(",")) {
            String[] rangeStrs = rawRange.split("-");
            ranges.add(new long[] {
                    Long.parseLong(rangeStrs[0]),
                    Long.parseLong(rangeStrs[1])});
        }
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