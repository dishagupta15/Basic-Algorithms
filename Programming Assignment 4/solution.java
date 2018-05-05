package edu.nyu.cs.dg2703;

import java.util.*;

public class Solution {
	public static void main(String[] args) {
		// Initialize a Scanner.
		Scanner input = new Scanner(System.in);

		// Get the number of stages of the potion and the number of relations for making the stages.
		int stages = input.nextInt();
		int relations = input.nextInt();
		
		// Initialize an ArrayList that contains an ArrayList of Integers.
		// This ArrayList nested in an ArrayList will store every vertices connections that will form the graph.
		ArrayList<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>();
		
		// Initialize an array to the size of the stages plus one that will track each vertices in degree.
		int inDegree[] = new int[stages + 1];

		// Instantiate a new instance of Comparison.
		Comparator<Integer> comparator = new Comparison();

		// Initialize a PriorityQueue implemented as a min heap.
		// Pass in the number of relations for making the stages and the comparator.
		PriorityQueue<Integer> minHeap = new PriorityQueue<Integer>(relations, comparator);

		// Initialize an ArrayList to store the output.
		ArrayList<Integer> output = new ArrayList<Integer>();
		
		// Loop over the number of stages required and instantiate a new ArrayList for each stage.
		// These ArrayList's will hold each stage's connections.
		for (int i = 0; i <= stages; i++) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			graph.add(i, list);
		}

		// Check each relation entered.
		for (int i = 1; i <= relations; i++) {
			int node1 = input.nextInt();
			int node2 = input.nextInt();
			// Add the connection (node2) given for the specified stage (node1).
			graph.get(node1).add(node2);
		}
		
		// Count the number of in degree's each stage has.
		for (int i = 1; i <= stages; i++) {
			for (int j : graph.get(i))
				// Update each stage's in degree in the array.
				inDegree[j] = inDegree[j] + 1;
		}
		
		// Add every stage that has an in degree of 0 to the min heap.
		for (int i = 1; i <= stages; i++) {
			if (inDegree[i] == 0)
				minHeap.add(i);
		}
		
		// Top Sorting of the min heap.
		// Loop over the min heap while it is not empty.
		while (!minHeap.isEmpty()) {
			// Get the element at the top of the min heap.
			int x = minHeap.poll();
			// Add the element to the ArrayList containing the output.
			output.add(x);
			// Loop over every stage.
			for (int i : graph.get(x)) {
				// Decrement the in degree until it equals 0.
				inDegree[i] = inDegree[i] - 1;
				// Add every stage that has an in degree of 0 to the min heap.
				if (inDegree[i] == 0)
					minHeap.add(i);
			}
		}

		input.close();
		
		// If the output ArrayList is less than the number of stages, print -1.
		if (output.size() < stages)
			System.out.println(-1);
		// Else, print out the sorted output.
		else {
			for (int i : output)
				System.out.print(i + " ");
		}
	} // main
} // Solution

class Comparison implements Comparator<Integer> {
	@Override
	// Override Java's compare() method for Integers.
	// Use this to implement the PriorityQueue as a min heap.
	public int compare(Integer a, Integer b) {
		// If a is less than b return -1, if a is greater than b return 1, else return 0.
		return a < b ? -1 : a > b ? 1: 0;
	}
}
