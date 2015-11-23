import java.util.Iterator;


//A sorted symbol table - a sorted inked list that stores both keys and values

public class ST<Item extends Comparable<Item>, Value> implements Iterable<Item> {
	private Node root;
	private int size = 0;
	private class Node {
		Item item;
		Value val;
		Node next;
		Node prev;
	}
	
	public void put(Item item, Value val) {
		if(isEmpty()) {
			root = new Node();
			root.item = item;
			root.val = val;
		} else {
			put(root, item, val);
		}
		size++;
	}

	public boolean isEmpty() {
		return root == null;
	}

	private void put(Node x, Item item, Value val) {
		int compare = 0;
		if(x != null) compare = item.compareTo(x.item);
		
		if(compare <= 0) {
			Node newNode = new Node();
			//possibly assign the previous
			if(x.prev != null) {
				Node last = x.prev;
				last.next = newNode;
				newNode.prev = last; 
				x.prev = newNode;
			} else {
				x.prev = newNode;
			}
			newNode.next = x;
			newNode.val = val;
			newNode.item = item;
		} else {
			if(x.next == null) {
				x.next = new Node();
				x.next.item = item;
				x.next.prev = x;
				x.next.val = val;
				return;
			}
			put(x.next, item, val);
		}
	}
	
	public Value get(Item item) {
		for (Node x = root; x != null; x = x.next) {
			if(x.item.equals(item)) return x.val;
		}
		return null;
	} 
	
	public boolean containsItem(Item item) {
		for(Node x = root; x != null; x = x.next) {
			if(x.item.equals(item)) return true;
		}
		return false;
	}
	
	public int getSize() {
		return size;
	}
	
	public String toString() {
		String s = "";
		for(Node x = root; x != null; x = x.next) {
			s += x.val.toString() + " ";
		}
		return s;
	}

	public boolean contains(Value val) {
		for(Node x = root; x != null; x = x.next) {
			if(x.val.equals(x)) 
				return true; 
		}
		return false;
	}
	
	public Item getItem(Value val) {
		for(Node x = root; x != null; x = x.next) {
			if(x.val.equals(val)) return x.item; 
		}
		return null;
	}
	
	public Value popMin() {
		Value val = root.val;
		root = root.next;
		return val;
	}
	
	public Iterator<Item> iterator() {
		// TODO Auto-generated method stub
		return new STIterator();
	}
	
	private class STIterator implements Iterator<Item> {
		LinkedList<Node> stack = new LinkedList<Node>();
		STIterator() {
			// TODO Auto-generated constructor stub
			pushLeft(root);	
		}
		private void pushLeft(Node x) {
			while(x != null) {
				stack.push(x);
				x = x.next;
			}
		}

		public boolean hasNext() {
			// TODO Auto-generated method stub
			return !stack.isEmpty();
		}

		public Item next() {
			// TODO Auto-generated method stub
			return stack.pop().item;
		}

		public void remove() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
