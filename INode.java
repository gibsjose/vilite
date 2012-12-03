package vilite;

/**********************************************************************
 * Interface for a node in a linklist. These commands to be overridden
 * in the Node class. 
 * 
 * @author Joe Gibson, Ryan Zondervan
 * @version 11/7/2012
 *********************************************************************/
public interface INode {
	/* returns the data stored in this node */
	String getData();

	/* sets the data to the argument value */
	void setData(String data);

	/* returns the value of the next field of this node */
	INode getNext();

	/* sets the next field of this node to the argument value */
	void setNext(INode next);
}
