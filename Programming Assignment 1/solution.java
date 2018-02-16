import java.util.*;

public class Solution {
		
	public static void main(String[] args) {
		// Instantiate a new tree.
		TwoThreeTree planets = new TwoThreeTree();
		
		// Read the user input.
		Scanner scanner = new Scanner(System.in);
		
		// Get the size of the database.
		int size = scanner.nextInt();
		
		// Insert all nodes into tree.
		for (int i = 0; i < size; i++) {
			String planet = scanner.next();
			int fee = scanner.nextInt();
			twothree.insert(planet, fee, planets);
		}
		
		// Get the number of queries.
		int queries = scanner.nextInt();
		
		for (int i = 0; i < queries; i++) {
			String planet1 = scanner.next();
			String planet2 = scanner.next();
			twothree.search(planets, planet1, planet2);
		}
		
		scanner.close();
	}
}

class twothree {	
	static void search(TwoThreeTree planets, String planet1, String planet2) {
		// Check the lexicographic values of the planet names.
		// If planet2 has a smaller lexicographic value than planet1, swap their values.
		if (planet2.compareTo(planet1) < 0) {
			String temp = planet1;
			planet1 = planet2;
			planet2 = temp;
		}	
		
		searchHelper(planets.root, planet1, planet2, planets.height);
	}
	
	static void searchHelper(Node node, String planet1, String planet2, int height) {		
		if (height > 0) {
			InternalNode q = (InternalNode) node;
			
			if (planet1.compareTo(q.child0.guide) <= 0)
				searchHelper(q.child0, planet1, planet2, height - 1);
			if (planet2.compareTo(q.child0.guide) > 0 || planet1.compareTo(q.child1.guide) <= 0)
				searchHelper(q.child1, planet1, planet2, height - 1);
			if (planet2.compareTo(q.child1.guide) > 0 && q.child2 != null)
				searchHelper(q.child2, planet1, planet2, height - 1);
		}
		else {
			LeafNode leaf = (LeafNode) node;
			
			if (planet1.compareTo(leaf.guide) <= 0 && planet2.compareTo(leaf.guide) >= 0)
				System.out.println(leaf.guide + " " + leaf.value);
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
				leaf.value = value; 
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
		x[0] = q.child0; x[1] = q.child1;
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
