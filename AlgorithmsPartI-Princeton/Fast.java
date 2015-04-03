import java.util.Arrays;


public class Fast {
    public static void main(String[] args) {
        
        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);
        StdDraw.setPenRadius(0.01);  // make the points a bit larger
        
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
            points[i].draw();
        }
        Quick3way.sort(points);
        fastMethod(points);
        StdDraw.show(0);
    }
    
    private static void fastMethod(Point[] points) {
        int N = points.length;
        for (int i = 0; i < N; i++) {
            Point origin = points[i];
            Point[] otherPoints = new Point[N-1];
            for (int j = 0; j < N; j++) {
                if (j < i) 
                    otherPoints[j] = points[j];
                else if (j > i)
                    otherPoints[j-1] = points[j];
            }
            Arrays.sort(otherPoints, origin.SLOPE_ORDER);
            
            int count = 0;
            int begin = 0;
            int end = 0;
            for (int j = 0; j < N - 1; j++) {
                if (j + 1 < N - 1 && origin.slopeTo(otherPoints[j]) == origin.slopeTo(otherPoints[j+1])) {
                    begin = j;
                    int k = 0;
                    for (k = j; k < N - 1; k++) {
                        if (k < N -1 && origin.slopeTo(otherPoints[j]) == origin.slopeTo(otherPoints[k]))
                            count++;
                        else break;
                    }
                    if (count >= 3 && otherPoints[begin].compareTo(origin) >= 0) {
                        end = k - 1;
                        StdOut.printf("%s", origin.toString());
                        for (int l = j; l <= end; l++)
                            StdOut.printf(" -> %s", otherPoints[l].toString());
                        StdOut.printf("\n");
                        origin.drawTo(otherPoints[end]);
                    }
                    j = j + count - 1;
                    count = 0;
                }                    
            }
        }
    }
}
