package bearmaps.utils.ps;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KDTreeTest {
    private static Random r = new Random(100);

    @Test
    public void test() {
        Point p1 = new Point(1.1, 2.2);
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        System.out.println(ret.getX()); // evaluates to 3.3
        System.out.println(ret.getY()); // evaluates to 4.4
        KDTree kd = new KDTree(List.of(p1, p2, p3));
        Point near = kd.nearest(3.0, 4.0);
        assertTrue(ret.equals(near));
    }

    public Point randomPoint() {
        double x = r.nextDouble();
        double y = r.nextDouble();
        return new Point(x, y);
    }

    public List<Point> randomPList(int N) {
        List<Point> plist = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            plist.add(randomPoint());
        }
        return plist;
    }

    @Test
    public void testNearest1() {
        List<Point> plist = randomPList(100000);
        List<Point> qlist = randomPList(1000);
        NaivePointSet nn = new NaivePointSet(plist);
        KDTree kd = new KDTree(plist);
        for (Point q : qlist) {
            Point KD = kd.nearest(q.getX(), q.getY());
            Point naive = nn.nearest(q.getX(), q.getY());
            assertEquals(naive, KD);
        }
    }

    @Test
    public void testNearest2() {
        Point p1 = new Point(2, 3);
        Point p2 = new Point(1, 5);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5);
        Point p5 = new Point(3, 3);
        Point p6 = new Point(4, 4);
        Point p7 = new Point(0, 5);

        KDTree kd = new KDTree(List.of(p1, p2, p3, p4, p5, p6, p7));
        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3, p4, p5, p6, p7));
        Point near = kd.nearest(0, 7);
        Point naive = nn.nearest(0, 7);
        assertTrue(near.equals(new Point(0, 5)));
        assertEquals(near, naive);
        System.out.println(naive.getX());
        System.out.println(naive.getY());
    }
}