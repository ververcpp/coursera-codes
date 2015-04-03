/*************************************************************************
 *  Compilation:  javac PercolationStats.java
 *  Execution:    java PercolationStats 200 100
 *  Dependencies: Percolation.java StdRandom.java StdOut.java 
 *  
 *  Model a percolation system
 *
 *************************************************************************/


public class PercolationStats {
    private int times;                      // experiment times
    private double[] results;    // store the result of each experiment
    
    /*
     * Constructor
     * perform T independent experiments on an N-by-N grid
     * */
    public PercolationStats(int N, int T) {
        validateArguments(N);            
        validateArguments(T);           
        results = new double[T];
        times = T;
        
        //initialize the results array
        for (int i = 0; i < T; i++) {   
            results[i] = 0.0;
        }
        
        for (int i = 0; i < T; i++) {
            Percolation perc = new Percolation(N);
//            boolean[] isEmptySiteLine = new boolean[N];
//            int num = 0;
            while (true) {
                int posX, posY;
                do {
                    posX = StdRandom.uniform(1, N+1);
                    posY = StdRandom.uniform(1, N+1);
                } while (perc.isOpen(posX, posY));
                perc.open(posX, posY);
                results[i]++;
//                if (!isEmptySiteLine[posX - 1]) {
//                    isEmptySiteLine[posX - 1] = true;
//                    num++;
//                }
//                if (num == N) {
                if (perc.percolates())
                    break;
//                }
            }
            results[i] = results[i] / (N * N);
        }
        
    }
    
    
    /*
     *  check the input arguments not less than 1 
     * */
    private void validateArguments(int arg) {
        if (arg <= 0) 
            throw new IllegalArgumentException("Arguments must be lagger than 0");
    }
    
    
    /*
     * sample mean of percolation threshold
     * @return the result of mean
     * */
    public double mean() {
        double total = 0.0;
        for (int i = 0; i < times; i++)
            total += results[i];
        return total / times;
    }
    
    
    /*
     * sample standard deviation of percolation threshold
     * @return the result of standard deviation
     * */
    public double stddev() {
        if (times == 1)
            return Double.NaN;
        double totalDev = 0.0;
        double mean = mean();
        for (int i = 0; i < times; i++) {
            totalDev += (results[i] - mean) * (results[i] - mean);
        }
        return Math.sqrt(totalDev / (times - 1));
    }
    
    
    /*
     * @return low endpoint of 95% confidence interval
     * */
    public double confidenceLo() {
        double mean = mean();
        double stddev = stddev();
        return (mean - (1.96 * stddev / Math.sqrt(times)));
    }
    
    
    /* 
     * @return high endpoint of 95% confidence interval
     * */
    public double confidenceHi() {
        double mean = mean();
        double stddev = stddev();
        return (mean + (1.96 * stddev / Math.sqrt(times)));
    }
    
    
    /*
     * accept two arguments from standard input and output 
     * to StdOut the experiment mean, standard deviation and 
     * low and high endpoint of 95% confidence interval respectively.
     * */
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percS = new PercolationStats(N, T);
        
        StdOut.printf("mean = %f\n", percS.mean());
        StdOut.printf("stddev = %f\n", percS.stddev());
        StdOut.printf("95%% confidence interval = %f, %f\n", 
                percS.confidenceLo(), percS.confidenceHi());
                
    }
}
