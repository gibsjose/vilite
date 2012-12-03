package vilite;

/**********************************************************************
 * Represents a node in a linklist to be incorporated by the MyLinkedList class.
 * 
 * @author Joe Gibson, Ryan Zondervan
 * @version 11/7/2012
 *********************************************************************/
public class Node implements INode {

	/** The data within a node */
	private String data;

	/** The next node in the linklist */
	private Node next;

	/******************************************************************
	 * Empty Node constructor
	 ******************************************************************/
	public Node() {

	}

	/******************************************************************
	 * Node constructor passing in data and a next node for a single linklist.
	 * 
	 * @param String
	 *            data the data within the node
	 * @param Node
	 *            next the next node in the linklist
	 ******************************************************************/
	public Node(String data, Node next) {
		this.data = data;
		this.next = next;
	}

	/******************************************************************
	 * Get and set methods for Data.
	 * 
	 * @param String
	 *            data the data in the node
	 ******************************************************************/
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	/******************************************************************
	 * Get and set methods for the next node in the linklist.
	 * 
	 * @param INode
	 *            next the next node in the linklist
	 ******************************************************************/
	public INode getNext() {
		return next;
	}

	public void setNext(INode next) {
		this.next = (Node) next;
	}
}