package vilite;

/**********************************************************************
 * Represents the linklist that the ViLiteEditor class will manipulate. These
 * methods will be called to manipulate and edit using the VI Editor. Implements
 * the ILinkedList interface.
 * 
 * @author Joe Gibson, Ryan Zondervan
 * @version 11/7/2012
 *********************************************************************/
public class MyLinkedList implements ILinkedList {

	/** The top node of the link list */
	private Node top;

	/******************************************************************
	 * Constructor for a new link list.
	 ******************************************************************/
	public MyLinkedList() {
		top = null;
	}

	/******************************************************************
	 * Add an element to the link list.
	 * 
	 * @param String
	 *            element the information in the linklist node
	 ******************************************************************/
	public void add(String element) {

		if (top == null)
			top = new Node(element, null);

		else {
			Node temp = top;

			while (temp.getNext() != null) {
				temp = (Node) temp.getNext();
			}

			temp.setNext((Node) new Node(element, null));
		}
	}

	/******************************************************************
	 * Add an element to the link list.
	 * 
	 * @param String
	 *            element the information in the linklist node
	 ******************************************************************/
	public void add(int index, String element) {
		if (top == null) {
			top = new Node(element, null);
			return;
		}

		else {

			if (index == 1) {
				Node temp = new Node(element, top);
				top = temp;

				return;
			}

			else {
				Node temp = top;

				for (int i = 1; i < (index - 1); i++) {
					if (temp.getNext() != null) {
						temp = (Node) temp.getNext();
					} else {
						this.add(element);

						return;
					}
				}

				temp.setNext((INode) new Node(element, (Node) temp.getNext()));
				return;
			}
		}
	}

	/******************************************************************
	 * Remove an element to the link list.
	 * 
	 * @param int index the number of the node to be removed
	 ******************************************************************/
	public String remove(int index) {
		// Remember to call isEmpty() before calling this because I will assume
		// the list is not empty

		// Case 1: Top element (index of 1)
		if (index == 1) {
			Node temp = top;
			top = (Node) temp.getNext();
			temp.setNext(null);
		}

		// Case 2: All other elements
		else {

			Node temp = top;

			for (int i = 1; i < (index - 1); i++) {
				if (temp.getNext() != null) {
					temp = (Node) temp.getNext();
				}
			}

			temp.setNext(temp.getNext().getNext());
		}

		return null;
	}

	/******************************************************************
	 * Get the information in an element of the link list.
	 * 
	 * @param int index the number node you want to get
	 * @return String the data of the node you want to retrieve
	 ******************************************************************/
	public String get(int index) {
		if (top == null)
			return null;

		if (index == 1)
			return top.getData();

		else {
			Node temp = top;

			for (int i = 1; i < index;) {
				if (temp.getNext() != null) {
					temp = (Node) temp.getNext();
				}

				i++;
			}

			return temp.getData();
		}
	}

	/******************************************************************
	 * Set the data of a specified node.
	 * 
	 * @param int index the number of the node you want to set
	 * @param String
	 *            data the data you wish to set in
	 ******************************************************************/
	public void set(int index, String data) {
		if (top == null)
			return;

		if (index == 1)
			top.setData(data);

		else {
			Node temp = top;

			for (int i = 1; i < index;) {
				if (temp.getNext() != null) {
					temp = (Node) temp.getNext();
				}

				i++;
			}

			temp.setData(data);
		}
	}

	/******************************************************************
	 * Boolean value to depict whether the link list is empty.
	 * 
	 * @return boolean whether or not the list is empty
	 ******************************************************************/
	public boolean isEmpty() {

		if (top == null)
			return true;
		else
			return false;
	}

	/******************************************************************
	 * The size value of the link list.
	 * 
	 * @return int size the size of the link list
	 ******************************************************************/
	public int size() {

		Node temp = top;
		int size = 0;

		while (temp.getNext() != null) {
			size++;
		}

		return size;
	}

	/******************************************************************
	 * Clears the link list. Sets the top to null.
	 ******************************************************************/
	public void clear() {
		top = null;
	}
}
