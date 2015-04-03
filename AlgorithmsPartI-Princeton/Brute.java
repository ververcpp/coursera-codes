
public class Brute {
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
        
        for (int p = 0; p + 3 < N; p++)
            for (int q = p+1; q + 2 < N; q++)
                for (int r = q+1; r + 1 < N; r++)
                    for (int s = r+1; s < N; s++) {
//                        StdOut.printf(" q slope: %f\n r slope:%f\n s slope:%f\n",
//                                        points[q].slopeTo(points[p]),
//                                        points[r].slopeTo(points[p]),
//                                        points[s].slopeTo(points[p]));
                        if (points[p].slopeTo(points[q]) 
                               == points[p].slopeTo(points[r])
                               && points[p].slopeTo(points[r]) 
                               == points[p].slopeTo(points[s])) {
                            StdOut.printf("%s -> %s -> %s -> %s\n",
                                    points[p].toString(), points[q].toString(),
                                    points[r].toString(), points[s].toString());
                            points[p].drawTo(points[s]);
                        }
                    }
        // display to screen all at once
        StdDraw.show(0);

        // reset the pen radius
        StdDraw.setPenRadius();
    }
}
