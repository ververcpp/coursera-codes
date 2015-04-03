
public class PointSET {
    private SET<Point2D> pset;
    
    public PointSET() {
        pset = new SET<Point2D>();
    }                              // construct an empty set of points 
    public boolean isEmpty() {
        return pset.isEmpty();
    }                     // is the set empty? 
    public int size() {
        return pset.size();
    }                        // number of points in the set 
    public void insert(Point2D p) {
        if (!pset.contains(p))
            pset.add(p);
    }             // add the point to the set (if it is not already in the set)
    public boolean contains(Point2D p) {
        return pset.contains(p);
    }           // does the set contain point p? 
    public void draw() {
        for (Point2D p : pset) {
            p.draw();
        }
    }                // draw all points to standard draw 
    public Iterable<Point2D> range(RectHV rect) {
        SET<Point2D> phit = new SET<Point2D>();
        for (Point2D p : pset) {
            if (rect.contains(p)) {
                phit.add(p);
            }
        }
        
        return phit;
    }            // all points that are inside the rectangle 
    public Point2D nearest(Point2D p) {
        Point2D pnear = null;
        double mindis = 1.0;
        for (Point2D i : pset) {
            double idis = i.distanceTo(p);
            if (idis <= mindis) {
                mindis = idis;
                pnear = i;
            }
        }
        return pnear;
    }
 
}
