import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day06 {

    private static final int NUMBER_LINE_COUNT = 4;
    private static final int LINE_SIZE = 1000;

    @SuppressWarnings("unchecked")
    private static final List<Long>[] numbers = new ArrayList[NUMBER_LINE_COUNT];
    private static final List<Boolean> isOpAdd = new ArrayList<>();
    private static int inputLineIdx = 0;

    public static void main(String[] args) {
        for (int i = 0; i < numbers.length; i++)
            numbers[i] = new ArrayList<>();

        readFile(Day06.class.getSimpleName().toLowerCase() + ".txt");
        System.out.println("Puzzle 1 solution: " + solve1());
        System.out.println("Puzzle 2 solution: " + solve2());
    }

    private static void printInput() {
        System.out.println(isOpAdd.size());
        for (int lineIdx = 0; lineIdx < NUMBER_LINE_COUNT; lineIdx++)
            System.out.println(numbers[lineIdx].size());

        System.out.println();

        for (int lineIdx = 0; lineIdx < NUMBER_LINE_COUNT; lineIdx++) {
            for (int probIdx = 0; probIdx < numbers[0].size(); probIdx++)
                System.out.print(numbers[lineIdx].get(probIdx) + ",");

            System.out.println();
        }

        System.out.println();

        for (boolean op : isOpAdd)
            System.out.println(op);
    }

    private static long solve1() {
        //printInput();
        final int inputLen = isOpAdd.size();
        long grandTotal = 0;

        for (int probIdx = 0; probIdx < inputLen; probIdx++) {
            boolean isAdd = isOpAdd.get(probIdx);
            long problemRes = isAdd ? 0 : 1;

            for (int lineIdx = 0; lineIdx < NUMBER_LINE_COUNT; lineIdx++) {
                if (isAdd)
                    problemRes += numbers[lineIdx].get(probIdx);
                else
                    problemRes *= numbers[lineIdx].get(probIdx);
            }

            //System.out.println(problemRes);
            grandTotal += problemRes;
        }

        return grandTotal;
    }

    private static long solve2() {
        long grandTotal = 0;
        return grandTotal;
    }

    private static void treatInputNumber(long number) {
        numbers[inputLineIdx].add(number);
        if (numbers[inputLineIdx].size() == LINE_SIZE)
            inputLineIdx++;
    }

    private static void treatInputOp(String str) {
        isOpAdd.add(str.trim().equals("+"));
    }

    private static void readFile(String filename) {
        File myObj = new File(System.getProperty("user.dir") + "/inputs/" + filename);

        try (Scanner reader = new Scanner(myObj)) {
            while (reader.hasNextLong())
                treatInputNumber(reader.nextLong());
            while (reader.hasNext())
                treatInputOp(reader.next());

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while trying to read " + filename);
            e.printStackTrace();
        }
    }
}