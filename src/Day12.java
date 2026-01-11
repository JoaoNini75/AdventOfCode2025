import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day12 {

    private static final int SHAPE_NUM = 5;
    private static final int SHAPE_SIZE = 3;
    private static final int REGION_NUM = 1000;
    private static final int QUANTITIES_SIZE = 6;

    private static boolean readingShapes = true;
    private static int shapeIdx = 0, shapeRowIdx = 0;
    private static final List<Shape> shapes = new ArrayList<>(SHAPE_NUM);
    private static final List<Region> regions = new ArrayList<>(REGION_NUM);

    public static void main(String[] args) {
        readFile(Day12.class.getSimpleName().toLowerCase() + ".txt");
        System.out.println("Part 1 result: " + solve1());
        System.out.println("Part 2 result: " + solve2());
    }

    private static class Shape {
        public int size;
        public char[][] shape;

        public Shape(int size, char[][] shape) {
            this.size = size;
            this.shape = shape;
        }
    }

    private record Region(int width, int length, List<Integer> shapeQuantities) {}

    private static int solve1() {
        int regionFitPresents = 0;

        for (Region region : regions) {
            final int regionSize = region.width * region.length;
            final int shapeQuantsLen = region.shapeQuantities.size();
            int shapesTotalSize = 0;

            for (int shapeIdx = 0; shapeIdx < shapeQuantsLen; shapeIdx++) {
                int shapeQuant = region.shapeQuantities.get(shapeIdx);
                int shapeSize = shapes.get(shapeIdx).size;
                shapesTotalSize += shapeQuant * shapeSize;
            }

            if (regionSize >= shapesTotalSize)
                regionFitPresents++;
        }

        return regionFitPresents;
    }

    private static int solve2() {
        return 0;
    }

    private static void printInput() {

    }

    private static int countShapeLineSize(String shapeLine) {
        int size = 0;

        for (char c : shapeLine.toCharArray())
            if (c == '#')
                size++;

        return size;
    }

    private static void handleInputShape(String shapeStr) {
        if (shapeStr.contains(":"))
            return;

        if (shapeRowIdx == 0) {
            char[][] shape = new char[SHAPE_SIZE][SHAPE_SIZE];
            char[] shapeChars = shapeStr.toCharArray();
            int size = countShapeLineSize(shapeStr);
            shape[shapeRowIdx++] = shapeChars;
            shapes.add(new Shape(size, shape));
        } else {
            char[][] shape = shapes.get(shapeIdx).shape;
            char[] shapeChars = shapeStr.toCharArray();
            int size = countShapeLineSize(shapeStr);
            shapes.get(shapeIdx).size += size;
            shape[shapeRowIdx++] = shapeChars;
        }
    }

    private static void handleInputRegion(String regionStr) {
        String[] firstSep = regionStr.split("x");
        int width = Integer.parseInt(firstSep[0]);

        String[] secondSep = firstSep[1].split(": ");
        int length = Integer.parseInt(secondSep[0]);

        String[] quantities = secondSep[1].split(" ");
        List<Integer> shapeQuantities = new ArrayList<>(QUANTITIES_SIZE);

        for (String quantity : quantities)
            shapeQuantities.add(Integer.parseInt(quantity));

        regions.add(new Region(width, length, shapeQuantities));
    }

    private static void handleInputLine(String line) {
        if (readingShapes) {
            if (line.contains("x")) {
                readingShapes = false;
                handleInputLine(line);
            }

            if (line.isEmpty()) {
                shapeIdx++;
                shapeRowIdx = 0;
            } else
                handleInputShape(line);

        } else
            handleInputRegion(line);
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