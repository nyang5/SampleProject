
//Description: this is the HuffmanTree class that is used to compress files base on the 
//frequency of characters in the file. Huffman's method use only a few bits for 
//characters that are used often, and more bits for those rarely used. rather than 
//the general seven or eight bits per character. 
//
import java.io.*;
import java.util.*;

public class HuffmanTree {
	private HuffmanNode overallRoot;
	
	//post:construct initial Huffman tree using the given array of frequencies where
	//count[i] is the number of occurrences of the character with ASCII value i.
	public HuffmanTree(int[] count) {
		Queue<HuffmanNode> priority = new PriorityQueue<HuffmanNode>();
		for (int i = 0; i < count.length; i++) {
			if (count[i] > 0) {
				priority.add(new HuffmanNode(i, count[i]));
			}
		}
		priority.add(new HuffmanNode(count.length, 1)); 
		while (priority.size() > 1) {
			HuffmanNode left = priority.remove();
			HuffmanNode right = priority.remove();
			priority.add(new HuffmanNode(-1, right.frequency + left.frequency, left, right)); 
		}
		overallRoot = priority.remove();
	}
	
	//post:write the tree to the given output stream in standard format
	public void write(PrintStream output) {
		writeTree(output, overallRoot, "");
	}
	
	//post:write the tree to the given output stream in standard format
	private void writeTree(PrintStream output, HuffmanNode root, String binaryNum) {
		if (root.left == null && root.right == null) {
			output.println(root.data);
			output.println(binaryNum);
		} else {
			writeTree(output, root.left, binaryNum + 0);
			writeTree(output, root.right, binaryNum + 1);
		}
	}
	
	//pre: the scanner contains a tree stored in standard format
	//post:reconstruct the tree from an input file.
	public HuffmanTree(Scanner input) {
		while	(input.hasNext()) {
			int data = Integer.parseInt(input.nextLine());
			String code = input.nextLine();
			overallRoot = HuffmanHelp(data, code, overallRoot);
		}
	}
	
	//post: returns a HuffmanNode. reconstruct the tree and leaf node from the 
	//given data and string code. 
	private HuffmanNode HuffmanHelp(int data, String code, HuffmanNode root) {
		if (code.length() == 0) {
			return new HuffmanNode(data, -1);
		} else {
			if (root == null) {
				root = new HuffmanNode(0, -1); 
			}
			if (code.charAt(0) == '0') {
				root.left = HuffmanHelp(data, code.substring(1), root.left);
			} else {
				root.right = HuffmanHelp(data, code.substring(1), root.right);
			}
		}
		return root;
	}
	
	//pre:input stream contains a legal encoding of characters for this tree's
	//Huffman code
	//post:read individual bits from the input stream and write the corresponding
	//characters to the output stream. when the character with value equal to eof
	//parameter passed in, it should stop on reading the file.
	public void decode(BitInputStream input, PrintStream output, int eof) {
		int data = decodeHelp(input, overallRoot);
		while (data != eof) {
			output.write(data);
			data = decodeHelp(input, overallRoot);
		}
	}
	
	//pre:input stream contains a legal encoding of characters for this tree's
	//Huffman code
	//post: returns an integer the ASCII value of the character of the leaf node root. 
	//takes in the input file and read through bit by bit until it reaches a leaf node. 
	private int decodeHelp(BitInputStream input, HuffmanNode root) {
		while (root.left != null && root.right != null) { 
			if (input.readBit() == 0) {
				root = root.left;
			} else {
				root = root.right;
			}
		}
		return root.data;
	}
}
