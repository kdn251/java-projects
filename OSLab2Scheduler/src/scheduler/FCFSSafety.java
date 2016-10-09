import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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
			while((!ready.isEmpty())) {
				
				//store current process
				Process currentProcess = ready.remove();
				
				int burst = randomOS(currentProcess.cpuBurst);
				
				//if the burst is greater than remaining cpu time set burst equal to remaining cpu time
				if(burst > currentProcess.cpuTime) {
					
					burst = currentProcess.cpuTime;
					
				}
				
				//decrement current processes' required cpu time by its current cpu burst
				currentProcess.cpuTime -= burst;
				
				//increment current process' finishing time
				currentProcess.finishingTime += burst;
				
				//System.out.println("Finishing time is currently: " + currentProcess.finishingTime);

				//if the required cpu time of current process is 0
				if(currentProcess.cpuTime <= 0) {
					
					//add current process to terminated processes
					terminatedProcesses.add(currentProcess);
					
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

					System.out.println("blockedTime is " + currentProcess.inputOutputTime);

					//increment current process' total I/O time
					//currentProcess.totalInputOutputTime += burst * currentProcess.multiplier;

					//increment waiting time and finishing time of every process in ready
					for(Process process : ready) {

						process.waitingTime += burst;
						process.finishingTime += burst;

						//System.out.println("Finishing time is currently: " + currentProcess.finishingTime);


						//TEST
						//System.out.println("Burst is: " + burst);
						//System.out.println("current process' total finishing time: " + process.totalFinishingTime);

					}				

					//update and check attributes of each process in blocked
					for(Process process : blocked) {

						//if the process' I/O time has finished
						if(process.inputOutputTime <= burst) {

							process.inputOutputTime = 0;

							//increment current process' finishing time
							process.finishingTime += burst;

							//System.out.println("Finishing time is currently: " + currentProcess.finishingTime);

							process.totalInputOutputTime += burst;

							blocked.remove(process);

							ready.add(process);

						}

						//decrement processes' I/O time by the burst
						else {

							process.inputOutputTime -= burst;

							process.totalInputOutputTime += burst;

							//increment current process' finishing time
							process.finishingTime += burst;

							//System.out.println("Finishing time is currently: " + currentProcess.finishingTime);


						}

					}				

					//if current process' I/O time is greater than 0
					if(currentProcess.inputOutputTime > 0) {

						//add current process to blocked queue
						blocked.add(currentProcess);				

					}

					//if current process has no I/O blast
					else {

						ready.add(currentProcess);

					}


					//System.out.println("Finishing time is currently (after while loop): " + currentProcess.finishingTime);
				}
			}
			
			//continue decrementing I/O time of all processes in blocked until ready is no longer empty
			if(!blocked.isEmpty()) {
				
				for(Process process : blocked) {
					
					process.finishingTime += 1;
					process.inputOutputTime -= 1;
					
					if(process.inputOutputTime == 0) {
						
						ready.add(process);
						
					}
					
					
				}
				
			}
			
			
		}
		
		//print formated output of first come first serve scheduling algorithm
		printFirstComeFirstServe(terminatedProcesses);
		
		 
	}