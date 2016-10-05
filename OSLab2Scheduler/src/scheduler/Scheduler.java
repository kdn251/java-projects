package scheduler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
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
			
			//System.out.println("Process " + i + ": "  + processes.get(i).arrivalTime + ", " + processes.get(i).cpuBurst + ", " + processes.get(i).cpuTime + ", " + processes.get(i).multiplier);
			
		}
		
		//run first come first serve scheduling algorithm on all processes
		firstComeFirstServe(processes);
	
	}
	
	
	public static void firstComeFirstServe(ArrayList<Process> processes) {
		
		//initialize an array list for terminated processes
		ArrayList<Process> terminatedProcesses = new ArrayList<Process>();
		
		//create queue for ready processes
		Queue<Process> ready = new LinkedList<Process>();
		
		//create queue for blocked processes
		Queue<Process> blocked = new LinkedList<Process>();
		
		//add all processes to read queue
		for(Process process : processes) {
			
			ready.add(process);
			
		}
		
		for(int i = 0; i < ready.size(); i++) {
			
			//TEST
			//System.out.println("Size of ready: " + ready.size());
			//System.out.println("Process " + i + " arrival time: " + ready.remove().arrivalTime);
			
		}
				
		//count the number of terminated processes
		int finishedProcesses = 0;
		
		//while all processes have not terminated
		while(finishedProcesses != processes.size()) {

			//while ready list and blocked list of processes are not empty
			while((!ready.isEmpty()) || (!blocked.isEmpty())) {
				
				//store current process
				Process currentProcess = ready.remove();
				
				int burst = randomOS(currentProcess.cpuBurst);
				
				//if the burst is greater than remaining cpu time set burst equal to remaining cpu time
				if(burst > currentProcess.cpuBurst) {
					
					burst = currentProcess.cpuBurst;
					
				}
				
				//decrement current processes' required cpu time by its current cpu burst
				currentProcess.cpuTime -= burst;
				
				//increment current process' finishing time
				currentProcess.finishingTime += burst;

				//if the required cpu time of current process is 0
				if(currentProcess.cpuTime <= 0) {
					
					//add current process to terminated processes
					terminatedProcesses.add(currentProcess);
					
					//remove current process from ready list
					ready.remove(currentProcess);
					
					//increment finished processes
					finishedProcesses++;
					
					if(finishedProcesses == processes.size()) {
						
						break;
						
					}
					
					//TEST
					//System.out.println("number of finished processes: " + finishedProcesses);
					
				}				
				
				//set current processes' I/O time to burst times its multiplier
				currentProcess.inputOutputTime = burst * currentProcess.multiplier;
				
				//add current process to blocked queue
				blocked.add(currentProcess);
				
				for(Process process : blocked) {
					
					//if the process' I/O time has finished
					if(process.inputOutputTime <= burst) {
						
						process.inputOutputTime = 0;
						
						//increment current process' finishing time
						process.finishingTime += burst;
						
						blocked.remove(process);
						
						ready.add(process);
						
					}
					
					//decrement processes' I/O time by the burst
					else {
						
						process.inputOutputTime -= burst;
						
						//increment current process' finishing time
						process.finishingTime += burst;
						
					}
					
				}
				
				
				
				
			}
			
			
		}
		
		//print formated output of first come first serve scheduling algorithm
		printFirstComeFirstServe(terminatedProcesses);
		
		 
	}


	private static void printFirstComeFirstServe(ArrayList<Process> terminatedProcesses) {

		for(int i = 0; i < terminatedProcesses.size(); i++) {
			
			System.out.println("Process " + i + ":");
			System.out.println("(A, B, C, M) = (" + terminatedProcesses.get(i).arrivalTime + ", " + terminatedProcesses.get(i).cpuBurst + ", " + terminatedProcesses.get(i).cpuTime + ", " + terminatedProcesses.get(i).multiplier + ")");
			System.out.println("Finishing time: " + terminatedProcesses.get(i).finishingTime);
			System.out.println("Turnaround time: " + (terminatedProcesses.get(i).finishingTime - terminatedProcesses.get(i).arrivalTime));
			System.out.println("I/O time: " + terminatedProcesses.get(i).inputOutputTime);
			System.out.println("Waiting time: " + terminatedProcesses.get(i).waitingTime);
			System.out.println();
			
		}
		
	}


	private static int randomOS(int cpuBurst) {
		
		Scanner randomOSScanner;
		
		//try to read input from random-numbers file
		try {
			
			randomOSScanner = new Scanner(new File("random-numbers"));
			
		}
		
		//catch all excepts and exit gracefully
		catch (Exception allException) {
			
			randomOSScanner = null;
			System.out.println("Please ensure that the file random-numbers is in the current directory");
			System.exit(-1);
			
		}
		
		//TEST
		//System.out.println("cpuBurst is " + cpuBurst);
		//System.out.println("number from random-numbers is " + randomOSScanner.nextInt());
		
		int burst = 1 + (randomOSScanner.nextInt() % cpuBurst);
		
		//TEST
		//System.out.println("current CPU burst is " + burst);
		
		return burst;
		
	}
	

}
