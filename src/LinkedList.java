
import java.util.Iterator;


public class LinkedList<Item> implements Iterable<Item>{
	private Node first;
	private Node last;
	//private Node indexNode;
	private int numberOfLinks = 0;
	private class Node {
		Item item;
		Node next;
	}
	
	public boolean isEmpty() {
		return first == null && last == null;
	}
	
	public void push(Item item) {
		Node oldFirst = first;
		first = new Node();
		first.item = item;
		first.next = oldFirst;
		numberOfLinks++;
	}
	
	public Item pop() {
		Item item = first.item;
		first = first.next;
		--numberOfLinks;
		return item;
	}
	
	public void add(Item item) {
		if(isEmpty()) {
			last = new Node();
			first = last;
			last = first;
			last.item = item;
		} else {
			Node oldLast = last;
			last  = new Node();
			oldLast.next = last;
			last.item  = item;
		}
		numberOfLinks++;
	}
	
	public Item getItem(int index) {
		int count = 1;
		Node indexNode = new Node();
		indexNode = first;
		while(count < index) {
			indexNode = indexNode.next;
			count++;
		}
		return indexNode.item;
	}
	
	public void remove(int index) {
		int count = 2;
		Node indexNode = new Node();
		indexNode = first;
		while(count < index) {
			indexNode  = indexNode.next;
			count++;
		}
		Node furtherNode = new Node();
		furtherNode = indexNode.next;
		indexNode.next = furtherNode.next;
		--numberOfLinks;
	}
	
	public void set(Item item, int index){
		int count = 1;
		Node indexNode = new Node();
		indexNode = first;
		while(count < index) {
			indexNode = indexNode.next;
			count++;
		}
		indexNode.item = item; 
	}
	
	public void add(Item item, int index) {
		int count = 2;
		Node indexNode = new Node();
		indexNode = first;
		if(index == 1){
			push(item);
			return;
		}
		if(index == numberOfLinks) {
			add(item);
			return;
		}
		if(index > numberOfLinks) {
			System.out.println("Index out of bounds. System will exit");
			System.exit(0);
		}
		while(count < index) {
			indexNode = indexNode.next;
			count++;
		} 
		Node temp  = new Node();
		temp = indexNode;
		Node furtherNode = new Node();
		furtherNode = temp.next;
		indexNode = new Node();
		temp.next = indexNode;
		indexNode.next = furtherNode;
		indexNode.item = item;
		numberOfLinks++;
	}
	
	public void printlist() {
		for(Node x = first; x != null; x = x.next) {
			System.out.println("Node: " + x + "\n" + "Node item: " + x.item + "\nNode next: " + x.next);
		}
	}
	
	public int getSize() {
		return numberOfLinks;
	}
	
	public Iterator<Item> iterator() {
		return new StackIterator();
	}
	
	private class StackIterator implements Iterator<Item> {
		Node current = first;
		
		public boolean hasNext() {
			return current != null;
		}
		
		public Item next() {
			Item item = current.item;
			current = current.next;
			return item;
		}
		
		public void remove() {}
	}
}