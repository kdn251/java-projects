package project2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This program provides a tool that given a name of a directory,
 * explores all its sub-directories and files and does two things: 
 *  - computes the total size of all the files and sub-directories,
 *  - prints a list of n largest files (their sizes and absolute paths. 
 *  
 * @author Joanna Klukowska & Kevin Naughton Jr.
 *
 */
public class DirectorySize {
	
	/**list of files found in the directory structure */
	static List <FileOnDisk> listOfFiles ; 
	/**list of visited directories (kept to avoid 
	 * circular links and infinite recursion) */
	static List <String> listOfVisitedDirs;
	
	
	/** 
	 * This method expects one or two arguments. 
	 * @param args Array of arguments passed to the program. The first one 
	 * is the name of the directory to be explored. The second (optional) is the
	 * max number of largest files to be printed to the screen.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		
		//TODO:
		//check the number of command line arguments
		//terminate if not sufficient
		if(args.length == 1 || args.length == 2) {
			if(args[0] == null){
				//exit program gracefully
				System.out.println("Please provide a directory to be searched.");
				System.exit(1);
			}
		}
		else {
			System.out.println("Erro: please enter the correct amount of arguments (1 or 2).");
		}
		

		
		//TODO:
		// use directory name entered from the command line
		// verify if the directory is valid, terminate if not
		String directory = args[0];
		File dir = new File(directory);
		   // verification goes here
			if(dir.exists()) { //check if directory exists
				System.out.println("The provided directory has been found.");
			}
			else {
				System.out.println("The provided directory could not be found.");
				System.exit(1);
			}
			
		
		
		
		
		//create an empty list of files
		listOfFiles = new LinkedList<FileOnDisk> ();
		//create an empty list of directories
		listOfVisitedDirs = new ArrayList<String> ();
		
		// Display the total size of the directory/file
		long size = getSize( dir );
		if (size < 1024 ) //print bytes
			System.out.printf("Total space used: %7.2f bytes", 
					(float) size  );
		else if (size/1024 < 1024 )//print kilobytes
			System.out.printf("Total space used: %7.2f KB\n", 
					(float) size / 1024.0 );
		else if (size/1024/1024 < 1024 )//print megabytes
			System.out.printf("Total space used: %7.2f MB\n", 
					(float) size / (1024.0 * 1024));
		else //print gigabytes
			System.out.printf("Total space used: %7.2f GB\n", 
					(float) size / (1024.0 * 1024*1024));
		
		
		// Display the largest files in the directory
		int numOfFiles = 20; //default value
		try {
			if (args.length == 2 )  {
				numOfFiles = Integer.parseInt(args[1]);
			}
		}
		catch (NumberFormatException ex) {
			System.err.printf("ERROR: Invalid number of files provided." +
					"The second argument should be an integer. \n");
			System.exit(1);
		}
		
		Collections.sort(listOfFiles);
		
		
		
		
		
		for (int i = 0; i < listOfFiles.size() && i < numOfFiles; i++)
			//print from the back so that the largest files are printed
		    System.out.println(listOfFiles.get(listOfFiles.size() - i - 1));
		
	}


	/**
	 * Recursively determines the size of a directory or file represented 
	 * by the abstract parameter object file.   
	 * This method populates the listOfFiles with all the files contained in the
	 * explored directory. 
	 * This method populates the listOfVisitedDirs with canonical path names of 
	 * all the visited directories. 
	 * @param file directory/file name whose size should be determined
	 * @return number of bytes used for storage on disk
	 * @throws IOException 
	 */
	public static long getSize (File file) throws IOException   {
		long size = 0; // Store the total size of all files
		
		try {
			
			//TODO: implement this method
			//check if file parameter is a directory and if it has not yet been visited
			if(file.isDirectory() && (!listOfVisitedDirs.contains(file))) {
				//string array to store files within directory
				File[] allFiles = file.listFiles();
				//loop through files within directory
				for(int i = 0; i < allFiles.length; i++) {
					//add the size of the directory to the variable size
					size += getSize(allFiles[i]);
					//add current file to listOfVisitedDirs
					listOfVisitedDirs.add(allFiles[i].getCanonicalPath());
					//recursive call
					getSize(allFiles[i]);

				}
			}
			else { //the parameter file is a file
				size += file.length(); //METHOD TO USE MIGHT BE .length()
				//change current file object to FileOnDisk object
				FileOnDisk fileInElseBlock = new FileOnDisk(file.getCanonicalPath(), file.length());
				//add the new FileOnDisk object to listOfFiles
				listOfFiles.add(fileInElseBlock);
				
			}
		
		} catch (IOException e) {
			//DEAL WITH IOEXCEPTION
			System.out.println("Can't access given directory.");
		}
		
		return size;

	}
		
}