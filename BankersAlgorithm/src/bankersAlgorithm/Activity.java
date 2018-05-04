package bankersAlgorithm;

public class Activity {
    // declare process number attribute
    protected int processNumber;
    // declare activity type attribute
    protected String activityType;
    //declare resource type attribute
    protected int resourceType;
    //declare info attribute
    protected int info;

    public Activity(String activityType, int processNumber, int resourceType, int info) {
        //initialize process number attribute
        this.processNumber = processNumber;
        //initialize resource type attribute
        this.resourceType = resourceType;
        //initialize info attribute
        this.info = info;

        //initialize activity object accordingly
        switch (activityType) {
            case "initiate":
                this.activityType = "initiate";
                break;
            case "request":
                this.activityType = "request";
                break;
            case "release":
                this.activityType = "release";
                break;
            case "compute":
                this.activityType = "compute";
                this.info = resourceType;
                break;
            case "terminate":
                this.activityType = "terminate";
                break;
            default:
                break;
        }
    }

    //getter for process number attribute
    public int getProcessNumber() {
        return this.processNumber;
    }

    //getter for activity attribute
    public String getActivityType() {
        return this.activityType;
    }

    //getter for info attribute
    public int getInfo() {
        return this.info;
    }

    //toString method used for debugging
    @Override
    public String toString(){
        switch (this.activityType){
            case "initiate":
                return this.activityType +" "+ this.processNumber +" "+ this.resourceType +" "+ this.info;
            case "request":
                return this.activityType +" "+ this.processNumber +" "+ this.resourceType +" "+ this.info;
            case "release":
                return this.activityType +" "+ this.processNumber +" "+ this.resourceType +" "+ this.info;
            case "compute":
                return this.activityType +" "+ this.processNumber +" "+ this.info;
            case "terminate":
                return this.activityType +" "+ this.processNumber ;
            default:
                return "";
        }
    }
}
