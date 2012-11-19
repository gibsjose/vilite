package vilite;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ViLiteEditor implements IEditor {

	private Scanner input;

	private String command;

	private int currentLine;

	private int numLines;

	private MyLinkedList list;

	private boolean fileSaved;

	public ViLiteEditor() {
		input = new Scanner(System.in);
		command = null;
		currentLine = 0;
		numLines = 0;
		list = new MyLinkedList();
		fileSaved = false;
	}

	@Override
	public void run() {
		System.out.println("\nCommand:");
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
			return;
		}

		//Check for b, i, and e, which are a command followed by a sentence

		if(cmd.equals("b")) {

			String sentence = null;

			try {
				sentence = scanner.nextLine();
			} catch(NoSuchElementException e) {
				System.out.println("Invalid parameter, try: b <sentence>");
				return;
			}

			if(list.isEmpty()) {
				insertEnd(sentence);
				currentLine++;
			}
			else {
				insertBefore(sentence);
				//No need to decrement currentLine
			}

			numLines++;
			fileSaved = false;

			return;
		}

		if(cmd.equals("i")) {

			String sentence = null;

			try {
				sentence = scanner.nextLine();
			} catch(NoSuchElementException e) {
				System.out.println("Invalid parameter, try: i <sentence>");
				return;
			}

			if(list.isEmpty()) {
				insertEnd(sentence);
				currentLine++;
			}
			else {
				insertAfter(sentence);
				currentLine++;
			}

			numLines++;
			fileSaved = false;

			return;
		}

		if(cmd.equals("e")) {

			String sentence = null;

			try {
				sentence = scanner.nextLine();
			} catch(NoSuchElementException e) {
				System.out.println("Invalid parameter, try: e <sentence>");
				return;
			}

			insertEnd(sentence);
			fileSaved = false;

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
			if(param1 != null) {
				try {
					int num = Integer.parseInt(param1);
					moveDown(num);
					fileSaved = false;
					return;

				} catch(NumberFormatException e) {
					System.out.println("Invalid parameters, try: m #");
					return;
				}
			}

			else {
				moveDown(1);
				return;
			}
		}

		if(cmd.equals("u")) {
			if(param1 != null) {
				try {
					int num = Integer.parseInt(param1);
					moveUp(num);
					fileSaved = false;
					return;

				} catch(NumberFormatException e) {
					System.out.println("Invalid parameters, try: u #");
					return;
				}
			}

			else {
				moveUp(1);
				return;
			}
		}

		if(cmd.equals("r")) {

			if(!list.isEmpty()) {
				if(param1 != null) {
					try {
						int num = Integer.parseInt(param1);
						remove(num);
						fileSaved = false;
						return;

					} catch(NumberFormatException e) {
						System.out.println("Invalid parameters, try: r #");
						return;
					}
				}

				else {
					remove(1);
					fileSaved = false;
					return;
				}
			}

			else
				return;

		}

		if(cmd.equals("d")) {

			if(param1 == null && param2 == null) {
				display(1, numLines);
				return;
			}

			else {

				if(param1.equals("c") && param2 == null) {
					if(list.isEmpty())
						System.out.println("No data to display!");
					else
						System.out.println("==> \t" + list.get(currentLine));

					return;
				}

				try {
					int start = Integer.parseInt(param1);
					int end = Integer.parseInt(param2);

					if(start <= end)
						if(start > 0)
							display(start, end);
						else
							System.out.println("Invalid parameters, try: d # *, where # starts at '1'");
					else
						System.out.println("Invalid parameters, try: d # *, where * is greater than #");

					return;
				} catch(NumberFormatException e) {
					System.out.println("Invalid parameters, try: d # *");

					return;
				}

			}
		}

		if(cmd.equals("c")) {
			clear();
			fileSaved = false;
			numLines = 0;
			currentLine = 0;
			return;
		}

		if(cmd.equals("s")) {
			if(param1 != null)
				save(param1);
			else
				System.out.println("Invalid parameters, try: s <filename>");

			return;
		}

		if(cmd.equals("l")) {

			//Make a save of the current list in case of corruption
			save(".viliteBackup");

			if(param1 != null)
				load(param1);
			else
				System.out.println("Invalid parameters, try: l <filename>");

			return;
		}

		if(cmd.equals("h")) {
			displayHelp();
			return;
		}

		if(cmd.equals("x")) {
			//Check if saved before exiting
			if(list.isEmpty() || fileSaved) {
				System.out.println("Goodbye!");
				System.exit(0);
			}
			else {
				System.out.println("File not saved! Please save before closing");
				return;
			}
		}

		if(cmd.equals("mc")) {

			if(param1 != null) {
				if(param1.equals("$")) {
					moveCurrentLineTo(numLines);
					fileSaved = false;
					return;
				}

				else {
					try {
						int line = Integer.parseInt(param1);

						if(line <= 0 || line > numLines) {
							System.out.println("Invalid parameters, try: mc #, where # starts at '1'");
							return;
						}
						else {
							moveCurrentLineTo(line);
							fileSaved = false;
							return;
						}
					} catch(NumberFormatException e) {
						System.out.println("Invalid parameters, try: mc # or mc $");
						return;
					}
				}
			}
			else {
				System.out.println("Invalid parameters, try: mc # or mc $");
				return;
			}
		}

		if(cmd.equals("fr")) {
			//Add fileSaved = false; for replace only
		}

		if(cmd.equals("sw")) {
			//Add fileSaved = false;
		}

		if(cmd.equals("ud")) {
			//Add fileSaved = false;
		}

		if(cmd != null)
			System.out.println("Command " + cmd + " not recognized.  Type 'h' for help");
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
		for(int i = 0; i < nbrOfPositions; i++)
			if(currentLine != 1)
				currentLine--;
	}

	@Override
	public void moveDown(int nbrOfPositions) {
		for(int i = 0; i < nbrOfPositions; i++)
			if(currentLine != numLines)
				currentLine++;
	}

	@Override
	public void remove(int nbrLinesToRemove) {
		for(int i = 0; i < nbrLinesToRemove; i++) {		
			if(currentLine == numLines) {
				list.remove(currentLine);
				currentLine--;
				numLines--;
				return;
			}

			else {
				list.remove(currentLine);
				numLines --;
			}
		}
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public void save(String filename) {
		String filepath = "/users/Joe/" + filename;
		File saveFile = new File(filepath);
		FileWriter saveFileWriter;

		//Attempt to create the file writer
		try {
			saveFileWriter = new FileWriter(saveFile);
		} catch (IOException e) {
			System.out.println("Error! File could not be saved!");
			return;
		}

		//Create the save string
		String saveString = "";

		//Populate the save string
		if(list.isEmpty()) {
			saveString += "" + 0x55 + "\n";
			saveString += "null";
		}
		else {
			saveString += "" + 0x55 + "\n";
			saveString += "" + currentLine + "\n";
			saveString += "" + numLines + "\n";

			for(int i = 1; i <= numLines; i++) {
				saveString += list.get(i) + "\n";
			}
		}

		//Attempt to write to the file and close it
		try {
			saveFileWriter.write(saveString);
			saveFileWriter.close();

			//The file was successfully saved
			fileSaved = true;

			return;
		}
		catch(IOException e) {
			System.out.println("Error! File could not be saved!");
			return;
		}
	}

	@Override
	public void load(String filename) {

		String filepath = "/users/Joe/" + filename;
		File loadFile;
		Scanner scanner;

		//Attempt to create the scanner
		try {
			loadFile = new File(filepath);
			scanner = new Scanner(loadFile);
		}
		catch(Exception e) {
			System.out.println("Error! File could not be loaded!");
			return;
		}

		String tempCurrentLineS = "";
		String tempNumLinesS = "";

		int tempCurrentLine = 0;
		int tempNumLines = 0;

		int checksum = scanner.nextInt();

		if(checksum == 0x55) {

			list.clear();

			try {
				tempCurrentLineS = scanner.next();
				tempNumLinesS = scanner.next();

				if(tempCurrentLineS == null)
					return;

				else {
					try{
						tempCurrentLine = Integer.parseInt(tempCurrentLineS);
						tempNumLines = Integer.parseInt(tempNumLinesS);
					} 
					catch(Exception e){
						System.out.println("Error! File is corrupt. Reverting to previous save...");
						load(".viliteBackup");
						System.out.println("Done.");
						scanner.close();
						return;
					}
				}
			}
			catch(Exception e) {
				//Do I need to catch anything?
			}

			//If there is another entry...
			for(int i = 0; i < tempNumLines; i++) {
				String temp = scanner.next();
				insertEnd(temp);
			}

			currentLine = tempCurrentLine;
			numLines = tempNumLines;
			fileSaved = false;
		}
		else
			System.out.println("Error! Incorrect file  type!");

		//Close the file
		scanner.close();

		//Completed successfully
		return;
	}

	@Override
	public int findReplace(String str1, String str2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void moveCurrentLineTo(int lineNbr) {
		
		display(1, numLines);
		System.out.println("\n");
		
		String temp = list.get(currentLine);
		
		System.out.println(temp + "\n");

		if(lineNbr == currentLine)
			return;

		if(lineNbr == 1) {
			list.remove(1);
			display(1, numLines);
			System.out.println("\n");
			list.add(1, temp);
			display(1, numLines);
			System.out.println("\n");
			return;
		}

		if(lineNbr == numLines) {
			remove(1);
			insertEnd(temp);
			return;
		}

		remove(1);
		list.add(lineNbr, temp);
		return;
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

			if(i > numLines)
				return;

			if(list.get(i) != null) {
				if(i == currentLine)
					System.out.println("==> \t" + list.get(i));
				else
					System.out.println("\t" + list.get(i));
			}
		}
	}

	public void displayHelp() {
		System.out.println("The following are possible commands:\n" +
				"b <sentence> = insert line before the current line\n" + 
				"i <sentence> = insert after the current line\n" +
				"e <sentence> = insert after the last line\n" +
				"m # = move the current line down # positions\n" +
				"u # = move the current line up # positions\n" +
				"r = remove the current line\n" +
				"r # = remove # lines starting at the current line\n" +
				"s <filename> = save the contents to a text file\n" + 
				"l <filename> = load contents of a text file\n" +
				"d = display the buffer contents\n" +
				"d c = display current line\n" +
				"d # * = display line # to *(inclusive)\n" +
				"c = clear the buffer contents\n" +
				"h = show a help menu of editor commands\n" +
				"fr string1 string2 = find/search and replace\n" +
				"mc # = move current line to another location\n" +
				"mc $ = move current line to the last line\n" +
				"sw # = switch the current line with line #\n" +
				"sw $ = switch current line with last line\n" +
				"ud = undo operation\n" +
				"x = exit the editor (if saved)");
	}

	public static void main (String[] args) {
		ViLiteEditor vilite = new ViLiteEditor();

		while(true) {
			vilite.run();
		}
	}
}
