package vilite;

public class MyLinkedList implements ILinkedList {

	private Node top;
	private Node current;
	private Node last;

	public MyLinkedList() {
		top = current = last = null;
	}

	@Override
	public void add(String element) {

		if(top == null) 
			top = current = last = new Node(element, null);

		else {
			Node temp = top;

			while(temp.getNext() != null) {
				temp = (Node) temp.getNext();
			}

			temp.setNext((Node) new Node(element, null));

			current = last = (Node) temp.getNext();
		}
	}

	@Override
	public void add(int index, String element) {
		if(top == null)
			top = current = last = new Node(element, null);

		else {
			
			if(index == 1) {
				Node temp = (Node) top.getNext();
				temp.setData(element);
				top.setNext((Node) temp);
			}

			else if(index == this.size()) {
				this.add(element);
			}

			else {
				Node temp = top;
				for(int i = 0; i < index; i++) {
					if(temp.getNext() != null)
				}
			}
		}
	}

	@Override
	public String remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String get(int index) {

		return null;
	}

	@Override
	public boolean isEmpty() {

		if(top == null)
			return true;
		else
			return false;
	}

	@Override
	public int size() {

		Node temp = top;
		int size = 0;

		while(temp.getNext() != null) {
			size ++;
		}

		return size;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}
}
