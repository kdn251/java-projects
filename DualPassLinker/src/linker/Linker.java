/* @author: Kevin Naughton Jr. */

package linker;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Linker {
	public static void main(String[] args) {
		//declare scanner
		Scanner scanner;
		try {
			//initialize scanner to try and read file
			scanner = new Scanner(new File(args[0]));
		}
		catch (Exception allExceptions) {
			//catch except and exit gracefully
			scanner = null;
			System.out.println("Please enter a file name via the command line.");
			System.exit(-1);
		}
		
		//initialize an array list of Modules
		ArrayList<String> entireFile = new ArrayList<String>();
		
		//add all tokens of the file to entire file variable
        while(scanner.hasNext()) {
            entireFile.add(scanner.next());
        }

        //create array to hold all modules
        Module[] allModules = new Module[Integer.parseInt(entireFile.get(0))];
        for(int i = 0; i < allModules.length; i++) {
            //create new module object
            Module newModule = new Module();

            //assign new module to ith index of all modules array
            allModules[i] = newModule;
        }

        SymbolTable symbolTable = new SymbolTable();

        //call first pass function
        firstPass(entireFile, allModules, symbolTable);

        //print symbol table
        printSymbolTable(allModules);

        System.out.println();

        //print memory map
        secondPass(allModules, symbolTable);

        for(int i = 0; i < symbolTable.absoluteAddresses.size(); i++) {
            System.out.println(i + ":" + "   " + symbolTable.absoluteAddresses.get(i));
        }

        System.out.println();
        for(int i = 0; i < symbolTable.warningMessages.size(); i++) {
            System.out.println(symbolTable.warningMessages.get(i));
        }

        System.out.println();
        for(int i = 0; i < symbolTable.errorMessages.size(); i++) {
            System.out.println(symbolTable.errorMessages.get(i));
        }		
    }

    private static void firstPass(ArrayList<String> entireFile, Module[] allModules, SymbolTable symbolTable) {
        //track line number of file
        int lineNumber = 0;		

        //track index of all modules' definition lines
        int allModulesDefIndex = 0;

        //track index of all modules' use lines
        int allModulesUseIndex = 0;

        //track index of all modules' program lines
        int allModulesProgramIndex = 0;

        //keep track of base address while iterating through modules
        int baseAddress = 0;

        //iterate through entire file
        for(int i = 1; i < entireFile.size() - 1; i ++) {
            if(Integer.parseInt(entireFile.get(i)) == 0) {
                //if i is 0 continue to next non zero element
                while(Integer.parseInt(entireFile.get(i)) == 0) {
                    i++;
                    if(lineNumber % 3 == 0) {
                        allModulesDefIndex++;
                    }
                    else if(lineNumber % 3 == 1) {
                        allModulesUseIndex++;
                    }
                    else {
                        allModulesProgramIndex++;
                    }

                    lineNumber++;
                }
            }

            if(Integer.parseInt(entireFile.get(i)) != 0)  {
                if(lineNumber % 3 == 0) {
                    int iterationLength = Integer.parseInt(entireFile.get(i)) * 2;
                    for(int l = 0; l < iterationLength / 2; l++) {
                        Variable variable = new Variable();
                        allModules[allModulesDefIndex].allVariables.add(variable);
                    }

                    int count = 0;
                    int currentVariable = 0;
                    for(int k = 0; k < iterationLength; k++) {
                        if(k % 2 == 0) {
                            i++;
                            boolean multiplyDefined = false;
                            String multiplyDefinedVariable = null;
                            //check if the current variable has been previously defined
                            for(int checkDefined = 0; checkDefined <= allModulesDefIndex; checkDefined++) {
                                for(int duplicate = 0; duplicate < allModules[checkDefined].allVariables.size(); duplicate++) {
                                    if(entireFile.get(i).equals(allModules[checkDefined].allVariables.get(duplicate).symbol)) {
                                        multiplyDefined = true;
                                        multiplyDefinedVariable = entireFile.get(i);
                                        symbolTable.errorMessages.add("Error: " + multiplyDefinedVariable + " is multiply defined; first value used");
                                    }
                                }
                            }

                            //if the current variable has not previously been defined add it to the symbol table
                            if(multiplyDefined == false) {
                                //add current variable to symbol table's array list of variable names
                                symbolTable.variableNames.add(entireFile.get(i));
                                allModules[allModulesDefIndex].allVariables.get(currentVariable).symbol = entireFile.get(i);
                                count++;
                            }
                            //if the current variable is not multiply defined
                            else {
                                i+=2;
                                count = 0;
                            }
                        }

                        if(k % 2 == 1) {
                            i++;
                            allModules[allModulesDefIndex].allVariables.get(currentVariable).symbolLocation = Integer.parseInt(entireFile.get(i)) + baseAddress;
                            count++;
                        }
                        if(count == 2) {
                            currentVariable++;
                            count = 0;
                        }
                    }

                    //increment module index
                    allModulesDefIndex++;

                    //increment line number
                    lineNumber++;
                }
                //current line is a use line of a module
                else if((lineNumber % 3) == 1) {
                    int numberOfUses = Integer.parseInt(entireFile.get(i));
                    for(int j = 0; j < numberOfUses; j++) {
                        i++;
                        allModules[allModulesUseIndex].uses.add(entireFile.get(i));
                    }

                    //increment index to next module
                    allModulesUseIndex++;

                    //increment line number
                    lineNumber++;
                }

                //process the program line of the current module
                else {
                    allModules[allModulesProgramIndex].setBaseAddress(baseAddress);

                    baseAddress = Integer.parseInt(entireFile.get(i)) + baseAddress;

                    //set the current module's length
                    allModules[allModulesProgramIndex].length = Integer.parseInt(entireFile.get(i));

                    int lengthOfWord = 5;

                    //if module length is 1 add the word and move to next line
                    if(Integer.parseInt(entireFile.get(i)) == 1) {;
                        i++;
                        if(entireFile.get(i).length() != lengthOfWord) {
                            //continue
                        }
                        else {
                            allModules[allModulesProgramIndex].words.add(Integer.parseInt(entireFile.get(i)));
                        }
                    }

                    //module has multiple words
                    else {
                        int numberOfSkips = Integer.parseInt(entireFile.get(i)) - 1;
                        for(int j = 0; j < numberOfSkips + 1; j++) {
                            //do not iterate outside of file bounds
                            if(i <= entireFile.size() - 2) {
                                i++;
                                //add current word to current module's word array list
                                if(entireFile.get(i).length() == lengthOfWord) {
                                    allModules[allModulesProgramIndex].words.add(Integer.parseInt(entireFile.get(i)));
                                }
                                else {
                                    j = numberOfSkips + 1;
                                    i--;

                                }
                            }
                        }
                    }

                    //increment index to next module
                    allModulesProgramIndex++;

                    //increment current line number
                    lineNumber++;
                }
            }
        }
        
        //check if the current use has been previously defined
        for(int i = allModules.length - 1; i > 0; i--) {
            for(int j = allModules[i].uses.size() - 1; j > 0; j--) {
                String currentSymbolUsed = allModules[i].uses.get(j);
                int useCount = 0;
                for(int k = 0; k < allModules.length; k++) {
                    for(int l = 0; l < allModules[k].allVariables.size(); l++) {
                        if(currentSymbolUsed.equals(allModules[k].allVariables.get(l).symbol)) {
                            useCount++;
                        }
                    }
                }

                if(useCount == 0) {
                    symbolTable.warningMessages.add("Error: " + currentSymbolUsed + " is not defined; zero used.");
                }
            }
        }	

        //check if any variables are defined but not used
        for(int i = 0; i < allModules.length; i++) {
            for(int j = 0; j < allModules[i].allVariables.size(); j++) {
                Variable currentSymbol = allModules[i].allVariables.get(j);
                int useCount = 0;
                for(int k = 0; k < allModules.length; k++) {
                    for(int l = 0; l < allModules[k].uses.size(); l++) {
                        if(currentSymbol.symbol.equals(allModules[k].uses.get(l))) {
                            useCount++;
                        }
                    }
                }

                if(useCount == 0) {
                    symbolTable.warningMessages.add("Warning: " + currentSymbol.symbol + " was defined in module " + i + " but was never used.");

                }
            }
        }
    }


    private static void printSymbolTable(Module[] allModules) {
        System.out.println("Symbol Table");
        for(int i = 0; i < allModules.length; i++) {
            for(int j = 0; j < allModules[i].allVariables.size(); j++) {
                if(allModules[i].allVariables.get(j).symbol != "") {
                    System.out.println(allModules[i].allVariables.get(j).symbol + " = " + allModules[i].allVariables.get(j).symbolLocation);
                }
            }
        }	
    }

    private static SymbolTable secondPass(Module[] allModules, SymbolTable symbolTable) {
        System.out.println("Memory Map");
        //check any a variable location exceeds the size of its respective module
        for(int i = 0; i < allModules.length; i++) {
            for(int j = 0; j < allModules[i].allVariables.size(); j++) {
                if(allModules[i].allVariables.get(j).symbolLocation - allModules[i].getBaseAddress() > allModules[i].length) {
                    //System.out.println("Current module's length is: " + allModules[i].length + " current symbol's location is: " + (allModules[i].allVariables.get(j).symbolLocation - allModules[i].getBaseAddress()));
                    symbolTable.errorMessages.add("Error: " + allModules[i].allVariables.get(j).symbol + " exceeds the size of the module; address given as 0 (relative).");
                    allModules[i].allVariables.get(j).symbolLocation = allModules[i].getBaseAddress();

                }
            }
        }

        //length of machine
        int lengthOfMachine = 600;

        //length of absolute address
        int lengthOfAddress = 4;

        //length of address of word
        int lengthOfAddressOfWord = 4;

        //for every module
        for(int i = 0; i < allModules.length; i++) {
            //for every word in current module
            for(int j = 0; j < allModules[i].words.size(); j++) {
                String currentWord = Integer.toString(allModules[i].words.get(j));
                char lastDigit = currentWord.charAt(currentWord.length() - 1);
                //if address is an immediate address or absolute address
                if(lastDigit == '1' || lastDigit == '2') {
                    //System.out.println("AFTER IF WORD IS  " + currentWord);
                    String absoluteAddressString = "";
                    String relativeAddressString = "";
                    String correctedAbsoluteAddressString = "";
                    //calculate address
                    for(int k = 0; k < lengthOfAddress; k++) {
                        absoluteAddressString += currentWord.charAt(k);
                        if(k == 0) {
                            correctedAbsoluteAddressString += currentWord.charAt(k);
                        }
                        if(k != 0) {
                            relativeAddressString += currentWord.charAt(k);
                        }
                    }

                    //If an absolute address exceeds the size of the machine, print an error message and use the value zero
                    if(lastDigit == '2' && Integer.parseInt(relativeAddressString) > 600) {
                        //add error message to symbol table
                        symbolTable.errorMessages.add("Error: Absolute  address exceeds machine size; zero used.");

                        //add corrected absolute address to symbol table
                        symbolTable.absoluteAddresses.add(Integer.parseInt(correctedAbsoluteAddressString + "000"));
                    }
                    else {
                        int absoluteAddressInteger = Integer.parseInt(absoluteAddressString);
                        symbolTable.absoluteAddresses.add(absoluteAddressInteger);
                    }
                }

                //if address is a relative address
                else if(lastDigit == '3') {
                    String absoluteAddressString = "";
                    String relativeAddressString = "";
                    String correctedAbsoluteAddressString = "";

                    //calculate address
                    for(int k = 0; k < lengthOfAddress; k++) {
                        absoluteAddressString += currentWord.charAt(k);
                        if(k == 0) {
                            correctedAbsoluteAddressString += currentWord.charAt(k);
                        }
                        if(k != 0) {
                            relativeAddressString += currentWord.charAt(k);
                        }
                    }

                    //If a relative address exceeds the size of the module, print an error message and use the value zero
                    if(Integer.parseInt(relativeAddressString) > allModules[i].length) {
                        //add error message to symbol table
                        symbolTable.errorMessages.add("Error: Relative  address exceeds module size; zero used.");

                        //add corrected absolute address to symbol table
                        symbolTable.absoluteAddresses.add(Integer.parseInt(correctedAbsoluteAddressString + "000"));
                    }
                    else {
                        int absoluteAddressInteger = Integer.parseInt(absoluteAddressString) + allModules[i].getBaseAddress();
                        symbolTable.absoluteAddresses.add(absoluteAddressInteger);
                    }
                }

                //address is an external address
                else {
                    String index = "";
                    String address = "";
                    String correctedAbsoluteAddressString = "";
                    String largerThanUseListString = "";
                    String externalReference = "";
                    boolean found = false;
                    boolean largerThanUseLength = false;

                    for(int k = 1; k < lengthOfAddressOfWord; k++) {
                        index += currentWord.charAt(k);
                        largerThanUseListString += currentWord.charAt(k);
                    }

                    if(Integer.parseInt(index) > allModules[i].uses.size()) {
                        //add error message to symbol table
                        symbolTable.errorMessages.add("Error: External address exceeds length of use list; treated as immediate.");
                        symbolTable.absoluteAddresses.add(Integer.parseInt(currentWord.charAt(0) + largerThanUseListString));
                        largerThanUseLength = true;
                    }

                    //if the current index does not exhaust the current module's use length
                    if(!largerThanUseLength) {
                        int absoluteAddress;
                        externalReference = allModules[i].uses.get(Integer.parseInt(index));
                        for(int l = 0; l < allModules.length; l++) {
                            for(int m = 0; m < allModules[l].allVariables.size(); m++) {
                                if(allModules[l].allVariables.get(m).symbol.equals(externalReference)) {
                                    absoluteAddress = allModules[l].allVariables.get(m).symbolLocation;
                                    String absoluteAddressString = String.valueOf(absoluteAddress);
                                    int numberOfDigits = absoluteAddressString.length();
                                    int fullAddress = numberOfDigits;
                                    int currentWordIndex = 0;

                                    //calculate the last remainder of the address
                                    while(fullAddress < lengthOfAddress) {
                                        address += currentWord.charAt(currentWordIndex);
                                        currentWordIndex++;
                                        fullAddress++;
                                    }	

                                    //add absolute address to the symbol table
                                    symbolTable.absoluteAddresses.add(Integer.parseInt(address + absoluteAddress));

                                    //increment number of uses in use list
                                    allModules[i].entireUseListUsed++;

                                    //mark that current variable was referenced in current module
                                    found = true;
                                }
                            }
                        }
                    }

                    //if the external address was never defined and index in current module's use line is valid
                    if((found == false) && (largerThanUseLength == false)) {
                        //add error message to symbol table
                        symbolTable.errorMessages.add("Error: " + externalReference + " is not defined; zero used.");

                        //add first digit of word to corrected absolute address
                        correctedAbsoluteAddressString += currentWord.charAt(0);

                        //add corrected absolute address to symbol table
                        symbolTable.absoluteAddresses.add(Integer.parseInt(correctedAbsoluteAddressString + "000"));	
                    }
                }
            }

            //check if all variables in the use list have been used
            if(allModules[i].entireUseListUsed < allModules[i].uses.size()) {
                symbolTable.warningMessages.add("Warning: In module " + i + " not all variables in the use list are used.");
            }
        }	

        return symbolTable;	
    }
}
