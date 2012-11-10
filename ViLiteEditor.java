package vilite;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ViLiteEditor implements IEditor {

	Scanner input;
	
	String command;
	
	int currentLine;
	
	int numLines;

	MyLinkedList list;

	public ViLiteEditor() {
		input = new Scanner(System.in);
		command = null;
		currentLine = 0;
		numLines = 0;
		list = new MyLinkedList();
	}

	@Override
	public void run() {
		System.out.println("Command:");
		command = input.nextLine();
		processCommand(command);
	}

	@Override
	public void processCommand(String command) {

		Scanner scanner = new Scanner(command);

		String cmd = null;
		String param1 = null;
		String param2 = null;

		try {
			cmd = scanner.next();
		} catch(NoSuchElementException e) {
			cmd = param1 = param2 = null;
		}

		//Check for b, i, and e, which are a command followed by a sentence
		
		if(cmd.equals("b")) {
			String sentence = scanner.nextLine();
			
			if(list.isEmpty()) {
				insertEnd(sentence);
				currentLine++;
			}
			else {
				insertBefore(sentence);
				currentLine--;
			}
			
			numLines++;
			
			return;
		}
		
		if(cmd.equals("i")) {
			
			String sentence = scanner.nextLine();
			
			if(list.isEmpty()) {
				insertEnd(sentence);
				currentLine++;
			}
			else {
				insertAfter(sentence);
				currentLine++;
			}
			
			numLines++;
			
			return;
		}
		
		if(cmd.equals("e")) {
			
			String sentence = scanner.nextLine();
			
			insertEnd(sentence);
			
			numLines++;
			currentLine = numLines;
			
			return;
		}

		try {
			param1 = scanner.next();
		} catch(NoSuchElementException e) {
			param1 = param2 = null;
		}

		try {
			param2 = scanner.next();
		} catch(NoSuchElementException e) {
			param2 = null;
		}

		if(cmd.equals("m")) {

		}

		if(cmd.equals("u")) {

		}

		if(cmd.equals("r")) {

		}

		if(cmd.equals("d")) {
			if(param1 == null && param2 == null) 
				display(0, numLines);
			
			else if(param1 == "c" && param2 == null)
				System.out.println("==> " + list.get(currentLine));
			
			else {
				//Check for non-integer parameters and display error
			}
		}

		if(cmd.equals("c")) {

		}
		
		if(cmd.equals("s")) {

		}
		
		if(cmd.equals("l")) {

		}
		
		if(cmd.equals("h")) {

		}
		
		if(cmd.equals("x")) {

		}
		
		if(cmd.equals("mc")) {

		}
		
		if(cmd.equals("fr")) {

		}
		
		if(cmd.equals("sw")) {

		}
		
		if(cmd.equals("ud")) {

		}
	}

	@Override
	public int getNbrLines() {
		return numLines;
	}

	@Override
	public int getCurrentLineNbr() {
		return currentLine;
	}

	@Override
	public String getLine(int lineNbr) {
		return list.get(lineNbr);
	}

	@Override
	public String[] getLines(int beginPos, int endPos) {
		String[] lines = {null};
		
		for(int i = beginPos; i <= endPos; i++)
			lines[i] = list.get(i);
		
		return lines;
	}

	@Override
	public void insertBefore(String line) {
		list.add(currentLine, line);
	}

	@Override
	public void insertAfter(String line) {
		list.add((currentLine + 1), line);
	}

	@Override
	public void insertEnd(String line) {
		list.add(line);
	}

	@Override
	public void moveUp(int nbrOfPositions) {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveDown(int nbrOfPositions) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(int nbrLinesToRemove) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(String filename) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void load(String filename) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public int findReplace(String str1, String str2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void moveCurrentLineTo(int lineNbr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void switchCurrentLineWith(int lineNbr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}
	
	private void display(int start, int end) {
		for(int i = start; i <= end; i++) {
			if(list.get(i) != null) {
				if(i == currentLine)
					System.out.println("==> \t" + list.get(i));
				else
					System.out.println("\t" + list.get(i));
			}
		}
	}
	
	public static void main (String[] args) {
		ViLiteEditor vilite = new ViLiteEditor();

		while(true) {
			vilite.run();
		}
	}
}
