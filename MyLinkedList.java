package vilite;

/**********************************************************************
Represents the linklist that the ViLiteEditor class will manipulate. 
These methods will be called to manipulate and edit using the VI Editor. 
Implements the ILinkedList interface.

@author Joe Gibson, Ryan Zondervan
@version 12/5/2012
**********************************************************************/

public class MyLinkedList implements ILinkedList {

	/** The top node of the link list */
	private Node top;

	/******************************************************************
	Constructs a new (blank) linked list.
	******************************************************************/
	public MyLinkedList() {
		top = null;
	}

	
	/******************************************************************
	Adds a new element to the bottom of the linked list.
	@param the element to be added to the bottom of the list
	******************************************************************/
	public void add(String element) {

		//If the list is empty...
		if (top == null)
			top = new Node(element, null);

		//If the list is not empty
		else {
			
			//Point to top
			Node temp = top;

			//Iterate down to the bottom of the list
			while (temp.getNext() != null) {
				
				//Iterate the temp node
				temp = (Node) temp.getNext();
			}

			//Add the node at the bottom
			temp.setNext((Node) new Node(element, null));
		}
	}

	
	/******************************************************************
	Adds an element to the linked list at the specified index.
	@param the index of where to add the item to the list
	@param the element to be added
	******************************************************************/
	public void add(int index, String element) {
		
		//If the list is empty...
		if (top == null) {
			
			//Add the item to the list
			top = new Node(element, null);
			return;
		}

		//If the list is not empty...
		else {

			//If adding to the first position...
			if (index == 1) {
				
				//Add the new node to the top position
				Node temp = new Node(element, top);
				
				//Update top
				top = temp;
				return;
			}

			//Adding to another position
			else {
				
				//Point to top
				Node temp = top;

				//Iterate to one item before the desired position
				for (int i = 1; i < (index - 1); i++) {
					if (temp.getNext() != null) {
						
						//Point to the next node
						temp = (Node) temp.getNext();
					} else {
						
						//Add a new element
						this.add(element);

						return;
					}
				}

				//Insert the temp node
				temp.setNext((INode) new Node(element, 
						(Node) temp.getNext()));
				return;
			}
		}
	}

	
	/******************************************************************
	Removes an item at the specified index.
	@param the index at which to remove the item
	******************************************************************/
	public String remove(int index) {
		// Remember to call isEmpty() before calling this because I 
		//will assume the list is not empty

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
	Returns the data of the element at the specified index.
	@param the index of the desired element
	@return the data of the desired element
	******************************************************************/
	public String get(int index) {
		
		//The list is empty...
		if (top == null)
			return null;

		//Top is requested
		if (index == 1)
			return top.getData();

		//Some other node
		else {
			
			//Point to top
			Node temp = top;

			//Iterate to the specified data
			for (int i = 1; i < index;) {
				if (temp.getNext() != null) {
					temp = (Node) temp.getNext();
				}

				i++;
			}

			//Return the data
			return temp.getData();
		}
	}

	
	/******************************************************************
	Sets the data of the element at the specified index to a given
	string.
	@param the index of the desired element
	@param the new data to be set
	******************************************************************/
	public void set(int index, String data) {
		
		//Empty list
		if (top == null)
			return;

		//Top element
		if (index == 1)
			top.setData(data);

		//Any other element
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
	Determines whether or not the list is empty (top pointing to null).
	@return whether the list is empty
	******************************************************************/
	public boolean isEmpty() {

		if (top == null)
			return true;
		else
			return false;
	}

	
	/******************************************************************
	Returns the size of the list.
	@return the size of the list
	******************************************************************/
	public int size() {

		Node temp = top;
		int size = 0;

		//Increment size for each valid node
		while (temp.getNext() != null) {
			size++;
		}

		return size;
	}

	
	/******************************************************************
	Clears the list. Here comes the garbage collector...
	******************************************************************/
	public void clear() {
		
		//Nom nom nom
		top = null;
	}
}
