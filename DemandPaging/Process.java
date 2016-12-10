
/**
 * Created by kdn251 on 11/28/16.
 */
public class Process {

    protected int number;
    protected int size;
    protected int references;
    protected float numberOfEvictions;
    protected int numberOfPageFaults;
    protected float residencyTime;
    protected int numberOfReferences;
    protected int nextWord;
    protected double a;
    protected double b;
    protected double c;
    protected boolean firstReference = true;
    protected String algorithm;

    public Process() {

        //defualt contructor

    }

    public Process(int number, double a, double b, double c, int numberOfReferences){

        this.number = number;
        this.a = a;
        this.b = b;
        this.c = c;
        this.numberOfReferences = numberOfReferences;

    }

}
