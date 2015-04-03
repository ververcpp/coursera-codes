
public class Subset {
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        int k = Integer.parseInt(args[0]);
        int n = 0;
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            n++;
            if (n > k) {
                int flag = StdRandom.uniform(1, n+1);
                if (flag > k)  continue;
                q.dequeue();
            }
            q.enqueue(item);
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(q.dequeue());
        }
    }
}
