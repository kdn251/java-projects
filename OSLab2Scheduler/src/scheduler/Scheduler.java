package scheduler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Scheduler {
	
	public static void main(String[] args) {

		//declare scanner for input file
		Scanner scanner;
		
		//declare scanner for random OS
		Scanner randomOSScanner;
		
		try {
			
			//initialize scanner to try and read file
			scanner = new Scanner(new File(args[0]));
			
			//initialize scanner to try and read random-numbers file
			randomOSScanner = new Scanner(new File("random-numbers"));
			
			
		}
		catch (Exception allExceptions) {
			
			//catch except and exit gracefully
			scanner = null;
			System.out.println("Please enter a file name via the command line.");
			System.exit(-1);
			
			randomOSScanner = null;
			System.out.println("Please make sure that the file named random-numbers is in the current directory");
			System.exit(-1);
			
		}
		
		//initialize array list to store all processes	
		ArrayList<Process> processes = new ArrayList<Process>();
		
		
		//keep track of when to create a new process
		int count = 0;
		
		//read input file
		while(scanner.hasNextLine()) {
			
			//store the current line of input file
			String currentLine = scanner.nextLine();
			
			//store all numbers of current line
			String[] numbers = currentLine.split(" ");
			
			//initialize all process objects and add them to processes array
			for(int i = 1; i < numbers.length; i++) {
				
				Process newProcess = new Process(Integer.parseInt(numbers[i]), Integer.parseInt(numbers[++i]), Integer.parseInt(numbers[++i]), Integer.parseInt(numbers[++i]));
				
				processes.add(newProcess);
				
			}
			


			
		}
		
		//sort processes by arrival time
		Collections.sort(processes);
		
		//print all processes
		for(int i = 0; i < processes.size(); i++) {
			
			System.out.println("Process " + i + ": "  + processes.get(i).arrivalTime + ", " + processes.get(i).cpuBurst + ", " + processes.get(i).cpuTime + ", " + processes.get(i).multiplier);
			
		}
	
	}

}
