package bearmaps.utils.ps;
import java.util.*;

public class KDTree implements PointSet{
    private Node root;

    private class Node {
        private Point p;
        private boolean split_by_x;
        private Node left;
        private Node right;
        public Node(Point p, boolean split_by_x) {
            this.p = p;
            this.split_by_x = split_by_x;
            this.left = null;
            this.right = null;
        }
    }

    public KDTree(List<Point> points) {
        for (Point p: points) {
            if (root == null) {
                root = new Node(p, true);
            } else {
                insertHelper(root, p);
            }
        }
    }

    public void insertHelper(Node n, Point p) {
        if (n.split_by_x) {
            if (p.getX() < n.p.getX()) {
                if (n.left == null) {
                    n.left = new Node(p, !n.split_by_x);
                    return;
                }
                insertHelper(n.left, p);
            } else {
                if (n.right == null) {
                    n.right = new Node(p, !n.split_by_x);
                    return;
                }
                insertHelper(n.right, p);
            }
        } else {
            if (p.getY() < n.p.getY()) {
                if (n.left == null) {
                    n.left = new Node(p, !n.split_by_x);
                    return;
                }
                insertHelper(n.left, p);
            } else {
                if (n.right == null) {
                    n.right = new Node(p, !n.split_by_x);
                    return;
                }
                insertHelper(n.right, p);
            }
        }
    }


    @Override
    public Point nearest(double x, double y) {
        Point goal = new Point(x, y);
        return nearestHelper(root, goal, root).p;
    }

    public Node nearestHelper(Node n, Point goal, Node best) {
        if (n == null) {
            return best;
        }
        if (Point.distance(n.p, goal) < Point.distance(best.p, goal)) {
            best = n;
        }
        Node good;
        Node bad;
        if (n.split_by_x) {
            if (goal.getX() < n.p.getX()) {
                good = n.left;
                bad = n.right;
            } else {
                good = n.right;
                bad = n.left;
            }
        } else {
            if (goal.getY() < n.p.getY()) {
                good = n.left;
                bad = n.right;
            } else {
                good = n.right;
                bad = n.left;
            }
        }
        best = nearestHelper(good, goal, best);
        if (n.split_by_x) {
            if (Point.distance(new Point(n.p.getX(), goal.getY()), goal) < Point.distance(best.p, goal)) {
                best = nearestHelper(bad, goal, best);
            }
        } else {
            if (Point.distance(new Point(goal.getX(), n.p.getY()), goal) < Point.distance(best.p, goal)) {
                best = nearestHelper(bad, goal, best);
            }
        }
        return best;
    }
}
