package bearmaps.utils.ps;
import java.util.*;

public class NaivePointSet implements PointSet {

    List<Point> pointset;

    public NaivePointSet(List<Point> points) {
        pointset = new ArrayList<>();
        for (Point p : points) {
            pointset.add(p);
        }
    }

    @Override
    public Point nearest(double x, double y) {
        Point comparePoint = new Point(x, y);
        Point nearest = pointset.get(0);
        for (Point p : pointset) {
            if (Point.distance(comparePoint, p) < Point.distance(comparePoint, nearest)) {
                nearest = p;
            }
        }
        return nearest;
    }
}
