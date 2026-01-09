import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Predicate;

public class Day10 {

    private static final int INPUT_SIZE = 179;

    private static final List<boolean[]> indicators = new ArrayList<>(INPUT_SIZE);
    private static final List<List<List<Integer>>> buttons = new ArrayList<>(INPUT_SIZE);
    private static final List<List<Integer>> joltageReqs = new ArrayList<>(INPUT_SIZE);

    public static void main(String[] args) {
        readFile(Day10.class.getSimpleName().toLowerCase() + ".txt");
        System.out.println("Part 1 result: " + solve1());
        System.out.println("Part 2 result: " + solve2());
    }

    private static int solve1() {
        int minPresses = 0;

        for (int i = 0; i < indicators.size(); i++)
            minPresses += getIndicatorMinPresses(indicators.get(i), buttons.get(i));

        return minPresses;
    }

    private static int getIndicatorMinPresses(boolean[] targetIndicator,
                                              List<List<Integer>> currButtons) {

        final int n = targetIndicator.length;
        // Since indicator fits in an int,
        // use bitmask BFS (much faster / more memory efficient)
        int targetMask = 0;
        for (int i = 0; i < n; i++)
            if (targetIndicator[i])
                targetMask |= (1 << i);

        int[] buttonMasks = new int[currButtons.size()];
        for (int i = 0; i < currButtons.size(); i++) {
            int mask = 0;

            for (int idx : currButtons.get(i))
                mask |= (1 << idx);

            buttonMasks[i] = mask;
        }

        return bitmaskBFS(buttonMasks, targetMask);
    }

    private static int bitmaskBFS(int[] buttonMasks, int targetMask) {
        ArrayDeque<Integer> q = new ArrayDeque<>();
        HashMap<Integer, Integer> dist = new HashMap<>();
        int start = 0;
        q.add(start);
        dist.put(start, 0);

        while (!q.isEmpty()) {
            int state = q.poll();
            int d = dist.get(state);

            for (int bmask : buttonMasks) { // try each button
                int next = state ^ bmask;

                if (dist.containsKey(next))
                    continue;

                if (next == targetMask)
                    return d + 1;

                dist.put(next, d + 1);
                q.add(next);
            }
        }

        throw new IllegalStateException("No solution.");
    }

    private static long solve2() {
        long minPresses = 0;
        return minPresses;
    }

    private static void handleInputLine(String line) {
        String[] firstSep = line.split("]");
        String indicatorStr = firstSep[0].substring(1);
        String[] secondSep = firstSep[1].split("\\{");
        String buttonsStr = secondSep[0].substring(1, secondSep[0].length() - 1);
        String joltageReqsStr = secondSep[1].substring(0, secondSep[1].length() - 1);

        handleIndicators(indicatorStr);
        handleButtons(buttonsStr);
        handleJoltage(joltageReqsStr);
    }

    private static void handleIndicators(String indicatorStr) {
        boolean[] indicator = new boolean[indicatorStr.length()];
        for (int i = 0; i < indicatorStr.length(); i++)
            if (indicatorStr.charAt(i) == '#')
                indicator[i] = true;

        indicators.add(indicator);
    }

    private static void handleButtons(String buttonsStr) {
        List<List<Integer>> lineButtons = new ArrayList<>();
        String[] individualButtonPresses = buttonsStr.split(" ");

        for (String individualButtonPress : individualButtonPresses) {
            String individualTrimmed = individualButtonPress.substring(1,
                    individualButtonPress.length() - 1);
            String[] buttonLightsStr = individualTrimmed.split(",");
            List<Integer> buttonLights = new ArrayList<>();

            for (String buttonLight : buttonLightsStr)
                buttonLights.add(Integer.parseInt(buttonLight));

            lineButtons.add(buttonLights);
        }

        buttons.add(lineButtons);
    }

    private static void handleJoltage(String joltageReqsStr) {
        String[] joltageNums = joltageReqsStr.split(",");
        List<Integer> lineJoltage = new ArrayList<>();

        for (String joltage : joltageNums)
            lineJoltage.add(Integer.parseInt(joltage));

        joltageReqs.add(lineJoltage);
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