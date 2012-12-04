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
		//Initialize all properties
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
		
		//False indicates that the command is NOT being called from the
		//undo method
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

		//Create the scanner to parse the command string
		Scanner scanner = new Scanner(command);

		//Initialize commands and parameters
		String cmd = null;
		String param1 = null;
		String param2 = null;

		//Attempt to parse the command
		//Set all to null on exception
		try {
			cmd = scanner.next();
		} catch (NoSuchElementException e) {
			cmd = param1 = param2 = null;
			return;
		}

		// Check for b, i, and e, which are a command followed by a sentence
		if (cmd.equals("b")) {
			
			String sentence = null;

			//Attempt to parse the sentence
			try {
				sentence = scanner.nextLine();
			} catch (NoSuchElementException e) {
				System.out.println("Invalid parameter, try: b <sentence>");
				return;
			}

			//Insert the sentence in the proper position and update
			//the current line indicator
			if (list.isEmpty()) {
				insertEnd(sentence);
				currentLine++;
				if(!undo)
					saveCommand(commandIndex++, cmd, sentence, null);
			} else {
				insertBefore(sentence);
				if(!undo)
					saveCommand(commandIndex++, cmd, sentence, null);
				// No need to decrement currentLine here
			}

			//Update the number of lines
			numLines++;
			
			//The file had been edited
			fileSaved = false;
			return;
		}

		if (cmd.equals("i")) {

			String sentence = null;

			//Attempt to parse the sentence
			try {
				sentence = scanner.nextLine();
			} catch (NoSuchElementException e) {
				System.out.println("Invalid parameter, try: i <sentence>");
				return;
			}

			//Insert the sentence in the proper position and update
			//the current line indicator
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

			//Increment the number of lines
			numLines++;
			
			//The file has been edited
			fileSaved = false;
			return;
		}

		if (cmd.equals("e")) {

			String sentence = null;

			//Attempt to parse the sentence
			try {
				sentence = scanner.nextLine();
			} catch (NoSuchElementException e) {
				System.out.println("Invalid parameter, try: e <sentence>");
				return;
			}

			//Insert the sentence at the proper position
			insertEnd(sentence);

			//Increment the number of lines
			numLines++;
			
			//Set the current line to the last line
			currentLine = numLines;
			
			//The file has been edited
			fileSaved = false;
			
			if(!undo)
				saveCommand(commandIndex++, cmd, sentence, null);			
			return;
		}

		
		//Attempt to parse the first parameter (if it exists)
		try {
			param1 = scanner.next();
		} catch (NoSuchElementException e) {
			param1 = param2 = null;
		}
		
		//Attempt to parse the second parameter (if it exists)
		try {
			param2 = scanner.next();
		} catch (NoSuchElementException e) {
			param2 = null;
		}

		//'m' or 'm #' command moves the current line indicator down
		if (cmd.equals("m")) {
			
			//Move down a number of times
			if (param1 != null) {
				
				//Attempt to parse the parameter
				try {
					int num = Integer.parseInt(param1);
					
					//Move the current line indicator down num times
					moveDown(num);
					
					//The file has been edited
					fileSaved = false;
					
					if(!undo)
						saveCommand(commandIndex++, cmd, param1, param2);				
					return;

				} catch (NumberFormatException e) {
					System.out.println("Invalid parameters, try: m #");
					return;
				}
			}

			//Move down once
			else {
				
				//Move the current line indicator down one line
				moveDown(1);
				
				//The file has been edited
				fileSaved = false;
				
				if(!undo)
					saveCommand(commandIndex++, cmd, param1, param2);			
				return;
			}
		}

		
		//'u' or 'u #' command moves the current line indicator up
		if (cmd.equals("u")) {
			
			//Move up a number of times
			if (param1 != null) {
				
				//Attempt to parse the parameter
				try {
					int num = Integer.parseInt(param1);
					
					//Move up num times
					moveUp(num);
					
					//The file has been edited
					fileSaved = false;
					
					if(!undo)
						saveCommand(commandIndex++, cmd, param1, param2);					
					return;

				} catch (NumberFormatException e) {
					System.out.println("Invalid parameters, try: u #");
					return;
				}
			}

			//Move up once
			else {
				
				//Move up one time
				moveUp(1);
				
				//The file has been edited
				fileSaved = false;
				
				if(!undo)
					saveCommand(commandIndex++, cmd, param1, param2);				
				return;
			}
		}

		//'r' or 'r #' removes a number of lines starting at the 
		//current line
		if (cmd.equals("r")) {

			//If the list is not empty...
			if (!list.isEmpty()) {
				
				//Remove a number of lines
				if (param1 != null) {
					
					//Attempt to parse the parameter
					try {
						int num = Integer.parseInt(param1);
						
						//Remove num lines
						remove(num);
						
						//The file has been edited
						fileSaved = false;
						
						if(!undo)
							saveCommand(commandIndex++, cmd, param1, param2);				
						return;

					} catch (NumberFormatException e) {
						System.out.println("Invalid parameters, try: r #");
						return;
					}
				}

				//Remove one line
				else {
					
					//Remove one line
					remove(1);
					
					//The file has been edited
					fileSaved = false;
					
					if(!undo)
						saveCommand(commandIndex++, cmd, param1, param2);
					return;
				}
			}

			//Do nothing if file is empty
			else
				return;
		}

		//'d', 'd c' or 'd # *' displays all or part of the file
		if (cmd.equals("d")) {

			//'d' displays the entire file
			if (param1 == null && param2 == null) {
				display(1, numLines);
				return;
			}

			else {

				//'d c' displays only the current line
				if (param1.equals("c") && param2 == null) {
					
					//Display that the list is empty or display the
					//current line if it exists
					if (list.isEmpty())
						System.out.println("No data to display!");
					else
						System.out.println("==> \t" + list.get(currentLine));
					
					return;
				}

				//Attempt to parse the parameters
				try {
					int start = Integer.parseInt(param1);
					int end = Integer.parseInt(param2);

					//Ensure good data...
					if (start <= end)
						if (start > 0)
							
							//If all is good, display the specified lines
							display(start, end);
						else
							System.out.println("Invalid parameters, " +
									"try: d # *, where # starts at '1'");
					else
						System.out
						.println("Invalid parameters, " +
								"try: d # *, where * is greater than #");
					return;
				} catch (NumberFormatException e) {
					System.out.println("Invalid parameters, try: d # *");
					return;
				}
			}
		}

		//'c' command clears the file
		if (cmd.equals("c")) {
			
			//Clear the list
			clear();
			
			//The file has been edited
			fileSaved = false;
			
			if(!undo)
				saveCommand(commandIndex++, cmd, param1, param2);
			return;
		}

		//'s' command saves the file
		if (cmd.equals("s")) {
			
			//If given a filename...
			if (param1 != null)
				
				//Save the file
				save(param1);
			else
				System.out.println("Invalid parameters, try: s <filename>");

			return;
		}

		//'l' command loads a file
		if (cmd.equals("l")) {

			// Make a save of the current list in case of corruption
			save(".viliteBackup");

			//If given a filename...
			if (param1 != null) {
				
				//Load the file
				load(param1);
				
				if(!undo)
					saveCommand(commandIndex++, cmd, param1, param2);
			}
			else
				System.out.println("Invalid parameters, try: l <filename>");

			return;
		}

		//'h' command displays a list of commands
		if (cmd.equals("h")) {
			
			//Display the list of commands
			displayHelp();
			return;
		}

		//'x' exits if the file is saved
		if (cmd.equals("x")) {
			
			//If saved...
			if (list.isEmpty() || fileSaved) {
				
				//Exit
				System.out.println("Goodbye!");
				System.exit(0);
			} 
			
			//If not saved...
			else {
				System.out.println("File not saved! " +
						"Please save before closing");
				return;
			}
		}

		//'mc #' or 'mc $' moves the current line indicator to
		//a given position
		if (cmd.equals("mc")) {

			//For an empty list...
			if(list.isEmpty()) {
				System.out.println("Empty list. There is nothing to move!");
				return;
			}

			if (param1 != null) {
				
				//If given 'mc $' move to last line
				if (param1.equals("$")) {
					
					//Move the current line to the last line
					moveCurrentLineTo(numLines);
					
					//The file has been edited
					fileSaved = false;
					
					if(!undo)
						saveCommand(commandIndex++, cmd, param1, param2);
					return;
				}

				//Move to given line
				else {
					
					//Attempt to parse the parameter
					try {
						int line = Integer.parseInt(param1);

						//Ensure that it is a valid line
						if (line <= 0 || line > numLines) {
							System.out.println("Invalid parameters, " +
									"try: mc #, where # starts at '1'");
							return;
						} 
						
						else {
							
							//Move the current line to the given line
							moveCurrentLineTo(line);
							
							//The file has been edited
							fileSaved = false;
							
							if(!undo)
								saveCommand(commandIndex++, cmd, param1, param2);
							return;
						}
					} catch (NumberFormatException e) {
						System.out.println("Invalid parameters, " +
								"try: mc # or mc $");
						return;
					}
				}
			} 
			
			//If given no parameter
			else {
				System.out.println("Invalid parameters, " +
						"try: mc # or mc $");
				return;
			}
		}

		//'fr <string> <string>' replaces all instances of the first
		//string with the second string
		if (cmd.equals("fr")) {
			
			//If given two strings
			if(param1 != null && param2 != null) {
				
				//Replace all instances of param1 with param2
				//and store how many changes were made
				int numFound = findReplace(param1, param2);
				
				if(!undo)
					saveCommand(commandIndex++, cmd, param1, param2);

				//Display how many instances were replaced
				if(numFound > 0) {
					System.out.println("All " + numFound + " instances of "
							+ param1 + " were replaced with " + param2);
					
					//The file has been edited
					fileSaved = false;
					return;
				}

				//Nothing was replaced
				if(numFound == 0) {
					System.out.println("No instances of " + 
							param1 + " were found!");
					return;
				}
			}
			
			else {
				System.out.println("Invalid parameters, " +
						"try: fr <string1> <string2>");
				return;
			}
		}

		//'sw #' or 'sw $' switches the current line with the given line
		if (cmd.equals("sw")) {

			//The list is empty...
			if(list.isEmpty()) {
				System.out.println("Empty list. " +
						"There is nothing to switch!");
				return;
			}

			//If given a valid parameter
			if (param1 != null) {
				
				//'sw $' switches with the last line
				if (param1.equals("$")) {
					
					//Switch with the last line
					switchCurrentLineWith(numLines);
					
					//The file has been edited
					fileSaved = false;
					
					if(!undo)
						saveCommand(commandIndex++, cmd, param1, param2);
					return;
				}

				//'sw #' switches with the given line
				else {
					
					//Attempt to parse the parameter
					try {
						int line = Integer.parseInt(param1);

						//Ensure the line exists
						if (line <= 0 || line > numLines) {
							System.out.println("Invalid parameters, try: " +
									"sw #, where # starts at '1'");
							return;
						} 
						
						else {
							
							//Switch with the given line
							switchCurrentLineWith(line);
							
							//The file has been edited
							fileSaved = false;
							
							if(!undo)
								saveCommand(commandIndex++, cmd, param1, param2);
							return;
						}
					} catch (NumberFormatException e) {
						System.out.println("Invalid parameters, " +
								"try: sw # or sw $");
						return;
					}
				}
			} 

			else {
				System.out.println("Invalid parameters, try: sw # or sw $");
				return;
			}
		}

		//'ud' undoes the last command
		if (cmd.equals("ud")) {
			
			//If any previous commands have been executed...
			if(commandIndex > 0)
				
				//Undo the last command
				undo();
			return;
		}

		//The command was not recognized...
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
			
			//Stop at the top of the list
			if (currentLine != 1 && !list.isEmpty())
				currentLine--;
	}

	/******************************************************************
	Moves the current line indicator down a given number of lines without
	manipulating the data in the list.
	@param the number of lines to move down
	******************************************************************/
	public void moveDown(int nbrOfPositions) {
		for (int i = 0; i < nbrOfPositions; i++)
			
			//Stop at the bottom of the list
			if (currentLine != numLines && !list.isEmpty())
				currentLine++;
	}

	/******************************************************************
	Removes a given number of lines starting at the current line until
	reaching the end of the file and then continues removing lines
	above (if applicable).
	@param the number of lines to remove
	******************************************************************/
	public void remove(int nbrLinesToRemove) {
		for (int i = 0; i < nbrLinesToRemove; i++) {
			
			//If at the end of the list start removing lines above
			if (currentLine == numLines) {
				
				//Remove the current line
				list.remove(currentLine);
				
				//Decrement both the current line indicator and the
				//number of lines
				currentLine--;
				numLines--;
				return;
			}

			//Remove the element and decrement the number of lines
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
		
		//Clear the list
		list.clear();
		
		//Set both the number of lines and the current line indicator
		//to zero
		numLines = 0;
		currentLine = 0;
	}

	/******************************************************************
	Saves the current list to a file given a filename. Writes a '0x55'
	to the first line so loaded files can be known to have been saved
	correctly.
	@param the name of the file which is to be saved
	******************************************************************/
	public void save(String filename) {
		
		//Create the given filepath in the user's directory
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

		//Initialize temp variables
		String tempCurrentLineS = "";
		String tempNumLinesS = "";

		int tempCurrentLine = 0;
		int tempNumLines = 0;

		//Get the checksum
		int checksum = scanner.nextInt();
		
		//Verify the checksum
		if (checksum == 0x55) {

			//Clear the list
			list.clear();

			//Attempt to parse the current line and number of lines
			try {
				tempCurrentLineS = scanner.next();
				tempNumLinesS = scanner.next();

				//The file was saved as an empty file
				if (tempCurrentLineS == null)
					return;

				else {
					
					//Attempt to parse the current line and number of lines
					try {
						tempCurrentLine = Integer.parseInt(tempCurrentLineS);
						tempNumLines = Integer.parseInt(tempNumLinesS);
					} catch (Exception e) {
						
						//In case of corrput data, load in the previous
						//save!
						System.out.println("Error! File is corrupt. " +
								"Reverting to previous save...");
						
						//Load the previous save
						load(".viliteBackup");
						
						System.out.println("Done.");
						
						//Close the scanner
						scanner.close();
						return;
					}
				}
			} catch (Exception e) {
				//Do I need to catch anything?
			}

			// If there is another entry...
			for (int i = 0; i < tempNumLines; i++) {
				
				//Add a space to the data... just for appearances
				String temp = " " + scanner.next();
				
				//Add the new data
				insertEnd(temp);
			}

			//Set the properties to the temp values
			currentLine = tempCurrentLine;
			numLines = tempNumLines;
			
			//The file has been edited
			fileSaved = false;
		} 
		
		else
			System.out.println("Error! Incorrect file type!");

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
		
		//Nothing found yet...
		int numFound = 0;

		for(int i = 1; i <= numLines; i++) {
			
			//If the line contains the search string...
			if(list.get(i).contains(str1)) {
				
				//The last index of the search string
				int lastIndex = 0;
				
				//Updates the number found (in case of multiple words
				//being found and replaced in one line)
				while(lastIndex != -1) {
					
					//Get the last index of the search string starting
					//at the last, last index
					lastIndex = list.get(i).indexOf(str1, lastIndex);
					
					if(lastIndex != -1) {
						
						//We got another one!
						numFound++;
						
						//Increment the last index
						lastIndex += str1.length();
					}
				}

				//Replace the old string with the new one
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
