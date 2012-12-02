package vilite;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**********************************************************************
ViLiteEditor simulates a VI Editor (line editor). The program is able
to insert, delete, save, load, clear, display and edit a text file.  
Uses a current line indicator to display where in the editor the user
is currently working. Implements the functionality of Link Lists.

@author Joe Gibson, Ryan Zondervan
@version 12/5/2012
 *********************************************************************/
public class ViLiteEditor implements IEditor {

	/** Scanner for the command input */
	private Scanner input;

	/** String for the command input */
	private String command;

	/** Current line indicator */
	private int currentLine;

	/** Total number of lines */
	private int numLines;

	/** The linked list */
	private MyLinkedList list;

	/** Boolean value for whether or not the file is saved */
	private boolean fileSaved;
	
	/** ArrayList used for undo method */
	private ArrayList<Command> commandList;
	
	/** Index used for undo method */
	private int commandIndex;

	/******************************************************************
	Constructs a blank ViLiteEditor.
	******************************************************************/
	public ViLiteEditor() {
		input = new Scanner(System.in);
		command = null;
		currentLine = 0;
		numLines = 0;
		list = new MyLinkedList();
		fileSaved = false;
		commandList = new ArrayList<Command>();
		commandIndex = 0;
	}

	/******************************************************************
	Prompts the user for command input and processes the command.
	******************************************************************/
	public void run() {
		System.out.println("\nCommand:");
		command = input.nextLine();
		processCommand(command, false);
	}

	/******************************************************************
	Processes the given string into a command. Is called by either the
	run() method or the undo() method, hence the boolean condition.
	Also, this method is large because it checks input for errors before
	sending the command off to be performed. This way, each subsequent
	method can expect good input.
	@param the command string to be processed
	@param if it's being called by the undo method
	******************************************************************/
	public void processCommand(String command, boolean undo) {

		Scanner scanner = new Scanner(command);

		String cmd = null;
		String param1 = null;
		String param2 = null;

		try {
			cmd = scanner.next();
		} catch (NoSuchElementException e) {
			cmd = param1 = param2 = null;
			return;
		}

		// Check for b, i, and e, which are a command followed by a sentence

		if (cmd.equals("b")) {

			String sentence = null;

			try {
				sentence = scanner.nextLine();
			} catch (NoSuchElementException e) {
				System.out.println("Invalid parameter, try: b <sentence>");
				return;
			}

			if (list.isEmpty()) {
				insertEnd(sentence);
				currentLine++;
				if(!undo)
					saveCommand(commandIndex++, cmd, sentence, null);
			} else {
				insertBefore(sentence);
				if(!undo)
					saveCommand(commandIndex++, cmd, sentence, null);
				// No need to decrement currentLine
			}

			numLines++;
			fileSaved = false;

			return;
		}

		if (cmd.equals("i")) {

			String sentence = null;

			try {
				sentence = scanner.nextLine();
			} catch (NoSuchElementException e) {
				System.out.println("Invalid parameter, try: i <sentence>");
				return;
			}

			if (list.isEmpty()) {
				insertEnd(sentence);
				currentLine++;
				if(!undo)
					saveCommand(commandIndex++, cmd, sentence, null);
			} else {
				insertAfter(sentence);
				currentLine++;
				if(!undo)
					saveCommand(commandIndex++, cmd, sentence, null);
			}

			numLines++;
			fileSaved = false;

			return;
		}

		if (cmd.equals("e")) {

			String sentence = null;

			try {
				sentence = scanner.nextLine();
			} catch (NoSuchElementException e) {
				System.out.println("Invalid parameter, try: e <sentence>");
				return;
			}

			insertEnd(sentence);
			fileSaved = false;

			numLines++;
			currentLine = numLines;
			
			if(!undo)
				saveCommand(commandIndex++, cmd, sentence, null);
			
			return;
		}

		try {
			param1 = scanner.next();
		} catch (NoSuchElementException e) {
			param1 = param2 = null;
		}

		try {
			param2 = scanner.next();
		} catch (NoSuchElementException e) {
			param2 = null;
		}

		if (cmd.equals("m")) {
			if (param1 != null) {
				try {
					int num = Integer.parseInt(param1);
					moveDown(num);
					fileSaved = false;
					if(!undo)
						saveCommand(commandIndex++, cmd, param1, param2);
					return;

				} catch (NumberFormatException e) {
					System.out.println("Invalid parameters, try: m #");
					return;
				}
			}

			else {
				moveDown(1);
				if(!undo)
					saveCommand(commandIndex++, cmd, param1, param2);
				return;
			}
		}

		if (cmd.equals("u")) {
			if (param1 != null) {
				try {
					int num = Integer.parseInt(param1);
					moveUp(num);
					fileSaved = false;
					if(!undo)
						saveCommand(commandIndex++, cmd, param1, param2);
					return;

				} catch (NumberFormatException e) {
					System.out.println("Invalid parameters, try: u #");
					return;
				}
			}

			else {
				moveUp(1);
				if(!undo)
					saveCommand(commandIndex++, cmd, param1, param2);
				return;
			}
		}

		if (cmd.equals("r")) {

			if (!list.isEmpty()) {
				if (param1 != null) {
					try {
						int num = Integer.parseInt(param1);
						remove(num);
						fileSaved = false;
						if(!undo)
							saveCommand(commandIndex++, cmd, param1, param2);
						return;

					} catch (NumberFormatException e) {
						System.out.println("Invalid parameters, try: r #");
						return;
					}
				}

				else {
					remove(1);
					fileSaved = false;
					if(!undo)
						saveCommand(commandIndex++, cmd, param1, param2);
					return;
				}
			}

			else
				return;

		}

		if (cmd.equals("d")) {

			if (param1 == null && param2 == null) {
				display(1, numLines);
				return;
			}

			else {

				if (param1.equals("c") && param2 == null) {
					if (list.isEmpty())
						System.out.println("No data to display!");
					else
						System.out.println("==> \t" + list.get(currentLine));

					return;
				}

				try {
					int start = Integer.parseInt(param1);
					int end = Integer.parseInt(param2);

					if (start <= end)
						if (start > 0)
							display(start, end);
						else
							System.out
							.println("Invalid parameters, try: d # *, where # starts at '1'");
					else
						System.out
						.println("Invalid parameters, try: d # *, where * is greater than #");

					return;
				} catch (NumberFormatException e) {
					System.out.println("Invalid parameters, try: d # *");

					return;
				}

			}
		}

		if (cmd.equals("c")) {
			clear();
			fileSaved = false;
			numLines = 0;
			currentLine = 0;
			saveCommand(commandIndex++, cmd, param1, param2);
			return;
		}

		if (cmd.equals("s")) {
			if (param1 != null)
				save(param1);
			else
				System.out.println("Invalid parameters, try: s <filename>");

			return;
		}

		if (cmd.equals("l")) {

			// Make a save of the current list in case of corruption
			save(".viliteBackup");

			if (param1 != null) {
				load(param1);
				if(!undo)
					saveCommand(commandIndex++, cmd, param1, param2);
			}
			else
				System.out.println("Invalid parameters, try: l <filename>");

			return;
		}

		if (cmd.equals("h")) {
			displayHelp();
			return;
		}

		if (cmd.equals("x")) {
			// Check if saved before exiting
			if (list.isEmpty() || fileSaved) {
				System.out.println("Goodbye!");
				System.exit(0);
			} else {
				System.out
				.println("File not saved! Please save before closing");
				return;
			}
		}

		if (cmd.equals("mc")) {

			if(list.isEmpty()) {
				System.out.println("Empty list. There is nothing to move!");
				return;
			}

			if (param1 != null) {
				if (param1.equals("$")) {
					moveCurrentLineTo(numLines);
					fileSaved = false;
					if(!undo)
						saveCommand(commandIndex++, cmd, param1, param2);
					return;
				}

				else {
					try {
						int line = Integer.parseInt(param1);

						if (line <= 0 || line > numLines) {
							System.out
							.println("Invalid parameters, try: mc #, where # starts at '1'");
							return;
						} else {
							moveCurrentLineTo(line);
							fileSaved = false;
							if(!undo)
								saveCommand(commandIndex++, cmd, param1, param2);
							return;
						}
					} catch (NumberFormatException e) {
						System.out
						.println("Invalid parameters, try: mc # or mc $");
						return;
					}
				}
			} else {
				System.out.println("Invalid parameters, try: mc # or mc $");
				return;
			}
		}

		if (cmd.equals("fr")) {
			if(param1 != null && param2 != null) {
				int numFound = findReplace(param1, param2);
				
				if(!undo)
					saveCommand(commandIndex++, cmd, param1, param2);

				if(numFound > 0) {
					System.out.println("All " + numFound + " instances of " + param1 + " were replaced with " + param2);
					fileSaved = false;
					return;
				}

				if(numFound == 0) {
					System.out.println("No instances of " + param1 + " were found!");
					return;
				}
			}
			else {
				System.out.println("Invalid parameters, try: fr <string1> <string2>");
				return;
			}
		}

		if (cmd.equals("sw")) {

			if(list.isEmpty()) {
				System.out.println("Empty list. There is nothing to switch!");
				return;
			}

			if (param1 != null) {
				if (param1.equals("$")) {
					switchCurrentLineWith(numLines);
					fileSaved = false;
					if(!undo)
						saveCommand(commandIndex++, cmd, param1, param2);
					return;
				}

				else {
					try {
						int line = Integer.parseInt(param1);

						if (line <= 0 || line > numLines) {
							System.out
							.println("Invalid parameters, try: sw #, where # starts at '1'");
							return;
						} else {
							switchCurrentLineWith(line);
							fileSaved = false;
							if(!undo)
								saveCommand(commandIndex++, cmd, param1, param2);
							return;
						}
					} catch (NumberFormatException e) {
						System.out
						.println("Invalid parameters, try: sw # or sw $");
						return;
					}
				}
			} 

			else {
				System.out.println("Invalid parameters, try: sw # or sw $");
				return;
			}
		}

		if (cmd.equals("ud")) {
			if(commandIndex > 0)
				undo();
			return;
		}

		if (cmd != null)
			System.out.println("Command " + cmd
					+ " not recognized.  Type 'h' for help");
	}

	/******************************************************************
	Returns the number of lines in the file.
	@return the number of lines in the file
	******************************************************************/
	public int getNbrLines() {
		return numLines;
	}

	/******************************************************************
	Returns the current line number.
	@return the current line number 	
	******************************************************************/
	public int getCurrentLineNbr() {
		return currentLine;
	}

	/******************************************************************
	Returns the data at the specified line in the list.
	@param the line in the list
	@return the data at the specified line
	******************************************************************/
	public String getLine(int lineNbr) {
		return list.get(lineNbr);
	}

	/******************************************************************
	Returns an array of data from the specified range in the list.
	@param the starting line
	@param the ending line
	@return the array of data (Strings in this case)
	******************************************************************/
	public String[] getLines(int beginPos, int endPos) {
		String[] lines = { null };

		for (int i = beginPos; i <= endPos; i++)
			lines[i] = list.get(i);

		return lines;
	}

	/******************************************************************
	Inserts a line before the current line.
	@param the data to insert before the current line
	******************************************************************/
	public void insertBefore(String line) {
		list.add(currentLine, line);
	}

	/******************************************************************
	Inserts a line after the current line.
	@param the data to insert after the current line
	******************************************************************/
	public void insertAfter(String line) {
		list.add((currentLine + 1), line);
	}

	/******************************************************************
	Inserts a line at the end of the file.
	@param the data to insert at the end of the file
	******************************************************************/
	public void insertEnd(String line) {
		list.add(line);
	}

	/******************************************************************
	Moves the current line indicator up a given number of lines without
	manipulating the data in the list.
	@param the number of lines to move up
	******************************************************************/
	public void moveUp(int nbrOfPositions) {
		for (int i = 0; i < nbrOfPositions; i++)
			if (currentLine != 1)
				currentLine--;
	}

	/******************************************************************
	Moves the current line indicator down a given number of lines without
	manipulating the data in the list.
	@param the number of lines to move down
	******************************************************************/
	public void moveDown(int nbrOfPositions) {
		for (int i = 0; i < nbrOfPositions; i++)
			if (currentLine != numLines)
				currentLine++;
	}

	/******************************************************************
	Removes a given number of lines starting at the current line until
	reaching the end of the file (if applicable).
	@param the number of lines to remove
	******************************************************************/
	public void remove(int nbrLinesToRemove) {
		for (int i = 0; i < nbrLinesToRemove; i++) {
			if (currentLine == numLines) {
				list.remove(currentLine);
				currentLine--;
				numLines--;
				return;
			}

			else {
				list.remove(currentLine);
				numLines--;
			}
		}
	}

	/******************************************************************
	Completely clears the list.
	******************************************************************/
	public void clear() {
		list.clear();
	}

	/******************************************************************
	Saves the current list to a file given a filename. Writes a '0x55'
	to the first line so loaded files can be known to have been saved
	correctly.
	@param the name of the file which is to be saved
	******************************************************************/
	public void save(String filename) {
		String filepath = "/users/Joe/" + filename;
		File saveFile = new File(filepath);
		FileWriter saveFileWriter;

		// Attempt to create the file writer
		try {
			saveFileWriter = new FileWriter(saveFile);
		} catch (IOException e) {
			System.out.println("Error! File could not be saved!");
			return;
		}

		// Create the save string
		String saveString = "";

		// Populate the save string
		if (list.isEmpty()) {
			saveString += "" + 0x55 + "\n";
			saveString += "null";
		} else {
			saveString += "" + 0x55 + "\n";
			saveString += "" + currentLine + "\n";
			saveString += "" + numLines + "\n";

			for (int i = 1; i <= numLines; i++) {
				saveString += list.get(i) + "\n";
			}
		}

		// Attempt to write to the file and close it
		try {
			saveFileWriter.write(saveString);
			saveFileWriter.close();

			// The file was successfully saved
			fileSaved = true;

			return;
		} catch (IOException e) {
			System.out.println("Error! File could not be saved!");
			return;
		}
	}

	/******************************************************************
	Loads a file given the filename. Checks for the '0x55' header to
	verify the file was saved correctly, and checks for corrupt header
	data (currentLine, numLines). If corrupt data is found while
	loading the file, a save from immediately before the load operation
	is loaded in as a restore save.
	@param the name of the file which is to be loaded
	******************************************************************/
	public void load(String filename) {

		String filepath = "/users/Joe/" + filename;
		File loadFile;
		Scanner scanner;

		// Attempt to create the scanner
		try {
			loadFile = new File(filepath);
			scanner = new Scanner(loadFile);
		} catch (Exception e) {
			System.out.println("Error! File could not be loaded!");
			return;
		}

		String tempCurrentLineS = "";
		String tempNumLinesS = "";

		int tempCurrentLine = 0;
		int tempNumLines = 0;

		int checksum = scanner.nextInt();

		if (checksum == 0x55) {

			list.clear();

			try {
				tempCurrentLineS = scanner.next();
				tempNumLinesS = scanner.next();

				if (tempCurrentLineS == null)
					return;

				else {
					try {
						tempCurrentLine = Integer.parseInt(tempCurrentLineS);
						tempNumLines = Integer.parseInt(tempNumLinesS);
					} catch (Exception e) {
						System.out
						.println("Error! File is corrupt. Reverting to previous save...");
						load(".viliteBackup");
						System.out.println("Done.");
						scanner.close();
						return;
					}
				}
			} catch (Exception e) {
				// Do I need to catch anything?
			}

			// If there is another entry...
			for (int i = 0; i < tempNumLines; i++) {
				String temp = " " + scanner.next();
				insertEnd(temp);
			}

			currentLine = tempCurrentLine;
			numLines = tempNumLines;
			fileSaved = false;
		} else
			System.out.println("Error! Incorrect file  type!");

		// Close the file
		scanner.close();

		// Completed successfully
		return;
	}

	/******************************************************************
	Replaces all instances of a given string pattern with a second
	given string pattern.
	@param string pattern to be replaced
	@param string pattern to replace the previous
	******************************************************************/
	public int findReplace(String str1, String str2) {
		int numFound = 0;

		for(int i = 1; i <= numLines; i++) {
			if(list.get(i).contains(str1)) {
				int lastIndex = 0;
				while(lastIndex != -1) {
					lastIndex = list.get(i).indexOf(str1, lastIndex);
					if(lastIndex != -1) {
						numFound++;
						lastIndex += str1.length();
					}
				}

				String newLine = list.get(i).replaceAll(str1, str2);
				list.set(i, newLine);
			}
		}

		return numFound;
	}

	/******************************************************************
	Moves the current line to the specified line without modifying the
	current line indicator.
	@param the line number to move the current line to
	******************************************************************/
	public void moveCurrentLineTo(int lineNbr) {

		String temp = list.get(currentLine);

		if (lineNbr == currentLine)
			return;

		if (lineNbr == 1) {
			remove(1);
			numLines++;
			list.add(1, temp);
			return;
		}

		if (lineNbr == numLines) {
			remove(1);
			numLines++;
			list.add(numLines, temp);
			return;
		}

		boolean isLast = false;

		if(currentLine == numLines)
			isLast = true;

		remove(1);
		numLines++;
		list.add(lineNbr, temp);

		if(isLast)
			currentLine++;

		return;
	}

	/******************************************************************
	Switches the current line with the specified line without modifying
	the current line indicator.
	@param the line number to switch the current line with
	******************************************************************/
	public void switchCurrentLineWith(int lineNbr) {

		String destTemp = list.get(lineNbr);

		int curr = currentLine;

		if(lineNbr == currentLine)
			return;

		if(lineNbr < currentLine) {
			list.remove(lineNbr);
			currentLine --;
			moveCurrentLineTo(lineNbr);
			list.add(curr, destTemp);
			currentLine = curr;
			return;
		}

		else {
			list.remove(lineNbr);
			if(lineNbr == numLines)
				moveCurrentLineTo(lineNbr);
			else 
				moveCurrentLineTo(lineNbr - 1);
			list.add(curr, destTemp);
			currentLine = curr;
			return;
		}
	}

	/******************************************************************
	Undoes the last command (if the last command modified anything in
	the list) by using the saveList. Essentially this clears the current
	list and then executes all commands in that list until the last
	command.
	******************************************************************/
	public void undo() {
		
		clear();
		
		commandIndex--;
		
		int i = 0;
		
		while(i < commandIndex) {
			Command command = commandList.get(i);
			
			String cmd = command.getCmd();
			String param1 = command.getParam1();
			String param2 = command.getParam2();
			
			String cmdString = cmd;
			
			if(param1 != "-1") {
				cmdString += ((cmd.equals("e") || cmd.equals("i") || cmd.equals("b")) ? param1 : " " + param1);
				
				if(param2 != "-1")
					cmdString += " " + param2;
			}
			
			processCommand(cmdString, true);
			
			i++;
		}
	}

	/******************************************************************
	Displays each line of data from the list in the specified range.
	@param the first line that is displayed
	@param the last line that is displayed
	******************************************************************/
	private void display(int start, int end) {
		for (int i = start; i <= end; i++) {

			if (i > numLines)
				return;

			if (list.get(i) != null) {
				if (i == currentLine)
					System.out.println("==> \t" + list.get(i));
				else
					System.out.println("\t" + list.get(i));
			}
		}
	}

	/******************************************************************
	Displays a list of valid commands.
	******************************************************************/
	public void displayHelp() {
		System.out.println("The following are possible commands:\n"
				+ "b <sentence> = insert line before the current line\n"
				+ "i <sentence> = insert after the current line\n"
				+ "e <sentence> = insert after the last line\n"
				+ "m # = move the current line down # positions\n"
				+ "u # = move the current line up # positions\n"
				+ "r = remove the current line\n"
				+ "r # = remove # lines starting at the current line\n"
				+ "s <filename> = save the contents to a text file\n"
				+ "l <filename> = load contents of a text file\n"
				+ "d = display the buffer contents\n"
				+ "d c = display current line\n"
				+ "d # * = display line # to *(inclusive)\n"
				+ "c = clear the buffer contents\n"
				+ "h = show a help menu of editor commands\n"
				+ "fr <string1> <string2> = find and replace\n"
				+ "mc # = move current line to another location\n"
				+ "mc $ = move current line to the last line\n"
				+ "sw # = switch the current line with line #\n"
				+ "sw $ = switch current line with last line\n"
				+ "ud = undo operation\n" + "x = exit the editor (if saved)");
	}

	/******************************************************************
	Private helper method to save each command (if said command changes
	the data in the list) and it's parameters to an array to be used
	with the undo command.
	@param the index of the command
	@param the command
	@param the first parameter
	@param the second parameter
	******************************************************************/
	private void saveCommand(int index, String cmd, String param1, String param2) {
		String paramA;
		String paramB;
		
		if(param1 == null)
			paramA = "-1";
		else
			paramA = param1;
		
		if(param2 == null)
			paramB = "-1";
		else
			paramB = param2;
		
		Command command = new Command(commandIndex, cmd, paramA, paramB);
		
		commandList.add(command);
	}

	/******************************************************************
	The main method which runs until the exit command is issued or a
	fatal error occurrs.
	@param the line arguments
	******************************************************************/
	public static void main(String[] args) {
		ViLiteEditor vilite = new ViLiteEditor();

		while (true) {
			vilite.run();
		}
	}
}
