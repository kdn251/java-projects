package project2;

import java.io.File;

/**
 * An abstract representation of file on disk.
 * @author Kevin Naughton Jr.
 *
 */

public class FileOnDisk implements Comparable<FileOnDisk> {
	//data fields for a FileOnDisk object
	private String directory;
	private long numberOfFiles;
	private String filePath;
	private long size;
	
	/**
	 * Creates a new FileOnDisk object
	 * @param pathName - directory name
	 * @param size - number of largest files within the directory to return
	 */
	public FileOnDisk(String enteredDirectory, long size) {
		this.directory = enteredDirectory;
		this.size = size;
		
	}
	
	/**
	 * Compares two FileOnDisk objects based on their size.
	 * if files are same size order is alphabetical
	 * @param arg0 - file to compare
	 * @return -1 - if current file's size < arg0 size
	 * @return 1 - if current file's size > arg0 size
	 */
	@Override
	public int compareTo(FileOnDisk arg0) {
		if(this.size > arg0.size) {
			return 1;
		}
		else if(this.size < arg0.size) {
			return -1;
		}
		else if(this.size == arg0.size) {
			if(this.directory.compareTo(arg0.directory) == 1) {
				return 1; 
			}
			else {
				return -1;
			}
		}
		return 0;
	}
	
	/**
	 * Returns the absolute path for the file represented by this object.
	 * @return Returns the size for the file represented by this object
	 */
	public String getAbsPath() {
		return this.filePath;
	}
	
	/**
	 * Returns the size for the file represented by this object
	 * @return Returns the size for the file represented by this object
	 */
	public long getSize() {
		return this.numberOfFiles;
	
	}
	
	/**
	 * Produces string representation of the object.
	 * @return file's size and file's full path name
	 */
	@Override
	public String toString() {
		// Display the total size of the directory/file
		if (size < 1024 ) //print bytes
			return String.format("%7.2f bytes", 
					(float) size  ) + "    " + this.directory;
		else if (size/1024 < 1024 )//print kilobytes
			return String.format("%7.2f KB", 
					(float) size / 1024.0 ) + "    " + this.directory;
		else if (size/1024/1024 < 1024 )//print megabytes
			return String.format("%7.2f MB", 
					(float) size / (1024.0 * 1024)) + "    " + this.directory;
		else //print gigabytes
			return String.format("%7.2f GB", 
					(float) size / (1024.0 * 1024*1024)) + "    " + this.directory;
	}
	


}
