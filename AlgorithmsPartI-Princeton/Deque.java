import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int N;
    private Node first;
    private Node last;
    
    private class Node {
        private Item item;
        private Node prev;
        private Node next;
    }
    
    public Deque() {
        first = null;
        last = null;
        N = 0;
        assert check();
    }
    
    public boolean isEmpty() {
        return N == 0;
    }
    
    public int size() {
        return N;
    }
    
    public void addFirst(Item item) {
        if (item == null) throw new NullPointerException();
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        if (!isEmpty()) oldFirst.prev = first;
        if (isEmpty()) {
            last = first;
        }
        N++;
        assert check();
    }
    
    public void addLast(Item item) {
        if (item == null) throw new NullPointerException();
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.prev = oldLast;
        if (!isEmpty()) oldLast.next = last;
        if (isEmpty()) {
            first = last;
        }
        N++; 
        assert check();
    }
    
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = first.item;
        if (N > 1) first.next.prev = null;
        if (N == 1) last = null;
        first = first.next;
        N--;
        assert check();
        return item;
    }
        
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = last.item;
        if (N > 1) last.prev.next = null;
        if (N == 1) first = null;
        last = last.prev;
        N--;
        assert check();
        return item;
    }
    
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;
        
        public boolean hasNext() {
            return current != null;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    
    private boolean check() {
        if (N == 0) {
            if (first != null)  return false;
            if (last != null)   return false;
        }
        else if (N == 1) {
            if (first == null || last == null)  return false;
            if (first != last)                  return false;
            if (first.next != null)             return false;
        }
        else {
            if (first == last)          return false;
            if (first.next == null)     return false;
            if (first.prev != null)     return false;
            if (last.next != null)      return false;
            if (last.prev == null)      return false;
            
            int numberOfNodes = 0;
            for (Node x = first; x != null; x = x.next) {
                numberOfNodes++;
            }
            if (numberOfNodes != N) return false;
            
            Node lastNode = first;
            while (lastNode.next != null)
                lastNode = lastNode.next;
            if (last != lastNode) return false;
        }
        
        return true;
    }
    
    public static void main(String[] args) {
        Deque<String> q = new Deque<String>();
        StdOut.printf("__Operations__\t\t\t __CurrentQueue__\n");
        while (!StdIn.isEmpty()) {
            //StdOut.printf("__Operations__\t\t\t\t __CurrentQueue__\n");
            String item = StdIn.readString();
            if (!item.equals("-") && !item.equals("+")) {
                if (Character.isLowerCase(item.charAt(0))) {
                    q.addFirst(item);
                    StdOut.printf("Add in First:      %s, \t\t %s\n", item, q);
                }
                else if (Character.isUpperCase(item.charAt(0))) {
                    q.addLast(item);
                    StdOut.printf("Add in Last:       %s, \t\t %s\n", item, q);
                }
            } else if (!q.isEmpty()) {
                if (item.equals("-"))           
                    StdOut.printf("Remove From first: %s, \t\t %s\n", q.removeFirst(), q);
                else if (item.equals("+"))      
                    StdOut.printf("Remove From last:  %s, \t\t %s\n", q.removeLast(),  q);
            }

        }
    }
}
