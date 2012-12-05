package vilite;

import java.io.IOException;

/**********************************************************************
IEditor.java implements the bulk of the program methods.  The methods
are to be overridden by the ViLiteEditor class.

@author Joe Gibson, Ryan Zondervan
@version 12/5/2012
**********************************************************************/
public interface IEditor {

	/* reads and processes each command until exit */
	void run();

	/* processes the given editor command */
	void processCommand(String command, boolean undo);

	/* returns number of lines in the editor buffer */
	int getNbrLines();

	/* returns the position of the current line */
	int getCurrentLineNbr();

	/* returns the line at the given line number */
	String getLine(int lineNbr);

	/* returns an array of lines from beginPos to endPos */
	String[] getLines(int beginPos, int endPos);

	/* inserts line before the current line */
	void insertBefore(String line);

	/* inserts line after the current line */
	void insertAfter(String line);

	/* inserts line as the last line */
	void insertEnd(String line);

	/* moves current line indicator up nbrOfPositions and returns new position */
	void moveUp(int nbrOfPositions);

	/* moves current line indicator down nbrOfPositions and returns new position */
	void moveDown(int nbrOfPositions);

	/* removes given number of lines from the current line */
	void remove(int nbrLinesToRemove);

	/* removes all lines in the buffer */
	void clear();

	/* saves contents to the file with filename */
	void save(String filename) throws IOException;

	/* clears the current contents and loads the contents of the specified file */
	void load(String filename) throws IOException;

	/* finds and replaces all occurrences of str1 with str2 and return the count */
	int findReplace(String str1, String str2);

	/* moves the current line to the lineNbr */
	void moveCurrentLineTo(int lineNbr);

	/* switches the current line with the lineNbr */
	void switchCurrentLineWith(int lineNbr);

	/* undo the effects of the last edit operation */
	void undo();

}