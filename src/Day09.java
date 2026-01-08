import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day09 {

    private static final int INPUT_SIZE = 496;
    private static final List<Point2D> points = new ArrayList<>(INPUT_SIZE);

    public static void main(String[] args) {
        readFile(Day09.class.getSimpleName().toLowerCase() + ".txt");
        System.out.println("Part 1 result: " + solve1());
        System.out.println("Part 2 result: " + solve2());
    }

    private static long solve1() {
        final int len = points.size();
        long maxArea = 0;

        for (int i = 0; i < len; i++)
            for (int j = i + 1; j < len; j++)
                maxArea = Math.max(maxArea, calculateRectangleArea(points.get(i).x,
                        points.get(i).y, points.get(j).x, points.get(j).y));

        return maxArea;
    }

    private static long calculateRectangleArea(int x1, int y1, int x2, int y2) {
        return (long) (Math.abs(x1 - x2) + 1) * (Math.abs(y1 - y2) + 1);
    }

    private static long solve2() { // TODO: not working
        final int len = points.size();
        final int rectangleNum = ((INPUT_SIZE - 1) * INPUT_SIZE) / 2;
        List<Rectangle> rectangles = new ArrayList<>(rectangleNum);

        // calculate all rectangles' areas
        for (int i = 0; i < len; i++)
            for (int j = i + 1; j < len; j++)
                rectangles.add(new Rectangle(points.get(i), points.get(j),
                        calculateRectangleArea(points.get(i).x, points.get(i).y,
                                points.get(j).x, points.get(j).y)));

        // sort rectangles by decreasing area
        rectangles.sort(Comparator.comparingLong(Rectangle::area).reversed());
        int count = 0;

        for (int i = 0; i < rectangleNum; i++) {
            Rectangle rec1 = rectangles.get(i);
            System.out.println("\ncount: " + count + ", " + rec1);
            count++;
            boolean isValid = true;

            for (int j = 0; j < rectangleNum; j++) {
                if (i == j)
                    continue;

                Rectangle rec2 = rectangles.get(j);
                if (isThereInnerIntersection(rec1, rec2)) {
                    System.out.println(rec2);
                    isValid = false;
                    break;
                }
            }

            if (isValid)
                return rec1.area;
        }

        return -1;
    }

    private static boolean isThereInnerIntersection(Rectangle rec1, Rectangle rec2) {
        // rec1 is at the right
        if (rec1.a.x >= rec2.a.x && rec1.b.x >= rec2.a.x &&
                rec1.a.x >= rec2.b.x && rec1.b.x >= rec2.b.x)
            return false;

        // rec1 is at the left
        if (rec1.a.x <= rec2.a.x && rec1.b.x <= rec2.a.x &&
                rec1.a.x <= rec2.b.x && rec1.b.x <= rec2.b.x)
            return false;

        // rec1 is above
        if (rec1.a.y >= rec2.a.y && rec1.b.y >= rec2.a.y &&
                rec1.a.y >= rec2.b.y && rec1.b.y >= rec2.b.y)
            return false;

        // rec1 is below
        if (rec1.a.y <= rec2.a.y && rec1.b.y <= rec2.a.y &&
                rec1.a.y <= rec2.b.y && rec1.b.y <= rec2.b.y)
            return false;

        return true;
    }

    /*private static boolean isPointInsideRectangle(Rectangle rec, Point2D point) {
        return !arePointsTheSame(rec.a, point) && !arePointsTheSame(rec.b, point) &&
                (point.x <= rec.a.x && point.x >= rec.b.x ||
                        point.x >= rec.a.x && point.x <= rec.b.x) &&
                (point.y <= rec.a.y && point.y >= rec.b.y ||
                        point.y >= rec.a.y && point.y <= rec.b.y);
    }*/

    private static boolean arePointsTheSame(Point2D a, Point2D b) {
        return a.x == b.x && a.y == b.y;
    }

    private record Point2D(int x, int y) {}
    private record Rectangle(Point2D a, Point2D b, long area) {}

    private static void treatInputLine(String line) {
        String[] numsSeparated = line.split(",");
        points.add(new Point2D(Integer.parseInt(numsSeparated[0]),
                Integer.parseInt(numsSeparated[1])));
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