package edu.nyu.cs.dg2703;

import java.util.*;

public class Solution {
		
	public static void main(String[] args) {
		// Instantiate a new tree.
		TwoThreeTree planets = new TwoThreeTree();
		
		// Read the user input.
		Scanner scanner = new Scanner(System.in);
		
		// Get the number of queries.
		int queries = scanner.nextInt();
		
		// Loop over the queries.
		for (int i = 0; i < queries; i++) {
			// Get the number of which query is to be performed.
			int query = scanner.nextInt();
			
			String planet, planet1, planet2;
			int fee;
			
			switch (query) {
				// Case 1: insert a planet into the tree with its given fee.
				case 1:
					planet = scanner.next();
					fee = scanner.nextInt();
					twothree.insert(planet, fee, planets);
					break;
				// Case 2: increase the entrance fee for all planets between planet1 and planet2 by feeInfo.
				case 2:
					planet1 = scanner.next();
					planet2 = scanner.next();
					fee = scanner.nextInt();
					twothree.update(planets, planet1, planet2, fee);
					break;
				// Case 3: return the entrance fee for planetInfo.
				case 3:
					planet = scanner.next();
					twothree.findValue(planets.root, planet, 0, planets.height);
					break;
			}
		}

		// Close the scanner.
		scanner.close();
	}
}

class twothree {		
	// findValue method gets the entrance fee of the selected planet.
	static void findValue(Node node, String planet, int lazy, int height) {
		// Update the fee.
		lazy += node.lazy;
		
		// Recurse over the tree as long as the height is greater than 0.
		if (height > 0) {
			// Instantiate an internal node.
			InternalNode q = (InternalNode) node;
			
			// Search for the planet.
			if (planet.compareTo(q.child0.guide) <= 0)
				findValue(q.child0, planet, lazy, height - 1);
			else if (planet.compareTo(q.child1.guide) <= 0 || q.child2 == null)
				findValue(q.child1, planet, lazy, height - 1);
			else if ((planet.compareTo(q.child2.guide) <= 0) && q.child2 != null)
				findValue(q.child2, planet, lazy, height - 1);
			// Return -1 if the planet does not exist.
			else
				System.out.println(-1);
		}
		else {
			// Instantiate a leaf node.
			LeafNode leaf = (LeafNode) node;
			
			// If the planet is found, print out it's entrance fee.
			if (planet.compareTo(leaf.guide) == 0)
				System.out.println(leaf.value + lazy);
			// Return -1 if the planet does not exist.
			else
				System.out.println(-1);
		}
	}
	
	// Update method that checks the two-three tree for the given planets.
	// Use the lazy method without path finding.
	static void update(TwoThreeTree planets, String planet1, String planet2, int fee) {
		// Check the lexicographic values of the planet names.
		// If planet2 has a smaller lexicographic value than planet1, swap their values.
		if (planet2.compareTo(planet1) < 0) {
			String temp = planet1;
			planet1 = planet2;
			planet2 = temp;
		}	
		
		// Call the helper method.
		updateHelper(planets.root, "", planets.root.guide, planet1, planet2, planets.height, fee);
	}
	
	// updateHelper method that searches the two-three tree for the given planets and updates their entrance fees.
	// Use the lazy method without path finding.
	static void updateHelper(Node node, String min, String max, String planet1, String planet2, int height, int fee) {	
		// Recurse over the tree as long as the height is greater than 0.
		if (height > 0) {
			// Instantiate an internal node.
			InternalNode q = (InternalNode) node;

			// Check if the planets are between planet1 and planet2 inclusively. 
			if (planet1.compareTo(min) <= 0 && planet2.compareTo(q.guide) >= 0)
				// Update the fee.
				q.lazy += fee;
			else if (planet2.compareTo(min) > 0 && planet1.compareTo(q.guide) <= 0) {
				updateHelper(q.child0, min, q.child0.guide, planet1, planet2, height - 1, fee);
				updateHelper(q.child1, q.child0.guide, q.child1.guide, planet1, planet2, height - 1, fee);
				if (q.child2 != null)
					updateHelper(q.child2, q.child1.guide, q.child2.guide, planet1, planet2, height - 1, fee);
			}
		}
		else {
			// Instantiate a leaf node.
			LeafNode leaf = (LeafNode) node;
			
			// If the leaf node is between planet1 and planet2, increase its entrance fee.
			if (planet1.compareTo(leaf.guide) <= 0 && planet2.compareTo(leaf.guide) >= 0)
				// Update the fee.
				leaf.lazy += fee;
		}
	} // searchHelper
    
	static void insert(String key, int value, TwoThreeTree tree) {
	// Insert a key value pair into tree (overwrite existing value if key is already present).
		int h = tree.height;

		if (h == -1) {
			LeafNode newLeaf = new LeafNode();
			newLeaf.guide = key;
			newLeaf.value = value;
			tree.root = newLeaf; 
			tree.height = 0;
		}
		else {
			WorkSpace ws = doInsert(key, value, tree.root, h);

			if (ws != null && ws.newNode != null) {
			// Create a new root.
				InternalNode newRoot = new InternalNode();
        	 
	        	 if (ws.offset == 0) {
	        		 newRoot.child0 = ws.newNode; 
	        		 newRoot.child1 = tree.root;
	        	 }
	        	 else {
	        		 newRoot.child0 = tree.root; 
	        		 newRoot.child1 = ws.newNode;
	        	 }
	        	 resetGuide(newRoot);
	        	 tree.root = newRoot;
	        	 tree.height = h+1;
			}
		}
	}

	static WorkSpace doInsert(String key, int value, Node p, int h) {
	// Auxiliary recursive routine for insert.
		if (h == 0) {
			// We're at the leaf level, so compare and either update value or insert new leaf.
			LeafNode leaf = (LeafNode) p;
			int cmp = key.compareTo(leaf.guide);

			if (cmp == 0) {
				// Update each planet's fee with the lazy method without path finding.
				leaf.value = value - leaf.lazy;
				return null;
			}

			// Create new leaf node and insert into tree.
			LeafNode newLeaf = new LeafNode();
			newLeaf.guide = key; 
	        newLeaf.value = value;

	        int offset = (cmp < 0) ? 0 : 1;

	        WorkSpace ws = new WorkSpace();
	        ws.newNode = newLeaf;
	        ws.offset = offset;
	        ws.scratch = new Node[4];

	        return ws;
        }
		else {
			InternalNode q = (InternalNode) p;
			int pos;
			WorkSpace ws;

			// Update each planet's fee with the lazy method without path finding.
			value = value - q.lazy;

			if (key.compareTo(q.child0.guide) <= 0) {
				pos = 0; 
				ws = doInsert(key, value, q.child0, h-1);
			}
			else if (key.compareTo(q.child1.guide) <= 0 || q.child2 == null) {
				pos = 1;
				ws = doInsert(key, value, q.child1, h-1);
			}
			else {
				pos = 2; 
				ws = doInsert(key, value, q.child2, h-1);
			}

			if (ws != null) {
				if (ws.newNode != null) {
					int sz = copyOutChildren(q, ws.scratch);
					insertNode(ws.scratch, ws.newNode, sz, pos + ws.offset);
					if (sz == 2) {
						ws.newNode = null;
						ws.guideChanged = resetChildren(q, ws.scratch, 0, 3);
					}
					else {
						ws.newNode = new InternalNode();
						ws.offset = 1;

						ws.newNode.lazy = q.lazy; // NEW

						resetChildren(q, ws.scratch, 0, 2);
						resetChildren((InternalNode) ws.newNode, ws.scratch, 2, 2);
					}
				}
				else if (ws.guideChanged) {
					ws.guideChanged = resetGuide(q);
				}
			}

			return ws;
		}
	}

	static int copyOutChildren(InternalNode q, Node[] x) {
	// Copy children of q into x, and return num of children.
		int sz = 2;
		x[0] = q.child0; 
		x[1] = q.child1;
		if (q.child2 != null) {
			x[2] = q.child2; 
			sz = 3;
		}
		return sz;
	}

	static void insertNode(Node[] x, Node p, int sz, int pos) {
		for (int i = sz; i > pos; i--)
			x[i] = x[i-1];

		x[pos] = p;
	}

	static boolean resetGuide(InternalNode q) {
	// Reset q.guide, and return true if it changes.
		String oldGuide = q.guide;
		if (q.child2 != null)
			q.guide = q.child2.guide;
		else
			q.guide = q.child1.guide;

		return q.guide != oldGuide;
	}

	static boolean resetChildren(InternalNode q, Node[] x, int pos, int sz) {
		q.child0 = x[pos]; 
		q.child1 = x[pos+1];

		if (sz == 3) 
			q.child2 = x[pos+2];
		else
			q.child2 = null;

		return resetGuide(q);
	}
} // twothree

class Node {
	String guide;
	// The value the planet's entrance fees will be updated by.
	int lazy = 0;
}

class InternalNode extends Node {
	Node child0, child1, child2;
}

class LeafNode extends Node {
	// Guide points to the key.
	int value;
}

class TwoThreeTree {
	Node root;
	int height;

	TwoThreeTree() {
		root = null;
		height = -1;
	}
}

class WorkSpace {
	// This class is used to hold return values for the recursive doInsert routine.
	Node newNode;
	int offset;
	boolean guideChanged;
	Node[] scratch;
}
