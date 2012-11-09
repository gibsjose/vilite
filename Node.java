package vilite;

public class Node implements INode {
	
	private String data;
	private Node next;
	
	public Node() {
		
	}
	
	public Node(String data, Node next) {
		this.data = data;
		this.next = next;
	}

	@Override
	public String getData() {
		return data;
	}

	@Override
	public void setData(String data) {
		this.data = data;
	}

	@Override
	public INode getNext() {
		return next;
	}

	@Override
	public void setNext(INode next) {
		this.next = (Node) next;
	}
}