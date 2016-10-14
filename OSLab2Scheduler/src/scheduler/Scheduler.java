package scheduler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Scheduler {
	
	//keep track of run time of program
	static int totalRunTime = 0;
	
	//keep track of running time of jobs
	static float jobsRunningTime = 0;
	
	//keep track of I/O time of jobs
	static float inputOutputJobsTime = 0;
	
	//initialize quantum for round robin scheduling algorithm
	static int QUANTUM = 2;
	
	//initialize verbose flag
	static boolean VERBOSE_FLAG = false;
	
	//initialize file location
	static int FILE_LOCATION = 0;
	
	
	public static void main(String[] args) {
		
		//check if verbose flag is set
		if(args.length == 2) {
			
			VERBOSE_FLAG = true;
			
			FILE_LOCATION = 1;
			
		}

		//declare scanner objects for input file for each scheduling algorithm
		Scanner scannerFirstComeFirstServed = null;
		Scanner scannerUniprogrammed = null;
		Scanner scannerRoundRobin = null;
		Scanner scannerShortestJobFirst = null;

		
		//initialize scanner objects for random-numbers file for each scheduling algorithm
		Scanner randomOSScannerFirstComeFirstServed = null;
		Scanner randomOSScannerUniprogrammed = null;
		Scanner randomOSScannerRoundRobin = null;
		Scanner randomOSScannerShortestJobFirst = null;

		try {
			
			//initialize scanner objects to try and read input file for each scheduling algorithm
			scannerFirstComeFirstServed = new Scanner(new File(args[FILE_LOCATION]));
			scannerUniprogrammed = new Scanner(new File(args[FILE_LOCATION]));
			scannerRoundRobin = new Scanner(new File(args[FILE_LOCATION]));
			scannerShortestJobFirst = new Scanner(new File(args[FILE_LOCATION]));

			
			//initialize scanner objects to try and read random-numbers file for each scheduling algorithm
			randomOSScannerFirstComeFirstServed = new Scanner(new File("random-numbers"));
			randomOSScannerUniprogrammed = new Scanner(new File("random-numbers"));
			randomOSScannerRoundRobin = new Scanner(new File("random-numbers"));
			randomOSScannerShortestJobFirst = new Scanner(new File("random-numbers"));

			
		}
		catch (Exception allExceptions) {
			
			//catch except and exit gracefully
			System.out.println("Please enter a file name via the command line and ensure that the random-numbers file is in the current directory.");
			System.exit(-1);
			
		}
		
		//initialize array lists to store all processes for each scheduling algorithm
		ArrayList<Process> processesFirstComeFirstServed = new ArrayList<Process>(readInputFile(scannerFirstComeFirstServed));
		ArrayList<Process> processesUniprogrammed = new ArrayList<Process>(readInputFile(scannerUniprogrammed));
		ArrayList<Process> processesRoundRobin = new ArrayList<Process>(readInputFile(scannerRoundRobin));
		ArrayList<Process> processesShortestJobFirst = new ArrayList<Process>(readInputFile(scannerShortestJobFirst));


		//run first come first serve scheduling algorithm on all processes
		firstComeFirstServed(processesFirstComeFirstServed, randomOSScannerFirstComeFirstServed);
				
		//run uniprogrammed scheduling algorithm on all processes
		uniprogrammed(processesUniprogrammed, randomOSScannerUniprogrammed);
		
		//run round robin scheduling algorithm on all processes
		roundRobin(processesRoundRobin, randomOSScannerRoundRobin);
		
		//run shortest job first scheduling algorithm on all processes
		shortestJobFirst(processesShortestJobFirst, randomOSScannerShortestJobFirst);
	
	}
	

	public static void firstComeFirstServed(ArrayList<Process> processes, Scanner randomOSScanner) {
		
		printInputs(processes);
		
		//initialize an array list for terminated processes
		ArrayList<Process> terminatedProcesses = new ArrayList<Process>();
		
		//create queue for ready processes
		Queue<Process> ready = new LinkedList<Process>();
		
		//create queue for blocked processes
		Queue<Process> blocked = new LinkedList<Process>();
		
		//add the first process to ready
		ready.add(processes.get(0));
		processes.get(0).added = true;
		
		int count = 0;
		
		System.out.println();
		
		if(VERBOSE_FLAG) {
			
			System.out.println("This detailed printout gives the state and remaining burst for each process");
			
			System.out.println();
		
			printVerbose(count, processes);
			count++;
		
		}
		
		//if any process has the same arrival time as the first process add it to ready
		for(int i = 1; i < processes.size(); i++) {
			
			if(processes.get(i).arrivalTime == processes.get(0).arrivalTime) {
				
				ready.add(processes.get(i));
				processes.get(i).added = true;
				processes.get(i).currentState = "ready";
				
			}
			
		}
				
		//count the number of terminated processes
		int finishedProcesses = 0;
		
		//while all processes have not terminated
		while(finishedProcesses != processes.size()) {

			//while ready list and blocked list of processes are not empty
			while((!ready.isEmpty())) {
				
				//store current process
				Process currentProcess = ready.remove();
				
				currentProcess.currentState = "running";
								
				int burst = randomOS(currentProcess.cpuBurst, randomOSScanner);
				
				currentProcess.burst = burst;
				
				for(int i = 0; i < burst; i++) { //MIGHT NEED TO DELETE THIS LOOP!
					
					if(VERBOSE_FLAG) {

						printVerbose(count, processes);
						count++;

					}
					
				}
								
				//if the burst is greater than remaining cpu time set burst equal to remaining cpu time
				if(burst > currentProcess.cpuTime) {
					
					burst = currentProcess.cpuTime;
					
				}
				
				//decrement current processes' required cpu time by its current cpu burst
				currentProcess.cpuTime -= burst;
				
				//increment current process' finishing time
				currentProcess.finishingTime += burst;
				
				//increment total run time
				totalRunTime += burst;
				
				//increment time that jobs have run
				jobsRunningTime += burst;
								
				//increment waiting time and finishing time of every process in ready
				for(Process process : ready) {

					process.currentState = "ready";
					
					process.waitingTime += burst;
										
					process.finishingTime += burst;

				}				
				
				//create an array list of items to remove from blocked
				ArrayList<Process> blockedToRemove = new ArrayList<Process>();
								
				//update and check attributes of each process in blocked
				for(Process process : blocked) {
					
					//if the process' I/O time has finished
					if(process.inputOutputTime <= burst) {
						
						//increment current process' finishing time
						process.finishingTime += burst;
						
						process.waitingTime += burst - process.inputOutputTime;

						process.inputOutputTime = 0;
						
						blockedToRemove.add(process);

						ready.add(process);
						
						process.currentState = "ready";

					}

					//decrement processes' I/O time by the burst
					else {

						process.inputOutputTime -= burst;

						//increment current process' finishing time
						process.finishingTime += burst;
						
						process.currentState = "blocked";

					}

				}
				
				//remove processes from blocked that have finished their I/O
				blocked.removeAll(blockedToRemove);
				
				//check if any other processes have arrived
				for(Process process : processes) {
										
					if(process.arrivalTime <= totalRunTime && process.added != true) {
											
						ready.add(process);
						process.finishingTime = totalRunTime;
						process.waitingTime = totalRunTime - process.arrivalTime; 
						process.added = true;
						process.currentState = "ready";
												
					}
					
				}
				
				//if the required cpu time of current process is 0
				if(currentProcess.cpuTime <= 0) {
					
					//increment finished processes
					finishedProcesses++;
					
					currentProcess.currentState = "terminated";
					
					//if all processes have terminated break from the while loop
					if(finishedProcesses == processes.size()) {
						
						break;
						
					}
					
				}
				
				else {

					//set current processes' I/O time to burst times its multiplier
					currentProcess.inputOutputTime = burst * currentProcess.multiplier;
					
					//increment I/O time of all processes
					inputOutputJobsTime += currentProcess.inputOutputTime;
					
					currentProcess.totalInputOutputTime += currentProcess.inputOutputTime;
					
					currentProcess.blocked = currentProcess.inputOutputTime;			

					//if current process' I/O time is greater than 0
					if(currentProcess.inputOutputTime > 0) {

						//add current process to blocked queue
						blocked.add(currentProcess);	
						
						currentProcess.currentState = "blocked";

					}

					//if current process has no I/O blast
					else {

						ready.add(currentProcess);

					}
				
				}

			}
			
			//continue decrementing I/O time of all processes in blocked until ready is no longer empty
			if(!blocked.isEmpty()) {
				
				if(VERBOSE_FLAG) {

				//print detailed output if verbose flag is set 
				printVerbose(count, processes);

				count++;

				}
				
				ArrayList<Process> toRemoveBlocked = new ArrayList<Process>();
				
				//increment total run time
				totalRunTime++;
								
				for(Process process : blocked) {
					
					process.finishingTime++;
					process.inputOutputTime--;
					
					if(process.inputOutputTime == 0) {
						
						toRemoveBlocked.add(process);
						ready.add(process);
						
					}
					
					//check if any other processes have arrived
					for(Process currentProcess : processes) {
						
						if(currentProcess.arrivalTime <= totalRunTime && currentProcess.added != true) {
													
							ready.add(currentProcess);
							currentProcess.finishingTime = totalRunTime;
							currentProcess.waitingTime = totalRunTime - currentProcess.arrivalTime;
							currentProcess.added = true;
							currentProcess.currentState = "ready";
							
						}
						
					}
					
				}
				
				blocked.removeAll(toRemoveBlocked);
				
			}
			
		}
				
		System.out.println();

		System.out.println("The scheduling algorithm used was First Come First Served");
		
		
		//print formated output of first come first serve scheduling algorithm
		printResults(processes);
		 
	}
	
	
	
	private static void uniprogrammed(ArrayList<Process> processes, Scanner randomOSScanner) {
		
		printInputs(processes);
		
		System.out.println();

		//initialize an array list for terminated processes
		ArrayList<Process> terminatedProcesses = new ArrayList<Process>();
		
		//create queue for ready processes
		Queue<Process> ready = new LinkedList<Process>();
		
		//create queue for blocked processes
		Queue<Process> blocked = new LinkedList<Process>();
		
		//add the first process to ready
		ready.add(processes.get(0));
		processes.get(0).added = true;
		
		int count = 0;
		
		if(VERBOSE_FLAG) {
			
			System.out.println("This detailed printout gives the state and remaining burst for each process");
			
			System.out.println();

			printVerbose(count, processes);
			
			count++;
			
		}	
		
		//if any process has the same arrival time as the first process add it to ready
		for(int i = 1; i < processes.size(); i++) {
			
			if(processes.get(i).arrivalTime == processes.get(0).arrivalTime) {
				
				ready.add(processes.get(i));
				processes.get(i).added = true;
				processes.get(i).currentState = "ready";
				
			}
			
		}
				
		//count the number of terminated processes
		int finishedProcesses = 0;
		
		//while all processes have not terminated
		while(finishedProcesses != processes.size()) {
						
			//store process to run
			Process currentProcess = ready.remove();
			
			//while current process has not terminated
			while(currentProcess.cpuTime > 0) {
			
				currentProcess.currentState = "running";
				
				//get cpu burst of current process
				int burst = randomOS(currentProcess.cpuBurst, randomOSScanner);
				
				currentProcess.burst = burst;

				for(int i = 0; i < burst; i++) { //MIGHT NEED TO DELETE THIS LOOP!
					
					if(VERBOSE_FLAG) {
						
						printVerbose(count, processes);
						
						count++;

					}
					
				}
								
				//if the burst is greater than remaining cpu time set burst equal to remaining cpu time
				if(burst > currentProcess.cpuTime) {
					
					burst = currentProcess.cpuTime;
					
				}
				
				//decrement current processes' required cpu time by its current cpu burst
				currentProcess.cpuTime -= burst;
				
				//increment current process' finishing time
				currentProcess.finishingTime += burst;
				
				//increment total run time
				totalRunTime += burst;
				
				//increment time that jobs have run
				jobsRunningTime += burst;
								
				//increment waiting time and finishing time of every process in ready
				for(Process process : ready) {

					process.waitingTime += burst;
										
					process.finishingTime += burst;
					
					process.currentState = "ready";

				}				
				
				//check if any other processes have arrived
				for(Process process : processes) {
										
					if(process.arrivalTime <= totalRunTime && process.added != true) {
												
						ready.add(process);
						process.finishingTime = totalRunTime;
						process.waitingTime = totalRunTime - process.arrivalTime; //BUG: if process arrives while a process is running, set it's waiting time accordingly
						process.added = true;
						process.currentState = "ready";
												
					}
					
				}
				
				//System.out.println("Finishing time is currently: " + currentProcess.finishingTime);

				//if the required cpu time of current process is 0
				if(currentProcess.cpuTime <= 0) {
					
					//increment finished processes
					finishedProcesses++;
					
					//if all processes have terminated break from the while loop
					if(finishedProcesses == processes.size()) {
						
						break;
						
					}
					
				}
				
				else {
				
					//set current processes' I/O time to burst times its multiplier
					currentProcess.inputOutputTime = burst * currentProcess.multiplier;
					
					currentProcess.blocked = currentProcess.inputOutputTime; 
					
					currentProcess.currentState = "blocked";
					
					if(VERBOSE_FLAG) {

						for(int i = 0; i < currentProcess.inputOutputTime; i++) {
							
							printVerbose(count, processes);
							
							count++;
							
						}

					}
										
					//increment I/O time of all processes
					inputOutputJobsTime += currentProcess.inputOutputTime;
					
					currentProcess.totalInputOutputTime += currentProcess.inputOutputTime;
					
					currentProcess.finishingTime += currentProcess.inputOutputTime;
					
					totalRunTime += currentProcess.inputOutputTime;
										
					for(Process process : ready) {
						
						process.finishingTime += currentProcess.inputOutputTime;
						process.waitingTime += currentProcess.inputOutputTime;
						process.currentState = "ready";
						
					}
									
				}
				
			}	
			
			currentProcess.currentState = "terminated";
			
		}
				
		System.out.println();
		
		System.out.println("The scheduling algorithm used was Uniprogrammed");
		
		//print formated output of Uniprogrammed scheduling algorithm
		printResults(processes);
		
	}
	
	
	
	
	private static void roundRobin(ArrayList<Process> processes, Scanner randomOSScanner) {
		
		printInputs(processes);
		
		//initialize an array list for terminated processes
		ArrayList<Process> terminatedProcesses = new ArrayList<Process>();
		
		//create queue for ready processes
		Queue<Process> ready = new LinkedList<Process>();
		
		//create queue for blocked processes
		Queue<Process> blocked = new LinkedList<Process>();
		
		//add the first process to ready
		ready.add(processes.get(0));
		processes.get(0).added = true;
		
		int count = 0;
		
		System.out.println();
				
		if(VERBOSE_FLAG) {
		
			System.out.println("This detailed printout gives the state and remaining burst for each process");
			
			System.out.println();

			printVerbose(count, processes);
					
		}
		
		//if any process has the same arrival time as the first process add it to ready
		for(int i = 1; i < processes.size(); i++) {
			
			if(processes.get(i).arrivalTime == processes.get(0).arrivalTime) {
				
				ready.add(processes.get(i));
				processes.get(i).added = true;
				processes.get(i).currentState = "ready";
				
			}
			
		}
				
		//count the number of terminated processes
		int finishedProcesses = 0;
		
		//while all processes have not terminated
		while(finishedProcesses != processes.size()) {

			//while ready list and blocked list of processes are not empty
			while((!ready.isEmpty())) {
			
				//store current process
				Process currentProcess = ready.remove();
				
				currentProcess.currentState = "running";
								
				int burst = randomOS(currentProcess.cpuBurst, randomOSScanner);
												
				//if the burst is greater than remaining cpu time set burst equal to remaining cpu time
				if(burst > QUANTUM) {
					
					burst = QUANTUM;
					
				}
				
				if(burst > currentProcess.cpuTime) {
										
					burst = currentProcess.cpuTime;
					
				}
				
				currentProcess.burst = burst;
								
				for(int i = 0; i < burst; i++) { //MIGHT NEED TO DELETE THIS LOOP!
					
					if(VERBOSE_FLAG) {

						count++;
						
						printVerbose(count, processes);

					}
					
				}
				
				//decrement current processes' required cpu time by its current cpu burst
				currentProcess.cpuTime -= burst;
				
				//increment current process' finishing time
				currentProcess.finishingTime += burst;
				
				//increment total run time
				totalRunTime += burst;
				
				//increment time that jobs have run
				jobsRunningTime += burst;
								
				//increment waiting time and finishing time of every process in ready
				for(Process process : ready) {
					
					process.currentState = "ready";

					process.waitingTime += burst;
					
					process.finishingTime += burst;

				}				
				
				//create an array list of items to remove from blocked
				ArrayList<Process> blockedToRemove = new ArrayList<Process>();
								
				//update and check attributes of each process in blocked
				for(Process process : blocked) {

					//if the process' I/O time has finished
					if(process.inputOutputTime <= burst) {

						process.waitingTime += burst - process.inputOutputTime; //THIS MIGHT BE WRONG, MAYBE REMOVE IT, MIGHT NOT BE CAUSING WAITING TIME BUG
						
						process.inputOutputTime = 0;

						//increment current process' finishing time
						process.finishingTime += burst;

						//blocked.remove(process);
						blockedToRemove.add(process);

						ready.add(process);

					}

					//decrement processes' I/O time by the burst
					else {

						process.inputOutputTime -= burst;
						
						//increment current process' finishing time
						process.finishingTime += burst;

					}

				}
				
				//remove processes from blocked that have finished their I/O
				blocked.removeAll(blockedToRemove);
				
				//check if any other processes have arrived
				for(Process process : processes) {
					
					//System.out.println("PROCESS ADDED ATTRIBUTE IS " + process.added);
					
					if(process.arrivalTime <= totalRunTime && process.added != true) {
										
						ready.add(process);
						process.currentState = "ready";
						process.finishingTime = totalRunTime;
						process.waitingTime = totalRunTime - process.arrivalTime; //BUG: if process arrives while a process is running, set it's waiting time accordingly
						process.added = true;
												
					}
					
				}
				
				//if the required cpu time of current process is 0
				if(currentProcess.cpuTime <= 0) {
					
					//increment finished processes
					finishedProcesses++;
					
					currentProcess.currentState = "terminated";
					
					//if all processes have terminated break from the while loop
					if(finishedProcesses == processes.size()) {
						
						break;
						
					}
					
				}
				
				else {

					//set current processes' I/O time to burst times its multiplier
					currentProcess.inputOutputTime = burst * currentProcess.multiplier;
					
					currentProcess.blocked = currentProcess.inputOutputTime;
					
					//increment I/O time of all processes
					inputOutputJobsTime += currentProcess.inputOutputTime;
					
					currentProcess.totalInputOutputTime += currentProcess.inputOutputTime;		

					//if current process' I/O time is greater than 0
					if(currentProcess.inputOutputTime > 0) {

						//add current process to blocked queue
						blocked.add(currentProcess);
						currentProcess.currentState = "blocked";

					}

					//if current process has no I/O blast
					else {

						ready.add(currentProcess);

					}
				
				}

			}
			
			
			//continue decrementing I/O time of all processes in blocked until ready is no longer empty
			if(!blocked.isEmpty()) {
				
				if(VERBOSE_FLAG) {

					count++;

					//print detailed output if verbose flag is set 
					printVerbose(count, processes);


				}
				
				ArrayList<Process> toRemoveBlocked = new ArrayList<Process>();
				
				//increment total run time
				totalRunTime++;
								
				for(Process process : blocked) {
					
					process.finishingTime++;
					process.inputOutputTime--;
					
					if(process.inputOutputTime == 0) {
						
						toRemoveBlocked.add(process);
						ready.add(process);
						
					}
					
					//check if any other processes have arrived
					for(Process currentProcess : processes) {
						
						if(currentProcess.arrivalTime <= totalRunTime && currentProcess.added != true) {
																	
							ready.add(currentProcess);
							currentProcess.finishingTime = totalRunTime;
							currentProcess.waitingTime = totalRunTime - currentProcess.arrivalTime;		
							currentProcess.added = true;
							
						}
						
					}	
					
				}
				
				blocked.removeAll(toRemoveBlocked);

			}
			
		}
				
		System.out.println();
		
		System.out.println("The scheduling algorithm used was Round Robin");
		
		//print formated output of first come Round Robin scheduling algorithm
		printResults(processes);
		
		 
	}
	
	
	private static void shortestJobFirst(ArrayList<Process> processes, Scanner randomOSScanner) {

		printInputs(processes);
		
		//initialize an array list for terminated processes
		ArrayList<Process> terminatedProcesses = new ArrayList<Process>();
		
		//create queue for ready processes
		ArrayList<Process> ready = new ArrayList<Process>();
		
		//create queue for blocked processes
		Queue<Process> blocked = new LinkedList<Process>();
		
		//add the first process to ready
		ready.add(processes.get(0));
		processes.get(0).added = true;
		
		int count = 0;
		
		System.out.println();
		
		if(VERBOSE_FLAG) {
			
			System.out.println("This detailed printout gives the state and remaining burst for each process");
			
			System.out.println();
		
			printVerbose(count, processes);
			
			count++;
		
		}
		
		//if any process has the same arrival time as the first process add it to ready
		for(int i = 1; i < processes.size(); i++) {
			
			if(processes.get(i).arrivalTime == processes.get(0).arrivalTime) {
				
				ready.add(processes.get(i));
				processes.get(i).added = true;
				processes.get(i).currentState = "ready";
				
			}
			
		}
				
		//count the number of terminated processes
		int finishedProcesses = 0;
		
		//while all processes have not terminated
		while(finishedProcesses != processes.size()) {

			//while ready list and blocked list of processes are not empty
			while((!ready.isEmpty())) {
				
				int min = 0;
				
				for(int i = 0; i < ready.size(); i++) {
					
					if(ready.get(i).cpuTime < ready.get(min).cpuTime) {
						
						min = i;
						
					}
					
				}
				
			
				//store current process
				Process currentProcess = ready.get(min);
				
				currentProcess.currentState = "running";
				
				ready.remove(min);
								
				int burst = randomOS(currentProcess.cpuBurst, randomOSScanner);
				
				currentProcess.burst = burst;
				
				for(int i = 0; i < burst; i++) { //MIGHT NEED TO DELETE THIS LOOP!
					
					if(VERBOSE_FLAG) {

						printVerbose(count, processes);
						count++;

					}
					
				}
								
				//if the burst is greater than remaining cpu time set burst equal to remaining cpu time
				if(burst > currentProcess.cpuTime) {
					
					burst = currentProcess.cpuTime;
					
				}
				
				//decrement current processes' required cpu time by its current cpu burst
				currentProcess.cpuTime -= burst;
				
				//increment current process' finishing time
				currentProcess.finishingTime += burst;
				
				//increment total run time
				totalRunTime += burst;
				
				//increment time that jobs have run
				jobsRunningTime += burst;
								
				//increment waiting time and finishing time of every process in ready
				for(Process process : ready) {

					process.waitingTime += burst;
					
					process.finishingTime += burst;
					
					process.currentState = "ready";

				}				
				
				//create an array list of items to remove from blocked
				ArrayList<Process> blockedToRemove = new ArrayList<Process>();
								
				//update and check attributes of each process in blocked
				for(Process process : blocked) {

					//if the process' I/O time has finished
					if(process.inputOutputTime <= burst) {

						process.waitingTime += burst - process.inputOutputTime;
						
						process.inputOutputTime = 0;

						//increment current process' finishing time
						process.finishingTime += burst;

						//blocked.remove(process);
						blockedToRemove.add(process);

						ready.add(process);

					}

					//decrement processes' I/O time by the burst
					else {

						process.inputOutputTime -= burst;
						
						//increment current process' finishing time
						process.finishingTime += burst;

					}

				}
				
				//remove processes from blocked that have finished their I/O
				blocked.removeAll(blockedToRemove);
				
				//check if any other processes have arrived
				for(Process process : processes) {
										
					if(process.arrivalTime <= totalRunTime && process.added != true) {
										
						ready.add(process);
						process.currentState = "ready";
						process.finishingTime = totalRunTime;
						process.waitingTime = totalRunTime - process.arrivalTime;
						process.added = true;
												
					}
					
				}

				//if the required cpu time of current process is 0
				if(currentProcess.cpuTime <= 0) {
					
					//increment finished processes
					finishedProcesses++;
					
					currentProcess.currentState = "terminated";
					
					//if all processes have terminated break from the while loop
					if(finishedProcesses == processes.size()) {
						
						break;
						
					}
					
				}
				
				else {

					//set current processes' I/O time to burst times its multiplier
					currentProcess.inputOutputTime = burst * currentProcess.multiplier;
					currentProcess.blocked = currentProcess.inputOutputTime;
					
					//increment I/O time of all processes
					inputOutputJobsTime += currentProcess.inputOutputTime;
					
					currentProcess.totalInputOutputTime += currentProcess.inputOutputTime;			

					//if current process' I/O time is greater than 0
					if(currentProcess.inputOutputTime > 0) {

						//add current process to blocked queue
						blocked.add(currentProcess);	
						currentProcess.currentState = "blocked";

					}

					//if current process has no I/O blast
					else {

						ready.add(currentProcess);

					}
				
				}

			}
			
			//continue decrementing I/O time of all processes in blocked until ready is no longer empty
			if(!blocked.isEmpty()) {
				
				if(VERBOSE_FLAG) {

					printVerbose(count, processes);
					
					count++;

				}
				
				ArrayList<Process> toRemoveBlocked = new ArrayList<Process>();
				
				//increment total run time
				totalRunTime++;
								
				for(Process process : blocked) {
					
					process.finishingTime++;
					process.inputOutputTime--;
		
					if(process.inputOutputTime == 0) {
						
						toRemoveBlocked.add(process);
						ready.add(process);
						
					}
					
					//check if any other processes have arrived
					for(Process currentProcess : processes) {
						
						if(currentProcess.arrivalTime <= totalRunTime && currentProcess.added != true) {
							
							ready.add(currentProcess);
							currentProcess.finishingTime = totalRunTime;
							currentProcess.waitingTime = totalRunTime - currentProcess.arrivalTime;	
							currentProcess.added = true;	
							
						}
						
					}
					
				}
				
				blocked.removeAll(toRemoveBlocked);
				
			}
			
		}
				
		System.out.println();
		
		System.out.println("The scheduling algorithm used was Shortest Job First");
		
		//print formated output of shortest job first scheduling algorithm
		printResults(processes);

	}
		

	private static void printResults(ArrayList<Process> terminatedProcesses) {

		System.out.println();
		
		//sort terminated processes by arrival time
		Collections.sort(terminatedProcesses);
		
		//initialize counters
		float averageTurnaroundTime = 0;
		float averageWaitingTime = 0;
		
		for(int i = 0; i < terminatedProcesses.size(); i++) {
			
			//print out all processes
			System.out.println("Process " + i + ":");
			System.out.println("\t(A, B, C, M) = (" + terminatedProcesses.get(i).arrivalTime + ", " + terminatedProcesses.get(i).cpuBurst + ", " + terminatedProcesses.get(i).cpuTimeCopy + ", " + terminatedProcesses.get(i).multiplier + ")");
			System.out.println("\tFinishing time: " + terminatedProcesses.get(i).finishingTime);
			System.out.println("\tTurnaround time: " + (terminatedProcesses.get(i).finishingTime - terminatedProcesses.get(i).arrivalTime));
			System.out.println("\tI/O time: " + terminatedProcesses.get(i).totalInputOutputTime);
			System.out.println("\tWaiting time: " + terminatedProcesses.get(i).waitingTime);
			System.out.println();
			
			//increment variables
			averageTurnaroundTime += terminatedProcesses.get(i).finishingTime - terminatedProcesses.get(i).arrivalTime;
			averageWaitingTime += terminatedProcesses.get(i).waitingTime;
			
		}
		
		//calculate variables
		averageTurnaroundTime = averageTurnaroundTime / terminatedProcesses.size();
		averageWaitingTime = averageWaitingTime / terminatedProcesses.size();
		float throughput = terminatedProcesses.size() / (float)totalRunTime * 100;
		
		//print formatted output
		System.out.println("Summary Data:");
		System.out.println("\tFinishing Time: " + totalRunTime);
		System.out.println("\tCPU Utilization: " + String.format("%.6f", jobsRunningTime / totalRunTime));
		System.out.println("\tI/O Utilization: " + String.format("%.6f", inputOutputJobsTime / totalRunTime));
		System.out.println("\tThroughput: " + String.format("%6.6f", throughput) + " processes per hundred cycles"); ;
		System.out.println("\tAverage turnaround time: " + String.format("%.6f",averageTurnaroundTime ));
		System.out.println("\tAverage waiting time: " + String.format("%.6f", averageWaitingTime));
		
		//reset static variables
		jobsRunningTime = 0;
		inputOutputJobsTime = 0;
		totalRunTime = 0;
		
		System.out.println();

	}


	private static int randomOS(int cpuBurst, Scanner randomOSScanner) {
		
		int randomNumber = randomOSScanner.nextInt();
		
		int burst = 1 + (randomNumber % cpuBurst);
		
		return burst;
		
	}
	
	private static void printVerbose(int count, ArrayList<Process> processes) {
		
		
		for(int i = 0; i < 1; i++) {
			
			System.out.print("Before cycle " + String.format("%5s", count) + ":\t");

			for(Process process : processes) {

				int number = 0;
				
				if(process.currentState == "unstarted") {
					
					number = 0;
					
					
				}
				
				else if(process.currentState == "ready") {
					
					number = 0;
					
				}
				
				else if(process.currentState == "running") {

					number = process.burst;
					process.burst--; //MIGHT NEED TO DELETE THIS KEVIN
					
				}
				
				else if(process.currentState == "blocked") {
					
					number = process.blocked;
					
					if(process.blocked != -1) {
						
						process.blocked--; //MIGHT NEED TO DELETE THIS KEVIN
						
					}
					
					if(process.blocked == -1) {
						
						process.currentState = "ready";
						number = 0;
						
					}
					
				}
				
				else {
					
					number = 0;
					
				}
				
				System.out.print(String.format("%10s", process.currentState) + "   " + number + "  ");

			}
			
			System.out.println();
		}
		
	}
	
	
	public static ArrayList<Process> readInputFile(Scanner scanner) {
		
		ArrayList<Process> processes = new ArrayList<Process>();
		
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
				
				newProcess.cpuTimeCopy = newProcess.cpuTime;
				
			}

		}
		
		return processes;
		
	}
	
	public static void printInputs(ArrayList<Process> processes) {
		
		System.out.print("The original input was: " + processes.size());
		
		//print all processes
		for(Process process : processes) {

			System.out.print(" ("  + process.arrivalTime + " " + process.cpuBurst + " " + process.cpuTime + " " + process.multiplier + ") ");

		}
		
		System.out.println();
		
		//sort processes by arrival time
		Collections.sort(processes);
		
		System.out.print("The (sorted) input is: " + processes.size());
		
		//print all processes
		for(Process process : processes) {
			
			System.out.print(" ("  + process.arrivalTime + " " + process.cpuBurst + " " + process.cpuTime + " " + process.multiplier + ") ");
			
		}
		
		System.out.println();
		
	}

}
