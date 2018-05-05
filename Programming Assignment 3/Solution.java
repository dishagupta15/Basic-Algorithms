package edu.nyu.cs.dg2703;

import java.util.*;

public class Solution {
	public static void main(String[] args) {
		// Instantiate a new HashHeap.
		HashHeap hashheap = new HashHeap();
		
		// Get the users input.
		Scanner scanner = new Scanner(System.in);
		
		long candidates = scanner.nextLong();
		
		// Loop over the number of candidates and insert them into the HashHeap.
		for (long i = 0; i < candidates; i++) {
			String name = scanner.next();
			long originalEvaluation = scanner.nextLong();
			hashheap.insert(name, originalEvaluation);
		}
		
		long queries = scanner.nextLong();
		
		// Loop over the number of queries.
		for (long i = 0; i < queries; i++) {
			int query = scanner.nextInt();
			
			String name;
			long improvement, standard;
			
			// Use a switch case to determine the query.
			switch (query) {
				// Update a soldier's score in the HashHeap.
				case 1:
					name = scanner.next();
					improvement = scanner.nextLong();
					hashheap.update(name, improvement);
					break;
				// Print out the number of soldier's who met or exceeded the minimum threshold score.
				case 2:
					standard = scanner.nextLong();
					int count = hashheap.threshold(standard);
					System.out.println(count);
					break;
			}
		}
		
		scanner.close();
	}
}

class Data {
	// Store the soldier's information.
	String name;
	long score;
	int position;
}

class HashHeap {
	// Instantiate a new hashtable.
	Hashtable<String, Data> hash = new Hashtable<String, Data>();
	// Instantiate a new min heap implemented with an ArrayList.
	ArrayList<Data> MinHeap = new ArrayList<Data>();

	// insert() adds another soldier to the min heap.
	public void insert(String name, long score) {
		Data d = new Data();
		d.name = name;
		d.score = score;
		d.position = size();
		hash.put(name, d);
		MinHeap.add(d);
		moveUp(size() - 1);
	}
	
	// update() modifies a soldier's score.
	public void update(String name, long score) {
		Data d = hash.get(name);
		d.score += score;
		d.position = d.position;
		
		moveDown(d.position);
	}
	
	// Swap() changes the values at two indexes.
	public void swap(int pos1, int pos2) {
		Data data1 = MinHeap.get(pos1);
		Data data2 = MinHeap.get(pos2);
		data1.position = pos2;
		data2.position = pos1;
		MinHeap.set(pos1, data2);
		MinHeap.set(pos2, data1);
	}
	
	// moveDown() fixes the min heap if the soldier at position pos violates the min heap property.
	public void moveDown(int pos) {
		// Get the left and right children of pos.
		int left = leftChild(pos);
		int right = rightChild(pos);
		int smallest = pos;
		
		// Compare the scores of the left and right children with pos and get the smallest value.
		if (size() > left && MinHeap.get(left).score < MinHeap.get(pos).score)
			smallest = left;		
		if (size() > right && MinHeap.get(right).score < MinHeap.get(smallest).score)
			smallest = right;
		if (smallest != pos) {
			// Swap with smallest child.
			swap(pos, smallest);
			// Recurse on the child.
			moveDown(smallest);
		}
	}
	
	// moveUp() fixes the min heap if the soldier at position pos violates the min heap property.
	public void moveUp(int pos) {
		if (pos > 0 && MinHeap.get(parent(pos)).score > MinHeap.get(pos).score) {
			// Swap with parent.
			swap(pos, parent(pos));
			// Recurse on the parent.
			moveUp(parent(pos));
		}
	}
	
	// threshold() deletes all soldier's from the min heap whose score doesn't meet the minmum threshold.
	public int threshold(long threshold) {		
		// Check if a soldier's score is less than the threshold.
		while (MinHeap.get(0).score < threshold) {
			Data d = poll();
			// If it is, remove it from the min heap.
			MinHeap.remove(d.name);
		}

		return size();
	}
	
	// poll() deletes the soldier at the head of the min heap.
	public Data poll() {
		if (size() <= 0)
			return null;
		else {
			Data d = MinHeap.get(0);
			swap(0, size() - 1);
			MinHeap.remove(size() - 1);
			moveDown(0);
			return d;
		}
	}
	
	// Size() returns the size of the min heap.
	public int size() {
		return MinHeap.size();
	}
	
	// Parent() returns the index of the parent.
	public int parent(int pos) {
		if (pos == 0)
			return -1;
		return ((pos-1)/2);
	}
	
	// leftChild() returns the index of the left child.
	public int leftChild(int pos) {
		return ((pos * 2) + 1);
	}
	
	// rightChild() returns the index of the right child.
	public int rightChild(int pos) {
		return ((pos * 2) + 2);
	}
} // HashHeap