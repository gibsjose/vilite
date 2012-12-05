package vilite;

/**********************************************************************
INode.java implements the node interface. The methods are to be
overridden by the Node class.

@author Joe Gibson, Ryan Zondervan
@version 12/5/2012
**********************************************************************/
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
