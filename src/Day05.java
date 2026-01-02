import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day05 {

    private static final List<List<Long>> fresh = new LinkedList<>();
    private static final List<Long> available = new LinkedList<>();
    private static boolean isInputFirstPart = true;

    public static void main(String[] args) {
        readFile(Day05.class.getSimpleName().toLowerCase() + ".txt");
        System.out.println("Puzzle 1 solution: " + solve1());
        System.out.println("Puzzle 2 solution: " + solve2());
    }

    private static void printFresh() {
        System.out.println("Fresh:");
        for (List<Long> range : fresh)
            System.out.println(range.get(0) + "-" + range.get(1));
        System.out.println();
    }

    private static int solve1() {
        int totalFresh = 0;

        for (long availableIng : available)
            if (isFresh(availableIng))
                totalFresh++;

        return totalFresh;
    }

    private static boolean isFresh(long ing) {
        for (List<Long> range : fresh)
            if (range.get(0) < ing && ing < range.get(1))
                return true;

        return false;
    }

    /**
     * First, order ranges by first ingredient.
     * Then, go through all the ranges in order and:
     *      if they have an intersection, connect them
     *      otherwise, add that range's amount of ingredients to the final result
     */
    private static long solve2() {
        //printFresh();
        heapSortFresh();
        //printFresh();

        long totalFresh = 0;
        List<Long> current = fresh.get(0);

        for (int i = 1; i < fresh.size(); i++) {
            List<Long> range = fresh.get(i);

            //System.out.println("current: " + (current.get(0) + "-" + current.get(1)));
            //System.out.println("totalFresh: " + totalFresh + "\n");

            if (range.get(0) <= current.get(1)) {
                current.set(1, Math.max(current.get(1), range.get(1)));
            } else {
                totalFresh += current.get(1) - current.get(0) + 1;
                current = range;
            }
        }

        totalFresh += current.get(1) - current.get(0) + 1;
        return totalFresh;
    }

    private static void heapSortFresh() {
        int n = fresh.size();

        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(n, i);

        for (int i = n - 1; i > 0; i--) {
            List<Long> temp = fresh.get(0);
            fresh.set(0, fresh.get(i));
            fresh.set(i, temp);
            heapify(i, 0);
        }
    }

    private static void heapify(int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && fresh.get(l).get(0) > fresh.get(largest).get(0))
            largest = l;

        if (r < n && fresh.get(r).get(0) > fresh.get(largest).get(0))
            largest = r;

        if (largest != i) {
            List<Long> temp = fresh.get(i);
            fresh.set(i, fresh.get(largest));
            fresh.set(largest, temp);
            heapify(n, largest);
        }
    }

    private static void treatInputLine(String line) {
        if (line.isEmpty()) {
            isInputFirstPart = false;
            return;
        }

        if (isInputFirstPart) {
            String[] rangeStr = line.split("-");
            List<Long> rangeList = new ArrayList<>(2);
            rangeList.add(Long.parseLong(rangeStr[0]));
            rangeList.add(Long.parseLong(rangeStr[1]));
            fresh.add(rangeList);
        } else {
            available.add(Long.parseLong(line));
        }
    }

    private static void readFile(String filename) {
        File myObj = new File(System.getProperty("user.dir") + "/inputs/" + filename);

        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine())
                treatInputLine(myReader.nextLine());

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while trying to read " + filename);
            e.printStackTrace();
        }
    }
}