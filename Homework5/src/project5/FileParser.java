package project5;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * FileParser class is used for parsing text files and retrieving
 * lists of alphabetic words stored in such file.
 * 
 * @author Joanna Klukowska
 * @version Mar 28, 2014 
 *
 */
public class FileParser {

	private Scanner input;
	
	/**
	 * Creates FileParser object given a string containing the name of 
	 * the input file to be parsed. 
	 * @param fileName
	 *    name of the input file to be parsed
	 * @throws IOException
	 *    if the input file cannot be opened for reading
	 */
	public FileParser ( String fileName ) throws IOException {
		File f = new File ( fileName );
		//if file does not exist, throw an exception
		if ( !f.exists() )
			throw new IOException (String.format("File: %s  does not exist.", fileName));
		//if file cannot be read 
		if ( !f.canRead() )
			throw new IOException (String.format("File: %s  cannot be accessed for reading.", fileName));
		//connect to the file using Scanner
		input = new Scanner( f );
		
	}
	
	/**
	 * Retrieves list of all words from the input file. 
	 * @return 
	 *    an array list containing all words from the input file with which the
	 *    File Parser object has been created
	 *    only alphabetic characters are included in the words
	 */
	public ArrayList <String> getAllWords ( ) {
		//create a new list to store all the words 
		ArrayList < String > listOfWords = new ArrayList<String> (5000); 
		
		while ( input.hasNext() ) {
			String nextWord = input.next();
			nextWord = stripNonLetters( nextWord.toLowerCase() ) ;
			if (nextWord.length() != 0 )
				listOfWords.add(  nextWord  );
		}
		return listOfWords;
	}
	
	/* 
	 * Removes any non-alphabetic characters from token word and 
	 * returns a modified word.
	 * @param word
	 *    token representing a potential word
	 * @return
	 *    "pure" words that contain only alphabetic characters
	 */
	private String stripNonLetters (String word ) {
		StringBuilder cleanWord = new StringBuilder() ;
		char current;
		for (int i = 0; i < word.length(); i++) {
			current = word.charAt(i);
			if (Character.isAlphabetic( current ) )
				cleanWord.append( current  );
			if (current == '\'') break;
		}
		return cleanWord.toString();
	}
	
	
	
}
