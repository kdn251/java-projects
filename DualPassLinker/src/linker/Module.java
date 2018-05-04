/* @author: Kevin Naughton Jr. */

package linker;

import java.util.ArrayList;

public class Module {
	//store array list of variable objects
	protected ArrayList<Variable> allVariables = new ArrayList<Variable>();
	
	//store uses of current module
	protected ArrayList<String> uses = new ArrayList<String>();
	
	//store boolean values to check if all elements in module's uses array list were referenced
	protected ArrayList<Integer> usesReferenced = new ArrayList<Integer>();
	
	//if all variables in list list are used
	protected int entireUseListUsed = 0;
	
	//store words of current module
	protected ArrayList<Integer> words = new ArrayList<Integer>();
	
	//store current module's length
	protected int length;
	
	//store the module's base address
	private int baseAddress = 0; 
	
	public Module() {
		//default constructor
	}
	
	public ArrayList<String> getUses() {
		return this.uses;
	}

	public void setBaseAddress(int baseAddress){
		this.baseAddress = baseAddress;
	}

	public int getBaseAddress(){
		return this.baseAddress;
	}
}
