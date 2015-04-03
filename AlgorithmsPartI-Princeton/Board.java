
public class Board {
    private int N;
    private int[][] blocks;
    //private int[] oneDTiles;
    
    public Board(int[][] blocks) {
        N = blocks.length;
        //this.oneDTiles = new int[N * N];
        this.blocks = new int[N][N];
        
        //change 2D board to 1D board
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                this.blocks[i][j] = blocks[i][j];
                //this.oneDTiles[k] = blocks[i][j];
            }
    }          // construct a board from an N-by-N array of blocks
                                           // (where blocks[i][j] = block in row i, column j)
    public int dimension() {
        return N;
    }                // board dimension N
    
    public int hamming() {
        int hamming = 0;
//        for (int i = 0; i < N * N - 1; i++) {
//            if (oneDTiles[i] != i + 1)
//                hamming++;
//        }
        
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (this.blocks[i][j] != 0 && this.blocks[i][j] != i * N + j + 1)
                    hamming++;
            
        return hamming;
    }                // number of blocks out of place
    
    public int manhattan() {
        int manhattan = 0;
        //int currentx = 0, currenty = 0;
        int goalx = 0, goaly = 0;
        int distance = 0;
//        for (int i = 0; i < N * N; i++) {
//            if (oneDTiles[i] != 0 && oneDTiles[i] != i + 1) {
//                currentx = i / N;
//                currenty = i % N;
//                goalx = (oneDTiles[i] - 1) / N;
//                goaly = (oneDTiles[i] - 1) % N;
//                distance = Math.abs(goalx - currentx) + Math.abs(goaly - currenty);
//                manhattan += distance;
//             }
//        }
        
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (this.blocks[i][j] != 0 && this.blocks[i][j] != i * N + j + 1) {
                    goalx = (blocks[i][j] - 1) / N;
                    goaly = (blocks[i][j] - 1) % N;
                    distance = Math.abs(goalx - i) + Math.abs(goaly - j);
                    manhattan += distance;
                }
        
        return manhattan;
    }                // sum of Manhattan distances between blocks and goal
    
    public boolean isGoal() {
        return hamming() == 0 || manhattan() == 0;
    }                // is this board the goal board?
   
    public Board twin() {
        Board twin = new Board(blocks);
        if (twin.blocks[0][0] != 0 && twin.blocks[0][1] != 0) {
            move(twin.blocks, 0, 0, 0, 1);
        } else {
            move(twin.blocks, 1, 0, 1, 1);
        }
        
        return twin;
    }                // a board that is obtained by exchanging two adjacent blocks in the same row
    
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.blocks == that.blocks) return true;
        if (N != that.blocks.length) return false;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (this.blocks[i][j] != that.blocks[i][j]) return false;
        return true;
    }                // does this board equal y?
    
    private void move(int[][] blocks, int fromx, int fromy, int tox, int toy) {
        int temp = blocks[fromx][fromy];
        blocks[fromx][fromy] = blocks[tox][toy];
        blocks[tox][toy] = temp;
        
//        int oneDFrom = fromx * N + fromy;
//        int oneDTo = tox * N + toy;
//        int oneDTemp = oneDTiles[oneDFrom];
//        oneDTiles[oneDFrom] = oneDTiles[oneDTo];
//        oneDTiles[oneDTo] = oneDTemp;
    }
    
    public Iterable<Board> neighbors() {
        Queue<Board> q = new Queue<Board>();
        int zerox = 0, zeroy = 0;
        outer: for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    if (blocks[i][j] == 0) {
                        zerox = i;
                        zeroy = j;
                        break outer;
                    }
        
        if (zerox != 0) {
            Board upneigh = new Board(blocks);
            move(upneigh.blocks, zerox - 1, zeroy, zerox, zeroy);
            q.enqueue(upneigh);
        }
        
        if (zerox != N - 1) {
            Board downneigh = new Board(blocks);
            move(downneigh.blocks, zerox + 1, zeroy, zerox, zeroy);
            q.enqueue(downneigh);
        }
        
        if (zeroy != 0) {
            Board leftneigh = new Board(blocks);
            move(leftneigh.blocks, zerox, zeroy - 1, zerox, zeroy);
            q.enqueue(leftneigh);
        }
        
        if (zeroy != N - 1) {
            Board rightneigh = new Board(blocks);
            move(rightneigh.blocks, zerox, zeroy + 1, zerox, zeroy);
            q.enqueue(rightneigh);
        }
        
        return q;
    }                // all neighboring boards
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }              // string representation of this board (in the output format specified below)
    
    //public static void main(String[] args) // unit tests (not graded)
   
}
