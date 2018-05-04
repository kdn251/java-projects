/* @author: Kevin Naughton Jr. */
package linker;

public class Variable {
	//store the module's symbol
	protected String symbol = "";
	
	//store the module's symbol location
	protected int symbolLocation = 0; //MAYBE CHANGE THIS BACK TO NO symbolLocation; IF IT CAUSES PROBLEMS/BUGS
	
	//store boolean as to whether this is the first definition of a specific variable
	protected boolean firstDefinition = true;
	
	//handles error where current variable is used but not defined
	protected boolean usedButNotDefined = false;
	
	//handles error where current variable is defined but not used
	protected boolean definedButNotUsed = false;
	
	public Variable() {
		//default constructor
	}
	
	public Variable(String symbol, int symbolLocation) {
		this.symbol = symbol;
		this.symbolLocation = symbolLocation;
	}
	
	public String getSymbol() {
		return this.symbol;
	}
	
	public int getSymbolLocation() {
		return this.symbolLocation;
	}
}
