package bankersAlgorithm;

import java.util.ArrayList;

public class Process {

    //declare number attribute
    protected int number;

    //declare resources attribute
    protected int[] resources;

    //declare claim attribute
    protected int[] claim;

    //declare request made attribute
    protected int[] requestsMade;

    //declare time taken attribute
    protected int timeTaken;

    //declare wait time attribute
    protected int waitTime;

    //declare computing attribute
    protected int computing;

    //declare boolean aborted attribute
    protected boolean aborted = false;

    //declare activities attribute
    protected ArrayList<Activity> activities = new ArrayList<Activity>();

    //constructor for process
    public Process(int number, int resources) {

        this.number = number;
        this.resources = new int[resources];

    }

    //constructor to copy a process
    public Process(Process toCopy) {

        this.number = toCopy.number;
        this.resources = toCopy.resources;
        this.claim = toCopy.claim;
        this.timeTaken = toCopy.timeTaken;
        this.waitTime = toCopy.waitTime;
        this.computing = toCopy.computing;
        this.aborted = toCopy.aborted;
        this.activities = toCopy.activities;

    }

    //getter for number
    public int getNumber() {

        return this.number;

    }

    //getter for resources
    public int[] getResources() {

        return this.resources;

    }

    //getter for time taken
    public int getTimeTake() {

        return this.timeTaken;

    }

    //getter for activities
    public ArrayList<Activity> getActivities() {

        return this.activities;

    }


}
