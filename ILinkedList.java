package vilite;

/**********************************************************************
ILinkedList.java implements the linked list interface. Methods are to
be overridden by the MyLinkedList class.

@author Joe Gibson, Ryan Zondervan
@version 12/5/2012
**********************************************************************/
public interface ILinkedList {

	/* adds the given element to the end of the list */
	void add(String element);

	/* inserts the given element at the specified position in the list */
	void add(int index, String element);

	/* removes the element at the specified position in the list */
	String remove(int index);

	/* returns the element at the specified position in the list */
	String get(int index);

	/* returns true if the list is empty; false otherwise */
	boolean isEmpty();

	/* returns the number of elements in the list */
	int size();

	/* removes all the elements from the list */
	void clear();
}
