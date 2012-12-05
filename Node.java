package vilite;

/**********************************************************************
Node.java implements a node to be referenced by the MyLinkedList class.
A node consists of a data string and a pointer to a 'next' node.

@author Joe Gibson, Ryan Zondervan
@version 12/5/2012
**********************************************************************/
public class Node implements INode {

	/** The data within a node */
	private String data;

	/** The next node in the linklist */
	private Node next;

	/******************************************************************
	Constructs an empty node.
	******************************************************************/
	public Node() {

	}

	
	/******************************************************************
	Constructs a node given a data string and a next pointer.
	@param the data string
	@param the next pointer
	******************************************************************/
	public Node(String data, Node next) {
		this.data = data;
		this.next = next;
	}

	
	/******************************************************************
	Returns the data string.
	@return the data string
	******************************************************************/
	public String getData() {
		return data;
	}

	
	/******************************************************************
	Sets the data string.
	@param the data string
	******************************************************************/
	public void setData(String data) {
		this.data = data;
	}

	
	/******************************************************************
	Returns the next pointer.
	@return the next pointer
	******************************************************************/
	public INode getNext() {
		return next;
	}

	
	/******************************************************************
	Sets the next pointer.
	@param the next pointer
	******************************************************************/
	public void setNext(INode next) {
		this.next = (Node) next;
	}
}