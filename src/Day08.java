import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day08 {

    private static final int INPUT_SIZE = 1000;

    private static final List<Box> boxes = new ArrayList<>(INPUT_SIZE);
    private static int inputIdx = 0;

    public static void main(String[] args) {
        readFile(Day08.class.getSimpleName().toLowerCase() + ".txt");
        System.out.println("Part 1 result: " + solve1());
        System.out.println("Part 2 result: " + solve2());
    }

    private static double distance2Points(Point3D p, Point3D q) {
        return Math.sqrt(Math.pow(p.x - q.x, 2) + Math.pow(p.y - q.y, 2) + Math.pow(p.z - q.z, 2));
    }

    private static int solve1() {
        /* calculate the distances between all points, while maintaining only
           the INPUT_SIZE smallest distances */
        Comparator<DistanceBoxes> comparator = Comparator.comparingDouble(DistanceBoxes::distance).reversed();
        PriorityQueue<DistanceBoxes> smallestDistancesPq = getSmallestDistances(comparator);

        // create list with distances in ascending order
        List<DistanceBoxes> smallestDistances = new ArrayList<>(smallestDistancesPq);
        smallestDistances.sort(comparator.reversed());

        // prepare disjoint set union
        int[] parent = new int[INPUT_SIZE];
        int[] size = new int[INPUT_SIZE];
        for (int i = 0; i < INPUT_SIZE; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        connectCircuits(smallestDistances, parent, size);
        return multiplyKLargestCircuitSizes(parent, size, 3);
    }

    private static PriorityQueue<DistanceBoxes> getSmallestDistances(Comparator<DistanceBoxes> comparator) {
        PriorityQueue<DistanceBoxes> smallestDistancesPq =
                new PriorityQueue<>(INPUT_SIZE, comparator);

        for (int curr = 0; curr < INPUT_SIZE; curr++) {
            for (int other = curr + 1; other < INPUT_SIZE; other++) {
                double distance = distance2Points(boxes.get(curr).point, boxes.get(other).point);
                DistanceBoxes db = new DistanceBoxes(curr, other, distance);

                if (smallestDistancesPq.size() < INPUT_SIZE)
                    smallestDistancesPq.add(db);
                else if (distance < smallestDistancesPq.peek().distance) {
                    smallestDistancesPq.poll();
                    smallestDistancesPq.add(db);
                }
            }
        }

        return smallestDistancesPq;
    }

    private static void connectCircuits(List<DistanceBoxes> smallestDistances, int[] parent, int[] size) {
        for (DistanceBoxes db : smallestDistances) {
            int a = db.box1Idx;
            int b = db.box2Idx;
            int ra = find(parent, a);
            int rb = find(parent, b);

            // if ra == rb do nothing (connection doesn't change components)
            if (ra != rb) {
                if (size[ra] < size[rb]) {
                    parent[ra] = rb;
                    size[rb] += size[ra];
                } else {
                    parent[rb] = ra;
                    size[ra] += size[rb];
                }
            }
        }
    }

    private static int find(int[] parent, int x) {
        int root = x;

        while (parent[root] != root)
            root = parent[root];

        while (x != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }

        return root;
    }

    private static int multiplyKLargestCircuitSizes(int[] parent, int[] size, int k) {
        // collect component sizes (root nodes carry sizes)
        List<Integer> circuitSizes = new ArrayList<>();
        for (int i = 0; i < INPUT_SIZE; i++)
            if (parent[i] == i && size[i] > 0)
                circuitSizes.add(size[i]);

        // get 3 largest circuits' sizes and multiply them
        circuitSizes.sort(Comparator.reverseOrder());
        int result = 1;

        for (int i = 0; i < k; i++)
            result *= circuitSizes.get(i);

        return result;
    }

    private static int solve2() {
        PriorityQueue<DistanceBoxes> distancesOrdered = getAllDistances();
        int[] parent = new int[INPUT_SIZE];
        int[] size = new int[INPUT_SIZE];

        for (int i = 0; i < INPUT_SIZE; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        return connectUntil1Circuit(distancesOrdered, parent, size);
    }

    private static PriorityQueue<DistanceBoxes> getAllDistances() {
        PriorityQueue<DistanceBoxes> distancesOrdered =
                new PriorityQueue<>(Comparator.comparingDouble(DistanceBoxes::distance));

        for (int curr = 0; curr < INPUT_SIZE; curr++) {
            for (int other = curr + 1; other < INPUT_SIZE; other++) {
                double distance = distance2Points(boxes.get(curr).point, boxes.get(other).point);
                distancesOrdered.add(new DistanceBoxes(curr, other, distance));
            }
        }

        return distancesOrdered;
    }

    private static int connectUntil1Circuit(PriorityQueue<DistanceBoxes> distancesOrdered, int[] parent, int[] size) {
        while (!distancesOrdered.isEmpty()) {
            DistanceBoxes db = distancesOrdered.poll();
            int idx1 = db.box1Idx;
            int idx2 = db.box2Idx;
            int ra = find(parent, idx1);
            int rb = find(parent, idx2);

            // if ra == rb do nothing (connection doesn't change components)
            if (ra != rb) {
                if (size[ra] < size[rb]) {
                    parent[ra] = rb;
                    size[rb] += size[ra];
                } else {
                    parent[rb] = ra;
                    size[ra] += size[rb];
                }

                if (size[ra] == INPUT_SIZE || size[rb] == INPUT_SIZE)
                    return boxes.get(idx1).point.x * boxes.get(idx2).point.x;
            }
        }

        throw new RuntimeException("Could not connect all boxes into one circuit.");
    }

    private record Point3D(int x, int y, int z) {}

    private static class Box {
        public final int idx;
        public int circuit;
        public final Point3D point;

        public Box(int idx, int circuit, Point3D point) {
            this.idx = idx;
            this.circuit = circuit;
            this.point = point;
        }
    }

    private record DistanceBoxes(int box1Idx, int box2Idx, double distance) {}

    private static void treatInputLine(String line) {
        String[] strDimensions = line.split(",");
        int[] dimensions = new int[3];
        for (int i = 0; i < 3; i++)
            dimensions[i] = Integer.parseInt(strDimensions[i]);

        boxes.add(new Box(inputIdx, inputIdx++, new Point3D(dimensions[0], dimensions[1], dimensions[2])));
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