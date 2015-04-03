import java.util.Comparator;


public class Solver {
    private int moves = -1;
    private Stack<Board> steps;
    
    public Solver(Board initial) {
        MinPQ<SearchNode> pqInit = new MinPQ<SearchNode>(new SearchNodeComparator());
        MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>(new SearchNodeComparator());
        pqInit.insert(new SearchNode(initial, null, 0));
        pqTwin.insert(new SearchNode(initial.twin(), null, 0));
        SearchNode initTmp, twinTmp;
        boolean isSolvable = true;
        int initMoves = 0, twinMoves = 0;
        while (true) {
            initTmp = pqInit.delMin();
            if (initTmp.board.isGoal()) {
                break;
            } else {
                initMoves = initTmp.moves + 1;
                for (Board b: initTmp.board.neighbors()) {
                    if (initTmp.pre == null) {
                        pqInit.insert(new SearchNode(b, initTmp, initMoves));
                    } else {
                        if (!b.equals(initTmp.pre.board)) {
                            pqInit.insert(new SearchNode(b, initTmp, initMoves));
                        }
                    }
                }
            }
            twinTmp = pqTwin.delMin();
            if (twinTmp.board.isGoal()) {
                isSolvable = false;
                break;
            } else {
                twinMoves = twinTmp.moves + 1;
                for (Board b: twinTmp.board.neighbors()) {
                    if (twinTmp.pre == null) {
                        pqTwin.insert(new SearchNode(b, twinTmp, twinMoves));
                    } else {
                        if (!b.equals(twinTmp.pre.board)) {
                            pqTwin.insert(new SearchNode(b, twinTmp, twinMoves));
                        }
                    }
                }
            }
        }
        if (isSolvable) {
            steps = new Stack<Board>();
            SearchNode cur = initTmp;
            int ms = -1;
            while (cur != null) {
                ms++;
                steps.push(cur.board);
                cur = cur.pre;
            }
            moves = ms;
        }
    }          // find a solution to the initial board (using the A* algorithm)
    
    public boolean isSolvable() {
        return moves != -1;
    }          // is the initial board solvable?
    
    public int moves() {
        return moves;
    }          // min number of moves to solve initial board; -1 if unsolvable
    
    public Iterable<Board> solution() {
        return steps;
    }          // sequence of boards in a shortest solution; null if unsolvable
    
    private class SearchNodeComparator implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            if (a.priority < b.priority) return -1;
            else if (a.priority == b.priority) return 0;
            else return 1;
        }
    }
    
    private class SearchNode {
        private Board board;
        private SearchNode pre;
        private int moves;
        private int priority;
        
        public SearchNode(Board board, SearchNode pre, int moves) {
            this.board = board;
            this.pre = pre;
            this.moves = moves;
            this.priority = board.manhattan() + moves;
        }
        
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SearchNode)) return false;
            
            SearchNode that = (SearchNode) o;
            if (board != null ? !board.equals(that.board) : (that.board != null))
                return false;
            if (moves != that.moves) return false;
            
            return true;
        }
    }
    
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
