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
	
	public static void main(String[] args) {

		//declare scanner for input file
		Scanner scanner;
		
		//declare scanner for random-numbers file
		Scanner randomOSScanner = null;
		
		
		try {
			
			//initialize scanner to try and read file
			scanner = new Scanner(new File(args[0]));
			
			//initialize scanner to try and read random-numbers file
			randomOSScanner = new Scanner(new File("random-numbers"));
			
		}
		catch (Exception allExceptions) {
			
			//catch except and exit gracefully
			scanner = null;
			System.out.println("Please enter a file name via the command line and ensure that the random-numbers file is in the current directory.");
			System.exit(-1);
			
		}
		
		//initialize array list to store all processes	
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
				
			}
			


			
		}
		
		//sort processes by arrival time
		Collections.sort(processes);
		
		//print all processes
		for(int i = 0; i < processes.size(); i++) {
			
				System.out.println("Process " + i + ": "  + processes.get(i).arrivalTime + ", " + processes.get(i).cpuBurst + ", " + processes.get(i).cpuTime + ", " + processes.get(i).multiplier);
			
		}
		
		System.out.println();
		
		//run first come first serve scheduling algorithm on all processes
		firstComeFirstServe(processes, randomOSScanner);
	
	}
	
	
	public static void firstComeFirstServe(ArrayList<Process> processes, Scanner randomOSScanner) {
		
		//initialize an array list for terminated processes
		ArrayList<Process> terminatedProcesses = new ArrayList<Process>();
		
		//create queue for ready processes
		Queue<Process> ready = new LinkedList<Process>();
		
		//create queue for blocked processes
		Queue<Process> blocked = new LinkedList<Process>();
		
		//add all processes to read queue
//		for(Process process : processes) {
//			
//			ready.add(process);
//			
//		}
		
		//add the first process to ready
		ready.add(processes.get(0));
		processes.get(0).added = true;
		
		//if any process has the same arrival time as the first process add it to ready
		for(int i = 1; i < processes.size(); i++) {
			
			if(processes.get(i).arrivalTime == processes.get(0).arrivalTime) {
				
				ready.add(processes.get(i));
				processes.get(i).added = true;
				
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
								
				int burst = randomOS(currentProcess.cpuBurst, randomOSScanner);
				
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
				
				System.out.println("TOTAL RUN TIME IS: " + totalRunTime);
				
				//increment waiting time and finishing time of every process in ready
				for(Process process : ready) {

					process.waitingTime += burst;
					
					//System.out.println("waiting time: " + process.waitingTime);
					
					process.finishingTime += burst;

					//System.out.println("Finishing time is currently: " + currentProcess.finishingTime);


					//TEST
					//System.out.println("Burst is: " + burst);
					//System.out.println("current process' total finishing time: " + process.totalFinishingTime);

				}				
				
				//create an array list of items to remove from blocked
				ArrayList<Process> blockedToRemove = new ArrayList<Process>();
								
				
				//System.out.println("BLOCKED SIZE IS: " + blocked.size());

				//update and check attributes of each process in blocked
				for(Process process : blocked) {

					//if the process' I/O time has finished
					if(process.inputOutputTime <= burst) {

						process.inputOutputTime = 0;

						//increment current process' finishing time
						process.finishingTime += burst;

						//System.out.println("Finishing time is currently: " + currentProcess.finishingTime);

						//process.totalInputOutputTime += burst;

						//blocked.remove(process);
						blockedToRemove.add(process);

						ready.add(process);

					}

					//decrement processes' I/O time by the burst
					else {

						process.inputOutputTime -= burst;

						//process.totalInputOutputTime += burst;

						//increment current process' finishing time
						process.finishingTime += burst;

						//System.out.println("Finishing time is currently: " + currentProcess.finishingTime);


					}

				}
				
				//remove processes from blocked that have finished their I/O
				blocked.removeAll(blockedToRemove);
				
				//System.out.println("BLOCKED SIZE IS NOW: " + blocked.size());

				
				//check if any other processes have arrived
				for(Process process : processes) {
					
					System.out.println("PROCESS ADDED ATTRIBUTE IS " + process.added);
					if(process.arrivalTime <= totalRunTime && process.added != true) {
						System.out.println("KEVINKEVINKEVINKEVINKEVINKEVINKEVINKEVINKEVINKEVINKEVINKEVINKEVINKEVINKEVINKEVINKEVINKEVINKEVINKEVINKEVINKEVIN");
						ready.add(process);
						process.finishingTime = totalRunTime;
						process.waitingTime = totalRunTime - process.arrivalTime; //BUG: if process arrives while a process is running, set it's waiting time accordingly
						System.out.println("WAIT TIME IS: " + process.waitingTime);
						process.added = true;
						
					}
					
				}
				
				//System.out.println("Finishing time is currently: " + currentProcess.finishingTime);

				//if the required cpu time of current process is 0
				if(currentProcess.cpuTime <= 0) {
					
					//add current process to terminated processes
					//terminatedProcesses.add(currentProcess);
					
					//remove current process from ready list
					//ready.remove(currentProcess);
					
					//increment finished processes
					finishedProcesses++;
					
					//if all processes have terminated break from the while loop
					if(finishedProcesses == processes.size()) {
						
						break;
						
					}
					
					//TEST
					//System.out.println("number of finished processes: " + finishedProcesses);
					
				}
				
				else {

					//set current processes' I/O time to burst times its multiplier
					currentProcess.inputOutputTime = burst * currentProcess.multiplier;
					
					currentProcess.totalInputOutputTime += currentProcess.inputOutputTime;

					//System.out.println("I/O time is " + currentProcess.inputOutputTime);
					
					System.out.println();

					//increment current process' total I/O time
					//currentProcess.totalInputOutputTime += burst * currentProcess.multiplier;				

					//if current process' I/O time is greater than 0
					if(currentProcess.inputOutputTime > 0) {

						//add current process to blocked queue
						blocked.add(currentProcess);				

					}

					//if current process has no I/O blast
					else {

						ready.add(currentProcess);

					}
				
				}


			}
			
			//continue decrementing I/O time of all processes in blocked until ready is no longer empty
			if(!blocked.isEmpty()) {
				
				ArrayList<Process> toRemoveBlocked = new ArrayList<Process>();
				
				//System.out.println("BLOCKED SIZE IS: " + blocked.size());
				
				for(Process process : blocked) {
					
					process.finishingTime++;
					//process.totalInputOutputTime++;
					process.inputOutputTime--;
					
					//increment total run time
					totalRunTime++;
					
					if(process.inputOutputTime == 0) {
						
						toRemoveBlocked.add(process);
						ready.add(process);
						
					}
					
					//check if any other processes have arrived
					for(Process currentProcess : processes) {
						
						if(currentProcess.arrivalTime <= totalRunTime && currentProcess.added != true) {
							System.out.println("HELLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
							ready.add(currentProcess);
							currentProcess.finishingTime = totalRunTime;
							currentProcess.waitingTime = totalRunTime - process.arrivalTime; //BUG: if process arrives while a process is running, set it's waiting time accordingly
							
							//TEST
							System.out.println("ARRIVAL TIME IS: " + currentProcess.arrivalTime);
							System.out.println("TOTAL RUN TIME IS: " + totalRunTime);
							System.out.println("WAIT TIME IS: " + currentProcess.waitingTime);
									
							currentProcess.added = true;
							
							
						}
						
					}
					
					
				}
				
				blocked.removeAll(toRemoveBlocked);
				//System.out.println("BLOCKED SIZE IS NOW: " + blocked.size());

				
			}
			
			
		}
		
		System.out.println("TERMINATED PROCESSES SIZE: " + processes.size());
		
		//print formated output of first come first serve scheduling algorithm
		printFirstComeFirstServe(processes);
		
		 
	}


	
	
	
	
	
	
	
	
	
	
	
	private static void printFirstComeFirstServe(ArrayList<Process> terminatedProcesses) {

		System.out.println();
		System.out.println();
		System.out.println();
		
		
		
		
		System.out.println("The scheduling algorithm used was First Come First Serve");
		
		System.out.println();
		
		//sort terminated processes by arrival time
		Collections.sort(terminatedProcesses);
		
		float averageTurnaroundTime = 0;
		float averageWaitingTime = 0;
		
		for(int i = 0; i < terminatedProcesses.size(); i++) {
			
			System.out.println("Process " + i + ":");
			System.out.println("(A, B, C, M) = (" + terminatedProcesses.get(i).arrivalTime + ", " + terminatedProcesses.get(i).cpuBurst + ", " + terminatedProcesses.get(i).cpuTime + ", " + terminatedProcesses.get(i).multiplier + ")");
			System.out.println("Finishing time: " + terminatedProcesses.get(i).finishingTime);
			System.out.println("Turnaround time: " + (terminatedProcesses.get(i).finishingTime - terminatedProcesses.get(i).arrivalTime));
			System.out.println("I/O time: " + terminatedProcesses.get(i).totalInputOutputTime);
			System.out.println("Waiting time: " + terminatedProcesses.get(i).waitingTime);
			System.out.println();
			
			averageTurnaroundTime += terminatedProcesses.get(i).finishingTime - terminatedProcesses.get(i).arrivalTime;
			averageWaitingTime += terminatedProcesses.get(i).waitingTime;
			
		}
		
		averageTurnaroundTime = averageTurnaroundTime / terminatedProcesses.size();
		averageWaitingTime = averageWaitingTime / terminatedProcesses.size();
		
		System.out.println("Summary Data:");
		System.out.println("Finishing Time: " + totalRunTime);
		System.out.println("CPU Utilization: ");
		System.out.println("I/O Utilization: ");
		System.out.println("Average turnaround time: " + System.out.format("%.6f",averageTurnaroundTime ));
		System.out.println("Average waiting time: " + System.out.format("%.6f", averageWaitingTime));
		
	}


	private static int randomOS(int cpuBurst, Scanner randomOSScanner) {
		
		//TEST
		//System.out.println("cpuBurst is " + cpuBurst);
		//System.out.println("number from random-numbers is " + randomOSScanner.nextInt());
		
		int randomNumber = randomOSScanner.nextInt();
		
		int burst = 1 + (randomNumber % cpuBurst);
		
		//System.out.println("NUMBER FROM RANDOM-NUMBERS IS " + randomNumber);
		
		//TEST
		//System.out.println("current CPU burst is " + burst);
		
		return burst;
		
	}
	

}
