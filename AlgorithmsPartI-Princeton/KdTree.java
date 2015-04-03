public class KdTree {
    private Node root;
    private int size;
    
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean vertical;
        
        public Node(Point2D p, RectHV rect, Node lb, Node rt, boolean vertical) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
            this.vertical = vertical;
        }
    }
   
    public KdTree() {
        root = null;
        size = 0;
    }                               
    
    public boolean isEmpty() {
       return size() == 0;
    }
    
    public int size() {
        return this.size;
    }
    
    public void insert(Point2D p) {
        root = insert(root, p, true, 0, 1, 0, 1);
    }
    
    private Node insert(Node node, Point2D p, boolean v, double xmin, double xmax, double ymin, double ymax) {
        if (node == null) {
            size++;
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax), null, null, v);
        }
        
        if (node.p.x() == p.x() && node.p.y() == p.y()) return node;
        
        if (node.vertical) {
            if (p.x() < node.p.x())
                node.lb = insert(node.lb, p, !node.vertical, node.rect.xmin(), node.p.x(), node.rect.ymin(), node.rect.ymax());
            else
                node.rt = insert(node.rt, p, !node.vertical, node.p.x(), node.rect.xmax(), node.rect.ymin(), node.rect.ymax());
        } else {
            if (p.y() < node.p.y())
                node.lb = insert(node.lb, p, !node.vertical, node.rect.xmin(), node.rect.xmax(), node.rect.ymin(), node.p.y());
            else
                node.rt = insert(node.rt, p, !node.vertical, node.rect.xmin(), node.rect.xmax(), node.p.y(), node.rect.ymax());
        }
        
        return node;
    }
    
    public boolean contains(Point2D p) {
        return contains(root, p.x(), p.y());
    }
    
    private boolean contains(Node node, double x, double y) {
        if (node == null) return false;
        if (node.p.x() == x && node.p.y() == y) return true;
        
        if (node.vertical && x < node.p.x() || !node.vertical && y < node.p.y())
            return contains(node.lb, x, y);
        else
            return contains(node.rt, x, y);
    }
    
    public void draw() {
        draw(root);
    }
    
    private void draw(Node node) {
        if (node == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        node.p.draw();
        
        if (node.vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }
        
        draw(node.lb);
        draw(node.rt);
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        Stack<Point2D> s = new Stack<Point2D>();
        range(root, rect, s);
        return s;
    }
    
    private void range(Node node, RectHV rect, Stack<Point2D> stack) {
        if (node == null) return;
        if (!node.rect.intersects(rect)) return;
        
        if (rect.contains(node.p))
            stack.push(node.p);
        
        range(node.lb, rect, stack);
        range(node.rt, rect, stack);
        
    }
    
    public Point2D nearest(Point2D p) {
        return nearest(root, p, null);
    }            
    
    private Point2D nearest(Node node, Point2D queryp, Point2D candidate) {
        if (node == null) return candidate;
        if (candidate == null)
            candidate = node.p;
        
        double closestDis = candidate.distanceSquaredTo(queryp);
        double currentDis = node.p.distanceSquaredTo(queryp);
        
        if (closestDis > node.rect.distanceSquaredTo(queryp)) {
            if (closestDis > currentDis)
                candidate = node.p;
            if (node.lb != null && node.rt != null && node.lb.rect.distanceSquaredTo(queryp) > node.rt.rect.distanceSquaredTo(queryp)) {
                candidate = nearest(node.rt, queryp, candidate);
                candidate = nearest(node.lb, queryp, candidate);
            } else {
                candidate = nearest(node.lb, queryp, candidate);
                candidate = nearest(node.rt, queryp, candidate);
            }
        }
        return candidate;
    }     
}
