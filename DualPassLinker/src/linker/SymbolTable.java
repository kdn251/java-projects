/* @author: Kevin Naughton Jr. */
package linker;
import java.util.ArrayList;

public class SymbolTable {
	//Store every variable name in the symbol table
	protected ArrayList<String> variableNames = new ArrayList<String>();
	
	//Store every absolute addresses of all modules
	protected ArrayList<Integer> absoluteAddresses = new ArrayList<Integer>();
		
	//Store possible error messages for absolute addresses corresponding to index
	protected ArrayList<String> errorMessages = new ArrayList<String>();
	
	//store possible warning messages for absolute addresses corresponding to index
	protected ArrayList<String> warningMessages = new ArrayList<String>();
	
	public SymbolTable() {
		//default constructor
	}
}


