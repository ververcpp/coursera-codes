import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int N;
    private Item[] a;
    
    public RandomizedQueue() {
        N = 0;
        a = (Item[]) new Object[2];
    }
    
    public boolean isEmpty() {
        return N == 0;
    }
    
    public int size() {
        return N;
    }
    
    private void resize(int capacity) {
        assert capacity >= N;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }
    
    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException();
        if (N == a.length) resize(2*a.length);
        a[N++] = item;
    }
    
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        int removeIndex = StdRandom.uniform(N);
        Item item = a[removeIndex];
        a[removeIndex] = a[N-1];
        a[N-1] = null;
        N--;
        if (N > 0 && N == a.length / 4) resize(a.length/2);
        return item;
    }
    
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        int sampleIndex = StdRandom.uniform(N);
        return a[sampleIndex];
    }
    
    public Iterator<Item> iterator() {
        return new RandIterator();
    }
    
    private class RandIterator implements Iterator<Item> {
        private int size;
        private int[] randIndex;
        private int count;
        
        public RandIterator() {
            size = N;
            count = 0;
            randIndex = new int[N];
            
            for (int i = 0; i < N; i++) {
                randIndex[i] = i;
            }
            StdRandom.shuffle(randIndex);
        }
        
        public boolean hasNext() {
            return count < size;
        }
        
        public void remove() {
            throw new UnsupportedOperationException(); 
        }
        
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = a[randIndex[count]];
            count++;
            return item;
        }
        
    }
    
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) {
                q.enqueue(item);
                StdOut.println("Add item:       " + item);
            }
            else
                StdOut.println("Remove item:    " + q.dequeue());
        }
    }
    
}
