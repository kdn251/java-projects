package scheduler;

public class Process implements Comparable<Process> {
	
	boolean added = false;
	
	//arrival time of process
	protected int arrivalTime;
	
	//cpu burst of process
	protected int cpuBurst;
	
	//total cpu time required by process
	protected int cpuTime;
	
	//current burst time of process
	protected int burst;
	
	//current blocked time of process
	protected int blocked;
	
	//copy of cpu time remaining
	protected int cpuTimeCopy;
	
	//multiplier of process
	protected int multiplier;
	
	//finishing time of process
	protected int finishingTime;
	
	//total finishing time of process
	protected int totalFinishingTime;
	
	//turnaround time of process
	protected int turnaroundTime;
	
	//input/ouput time of process
	protected int inputOutputTime;
	
	//total input/output time of process
	protected int totalInputOutputTime;
	
	//waiting time of process
	protected int waitingTime;
	
	//current state of process
	protected String currentState = "unstarted";
	
	
	Process() {
		
		//default constructor
		
	}
	
	Process(int arrivalTime, int cpuBurst, int cpuTime, int multiplier) {
		
		//set arrival time attribute of process
		this.arrivalTime = arrivalTime;
		
		//set cpu burst attribute of process
		this.cpuBurst = cpuBurst;
		
		//set cpu time attribute of process
		this.cpuTime = cpuTime;
		
		//set multplier attribute of process
		this.multiplier = multiplier;
		
	}

	@Override
	public int compareTo(Process o) {
		
		//if arrival times are equal
		if(this.arrivalTime == o.arrivalTime) {
			
			return 0;
					
		}
		//if arrival time is earlier...
		else if(this.arrivalTime < o.arrivalTime) {
			
			return -1;
			
		}
		
		//if arrival time is later...
		else {
			
			return 1;
			
		}

	}
	
}
