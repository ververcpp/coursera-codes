/*************************************************************************
 *  Compilation:  javac Percolation.java
 *  Execution:    java Percolation input.txt
 *  Dependencies: WeightedQuickUnionUf.java In.java StdOut.java 
 *  
 *  Model a N-by-N grid percolation system
 *
 *************************************************************************/


public class Percolation {
    
    private WeightedQuickUnionUF uf;                           
    private boolean[] openstatus;  
    private boolean[] topstatus;
    private boolean[] bottomstatus;
    //sites status:from right to left first digit   --- blocked(0);open(1)
    //                                second digit  --- connected to top(1)
    //                                third digit   --- connected to bottom(1)
    
    private int N;                              //grid size: N-by-N
    private boolean isPercolates;

    /* 
     * create N-by-N grid, with all sites blocked;
     * @throws IllegalArgumentException if the grid size N less than 1
     * */
    public Percolation(int N) {
        if (N <= 0)
            throw new IllegalArgumentException("The input N cannot less than 1");
        
        //define instance variables
        uf = new WeightedQuickUnionUF(N * N);   
        this.N = N;
        openstatus = new boolean[N * N];
        topstatus = new boolean[N * N];
        bottomstatus = new boolean[N * N];
        isPercolates = false;
        
        //initialize every status of sites to blocked        
        for (int i = 0; i < N * N; i++) {
            openstatus[i] = false;
        }
        
        //make first line and last line connected to top or bottom respectively
        for (int i = 0; i < N; i++) {
            topstatus[i] = true;
        }
        
        for (int i = N * (N - 1); i < N * N; i++) {
            bottomstatus[i] = true;
        }
    }
    
    
    /*
     * map 2-dimensional (row, column) pair to a 1-dimensional
     * union find object index  
     * @return the 1-dimensional index integer
     * */
    private int xyTo1D(int i, int j) {
        return ((i-1) * N + j - 1);
    }
    
    /*
     * validate the input indices if out of bounds
     * @throw IndexOutOfBoundsException
     * */
    private void validate(int i, int j) {
        if (i <= 0 || i > N)
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (j <= 0 || j > N)
            throw new IndexOutOfBoundsException("column index j out of bounds");
    }
    
    
//    /*
//     * check first value of site status array[index] 
//     * if value is false, @return true
//     * */    
//    private boolean isBlocked(int index) {
//        return openstatus[!]
//    }
    
    /*
     * check second value of site status array[index]
     * if value is true, @return true
     * */
    private boolean isConnectedToTop(int index) {
        return topstatus[index];
    }
    
    /*
     * check third value of site status array[index]
     * if value is true, @return true
     * */
    private boolean isConnectedToBottom(int index) {
        return bottomstatus[index];
    }
    
    
    /*
     * link two sites
     * */
    private void connect(int neighbori, int neightborj, 
                         int current, boolean[] originrootstatus) {
        if (neighbori >= 1 && neighbori <= N 
                && neightborj >= 1 && neightborj <= N 
                && isOpen(neighbori, neightborj)) {
            int neighbor = xyTo1D(neighbori, neightborj);
            int neighborroot = uf.find(neighbor);            
            uf.union(neighbor, current);
            boolean[] nrstatus = new boolean[3];
            nrstatus[0] = openstatus[neighborroot];
            nrstatus[1] = topstatus[neighborroot];
            nrstatus[2] = bottomstatus[neighborroot];
            for (int i = 0; i < 3; i++) {
                originrootstatus[i] |= nrstatus[i];
            }
        }
    }
    
    
    /* 
     * open site (row i, column j) if it is not open already
     * */
    public void open(int i, int j) {
        validate(i, j);
        int current = xyTo1D(i, j);
       
        if (!isOpen(i, j)) {
            openstatus[current] = true;
        }
        
        boolean[] rootstatus = new boolean[3];
        rootstatus[0] = openstatus[current];
        rootstatus[1] = topstatus[current];
        rootstatus[2] = bottomstatus[current];
        
        // link the site to its open neighbors
        connect(i-1, j, current, rootstatus);
        connect(i+1, j, current, rootstatus);
        connect(i, j-1, current, rootstatus);
        connect(i, j+1, current, rootstatus);
        
        
        int currentroot = uf.find(current);
        if (currentroot != current) {
            openstatus[currentroot] |= rootstatus[0];
            topstatus[currentroot] |= rootstatus[1];
            bottomstatus[currentroot] |= rootstatus[2];
        }
        
        if (isConnectedToTop(currentroot) && isConnectedToBottom(currentroot))
            isPercolates = true;
    }
    
    /*
     * is site (row i, column j) open?
     * @return true if open, false if not
     * */
    public boolean isOpen(int i, int j) {
        validate(i, j);
        int index = xyTo1D(i, j);
        return openstatus[index];
    }
    
    /* 
     * is site (row i, column j) full? 
     * */
    public boolean isFull(int i, int j) {
        validate(i, j);
        int current = xyTo1D(i, j);
        int currentroot = uf.find(current);
        if (isOpen(i, j) && isConnectedToTop(currentroot))
            return true;
        
        return false;
    }
    
    /* 
     * does the system percolate?
     * */
    public boolean percolates() {
        return isPercolates;
    }
    
    /*
     * test
     * */
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        boolean percolate = false;
        
        Percolation perc = new Percolation(N);
        StdOut.println("input:" + args[0]);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
            if (perc.percolates()) {
                percolate = true;
                StdOut.println("Per!");
                break;
            }
        }
        if (!percolate)
            StdOut.println("No per!");
    }
}
