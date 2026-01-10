import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day11 {

    private static final int INPUT_SIZE = 574;
    private static final Map<String, String[]> connections = new HashMap<>(INPUT_SIZE);

    public static void main(String[] args) {
        readFile(Day11.class.getSimpleName().toLowerCase() + ".txt");

        long startTime = System.nanoTime();
        System.out.println("Part 1 result: " + solve1());
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Execution time in nanoseconds: " + duration + "\n");

        long startTime2 = System.nanoTime();
        System.out.println("Part 2 result: " + solve2());
        long endTime2 = System.nanoTime();
        long duration2 = (endTime2 - startTime2);
        System.out.println("Execution time in nanoseconds: " + duration2 + "\n");
    }

    private static long solve1() {
        return dfs("you", "out", new HashMap<>());
    }

    private static long solve2() {
        return dfs("svr", "dac", new HashMap<>()) *
                dfs("dac", "fft", new HashMap<>()) *
                dfs("fft", "out", new HashMap<>())
                +
                dfs("svr", "fft", new HashMap<>()) *
                dfs("fft", "dac", new HashMap<>()) *
                dfs("dac", "out", new HashMap<>());
    }

    private static long dfs(String device, String end, Map<String, Long> cache) {
        if (device.equals(end))
            return 1;

        if (!connections.containsKey(device))
            return 0;

        long res = 0;

        for (String nextDev : connections.get(device)) {
            if (cache.containsKey(nextDev))
                res += cache.get(nextDev);
            else
                res += dfs(nextDev, end, cache);
        }

        cache.put(device, res);
        return res;
    }

    private static void printInput() {
        for (Map.Entry<String, String[]> entry : connections.entrySet()) {
            System.out.print(entry.getKey() + ":");
            for (String out : entry.getValue())
                System.out.print(" " + out);
            System.out.println();
        }
    }

    private static void handleInputLine(String line) {
        String[] separated = line.split(": ");
        String device = separated[0];
        String[] output = separated[1].split(" ");
        connections.put(device, output);
    }

    private static void readFile(String filename) {
        File myObj = new File(System.getProperty("user.dir") + "/inputs/" + filename);

        try (Scanner reader = new Scanner(myObj)) {
            while (reader.hasNextLine())
                handleInputLine(reader.nextLine());

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while trying to read " + filename);
            e.printStackTrace();
        }
    }
}