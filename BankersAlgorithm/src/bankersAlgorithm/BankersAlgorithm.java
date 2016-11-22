package bankersAlgorithm;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class BankersAlgorithm {

    // declare global variables
    static int NUMBER_OF_TASKS;
    static int NUMBER_OF_RESOURCE_TYPES;
    static int[] resources;
    static ArrayList<Process> processes;

    public static void main(String[] args) {

        Scanner scanner = initializeScanner(args);

        // read input and initialize processes
        processes = readInput(scanner);

        // initialize optimistic processes array list
        ArrayList<Process> optimisticProcesses = new ArrayList<Process>();

        // print processes
        //printProcesses(processes);

        try {

            // run optimisti cc manager on all processes
            optimisticProcesses = optimisticManager(processes);

        }

        catch(Exception exception) {

            System.out.println();
            System.out.println("Please go easy on me. :)");
            System.out.println();

        }

        // print the result of running the optimistic manager
        printOptimisticManager(optimisticProcesses);

        // reinitialize scanner
        scanner = initializeScanner(args);

        // reread input
        processes = readInput(scanner);

        // store processes in bankers processes array list
        ArrayList<Process> bankersProcesses = new ArrayList<Process>();

        try {

            // run Dijkstra' banker's algorithm
            bankersProcesses = bankersAlgorithm(processes);

        }

        catch (Exception exception) {

            System.out.println();
            System.out.println("Please go easy on me. :)");
            System.out.println();

        }

        // print the results of running Dijkstra's banker's algorithm
        printBankersAlgorithm(bankersProcesses);

    }

    private static void printBankersAlgorithm(ArrayList<Process> bankersProcesses) {

        // print header
        System.out.println(String.format("%17s", "Banker's"));

        // initialize total run time to zero
        int totalRunTime = 0;

        // initialize total wait time to zero
        int totalWaitTime = 0;

        // iterate through all processes
        for (int i = 0; i < bankersProcesses.size(); i++) {

            // if the current process was aborted as a result of a deadlock...
            if (bankersProcesses.get(i).aborted) {

                System.out.println("Task " + (i + 1) + "\t\taborted");

            }

            // if the current process successfully terminated
            else {

                // increment total time taken
                totalRunTime += bankersProcesses.get(i).timeTaken;

                // increment total wait time
                totalWaitTime += bankersProcesses.get(i).waitTime;

                // print output for current task
                System.out.format("Task " + (bankersProcesses.get(i).number) + "\t\t%d\t%d\t%.0f%s",
                        bankersProcesses.get(i).timeTaken, bankersProcesses.get(i).waitTime,
                        ((float) bankersProcesses.get(i).waitTime / (float) bankersProcesses.get(i).timeTaken) * 100,
                        "%");
                System.out.println();

            }

        }

        // print total runtime, total wait time, and overall percentage spent
        // waiting
        System.out.format("total \t\t%d\t%d\t%.0f%s", totalRunTime, totalWaitTime,
                ((float) totalWaitTime / (float) totalRunTime * 100), "%");

        // print line breaks
        System.out.println();
        System.out.println();

    }

    private static ArrayList<Process> bankersAlgorithm(ArrayList<Process> bankersProcesses) {

        // initialize array list of finished processes
        ArrayList<Process> finishedProcesses = new ArrayList<Process>();

        // iterate through all processes checking if any process' initial claims
        // exceeds the total number of any resource
        for (int i = 0; i < bankersProcesses.size(); i++) {

            // iterate through current process' claim array
            for (int j = 0; j < bankersProcesses.get(i).claim.length; j++) {

                // if the claim exceeds a specific resource...
                if (bankersProcesses.get(i).claim[j] > resources[j]) {

                    // print informative error messages
                    System.out.println("Banker aborts task " + bankersProcesses.get(i).number + " before run begins:");
                    System.out.println(" claim for resource " + (j + 1) + " (" + bankersProcesses.get(i).claim[j]
                            + ") exceeds number of units present " + "(" + resources[j] + ")");

                    // abort the current process
                    bankersProcesses.get(i).aborted = true;

                    // remove current process from bankers processes
                    bankersProcesses.remove(bankersProcesses.get(i));

                    // add the current process to finished process list
                    finishedProcesses.add(bankersProcesses.get(i));

                    // decrement i
                    i--;

                }

            }

        }

        // initialize cycle
        int cycle = 0;

        // initialize index variable
        int i = 0;

        // store initial size of processes
        int processesSize = bankersProcesses.size();

        // initialize wait queue for requests that cannot be processed at a
        // particular cycle
        ArrayList<Process> waitQueue = new ArrayList<Process>();

        // initialize buffer to hold resources released at cycle n to make them
        // available at cycle n + 1
        int[] resourceBuffer = new int[NUMBER_OF_RESOURCE_TYPES];

        // continue while all processes have not terminated or deadlock has not
        // occurred
        while (finishedProcesses.size() != processesSize) {

            // loop through all processes
            while (i < bankersProcesses.size()) {

                //if the process is computing...
                if (bankersProcesses.get(i).computing > 0) {

                    // decrement current process' remaining computing time
                    bankersProcesses.get(i).computing--;

                    // increment i
                    i++;

                }

                else {

                    // store current activity
                    Activity currentActivity = bankersProcesses.get(i).activities.get(0);

                    // process the current activity based on its type
                    switch (currentActivity.activityType) {

                        case "initiate":

                            // remove the current activity
                            bankersProcesses.get(i).activities.remove(0);

                            break;

                        case "request":

                            // add back resources that were released during the
                            // previous cycle
                            for (int k = 0; k < resourceBuffer.length; k++) {

                                resources[k] += resourceBuffer[k];
                                resourceBuffer[k] = 0;

                            }

                            if ((bankersProcesses.get(i).resources[currentActivity.resourceType - 1] + currentActivity.info) > bankersProcesses.get(i).claim[currentActivity.resourceType- 1]) {

                                bankersProcesses.get(i).aborted = true;

                                for (int k = 0; k < bankersProcesses.get(i).resources.length; k++) {

                                    System.out.println("During cycle " + cycle + "-" + (cycle + 1) + " of Banker's algorithm " + "Task " + bankersProcesses.get(i).number
                                            + "'s request exceeds its claim");

                                    resourceBuffer[k] += bankersProcesses.get(i).resources[k];
                                    bankersProcesses.get(i).resources[k] = 0;

                                }

                                finishedProcesses.add(bankersProcesses.get(i));
                                bankersProcesses.remove(bankersProcesses.get(i));
                                i--;

                            }

                            else {

                                // check if the state would be safe if the current
                                // request is granted
                                boolean safe = isSafe(bankersProcesses.get(i), bankersProcesses, waitQueue, finishedProcesses);

                                // if the state would be safe..
                                if (safe) {

                                    // grant the request of the current process
                                    bankersProcesses.get(i).resources[currentActivity.resourceType
                                            - 1] += currentActivity.info;
                                    resources[currentActivity.resourceType - 1] -= currentActivity.info;

                                    // remove the current activity
                                    bankersProcesses.get(i).activities.remove(0);

                                    break;

                                }

                                // if the state would not be safe...
                                else {

                                    // add the current process to the wait queue
                                    waitQueue.add(bankersProcesses.get(i));
                                    bankersProcesses.remove(bankersProcesses.get(i));

                                    //decrement i
                                    i--;

                                    break;

                                }

                            }

                        case "release":

                            // place resource to be released into the resource
                            // buffer
                            resourceBuffer[currentActivity.resourceType - 1] += currentActivity.info;

                            // decrement the resource count of the resource that the
                            // current process is releasing
                            bankersProcesses.get(i).resources[currentActivity.resourceType - 1] -= currentActivity.info;

                            // remove the current activity
                            bankersProcesses.get(i).activities.remove(0);

                            break;

                        case "compute":

                            // set current process' computing time
                            bankersProcesses.get(i).computing = currentActivity.info - 1;

                            // remove the current activity
                            bankersProcesses.get(i).activities.remove(0);

                            break;

                        case "terminate":

                            // set current process' time taken to the current cycle
                            bankersProcesses.get(i).timeTaken = cycle;

                            // update terminated processes count
                            finishedProcesses.add(bankersProcesses.get(i));

                            // release all of current process' resources
                            for (int k = 0; k < bankersProcesses.get(i).resources.length; k++) {

                                resourceBuffer[k] += bankersProcesses.get(i).resources[k];
                                bankersProcesses.get(i).resources[k] = 0;

                            }

                            // remove the current activity
                            bankersProcesses.get(i).activities.remove(0);

                            // remove current process
                            bankersProcesses.remove(i);

                            // decrement i
                            i--;

                            break;
                    }

                    // increment i
                    i++;

                }

            }

            // reset i to zero
            i = 0;

            // increment cycle
            cycle++;

            // increment wait time of all processes on wait queue
            incrementWaitTime(waitQueue);

            // check if any processes on the wait queue's requests can be
            // satisfied
            checkWaitQueueBankers(bankersProcesses, waitQueue);

        }

        // return the array list of finished processes
        return finishedProcesses;

    }

    private static boolean isSafe(Process currentProcess, ArrayList<Process> bankersProcesses,
                                  ArrayList<Process> waitQueue, ArrayList<Process> finishedProcesses) {

        // initialize bankers processes copy
        ArrayList<Process> bankersProcessesCopy = new ArrayList<Process>();

        // initialize resources copy
        int[] resourcesCopy = new int[NUMBER_OF_RESOURCE_TYPES];

        // initialize wait queue copy
        ArrayList<Process> waitQueueCopy = new ArrayList<Process>();

        // initialize finished processes copy
        ArrayList<Process> finishedProcessesCopy = new ArrayList<Process>();

        // copy each process from bankers processes and add them to bankers
        // processes copy
        for (int i = 0; i < bankersProcesses.size(); i++) {

            bankersProcessesCopy.add(new Process(bankersProcesses.get(i)));

        }

        // copy each resource to the resources copy
        for (int i = 0; i < resources.length; i++) {

            resourcesCopy[i] = resources[i];

        }

        // copy each process in the wait queue to wait queue copy
        for (int i = 0; i < waitQueue.size(); i++) {

            waitQueueCopy.add(waitQueue.get(i));

        }

        // copy each process to the finished processes copy
        for (int i = 0; i < finishedProcesses.size(); i++) {

            finishedProcessesCopy.add(finishedProcesses.get(i));

        }

        for (int i = 0; i < bankersProcessesCopy.size(); i++) {

            for (int j = 0; j < bankersProcessesCopy.get(i).resources.length; j++) {

                //System.out.println("Resources Copy: " + resourcesCopy[j]);
                int largestRequest = bankersProcesses.get(i).claim[j] - bankersProcesses.get(i).resources[j];

                if (resourcesCopy[j] - largestRequest < 0) {

                    return false;

                }

            }

        }

        return true;

    }

    private static void checkWaitQueueBankers(ArrayList<Process> bankersProcesses, ArrayList<Process> waitQueue) {

        // initialize stack to hold all processes that are to be removed from
        // the wait queue
        Stack<Process> stack = new Stack<Process>();

        // iterate through wait queue pushing all processes on to the stack
        for (int i = 0; i < waitQueue.size(); i++) {

            stack.push(waitQueue.remove(0));

        }

        // add all processes from stack back to the processes list at index zero
        while (!stack.isEmpty()) {

            bankersProcesses.add(0, stack.pop());

        }

    }

    private static Scanner initializeScanner(String[] args) {

        // initialize scanner to null
        Scanner scanner = null;

        try {

            // attempt to reinitialize scanner to read first command line
            // argument
            scanner = new Scanner(new File(args[0]));

        }

        // catch all exceptions
        catch (Exception exception) {

            // print error message
            System.out.println("Please ensure that the file is entered as the first command line argument");

            // exit gracefully
            System.exit(-1);

        }

        //return scanner
        return scanner;

    }

    private static void printProcesses(ArrayList<Process> processes2) {

        // iterate through all processes
        for (int i = 0; i < processes.size(); i++) {

            // print information about the current process
            for (int j = 0; j < processes.get(i).getActivities().size(); j++) {

                System.out.print(processes.get(i).activities.get(j).activityType + " ");
                System.out.print(processes.get(i).activities.get(j).processNumber + " ");
                System.out.print(processes.get(i).activities.get(j).resourceType + " ");
                System.out.print(processes.get(i).activities.get(j).info);

                System.out.println();

            }

            // print line break
            System.out.println();

        }

    }

    private static ArrayList<Process> readInput(Scanner scanner) {

        // initialize array list to hold all processes
        ArrayList<Process> processes = new ArrayList<Process>();

        // initialize number of tasks
        NUMBER_OF_TASKS = Integer.parseInt(scanner.next());

        // initialize number of resource types
        NUMBER_OF_RESOURCE_TYPES = Integer.parseInt(scanner.next());

        // initialize process objects
        for (int i = 0; i < NUMBER_OF_TASKS; i++) {

            Process currentProcess = new Process(i + 1, NUMBER_OF_RESOURCE_TYPES);
            currentProcess.claim = new int[NUMBER_OF_RESOURCE_TYPES];
            processes.add(currentProcess);

        }

        // initialize size of resource's array
        resources = new int[NUMBER_OF_RESOURCE_TYPES];

        //check arbitrary limits
        if(NUMBER_OF_TASKS > 10 || NUMBER_OF_RESOURCE_TYPES > 10) {

            System.out.println("Input file does not adhere to the arbitrary limits.");
            System.exit(-1);

        }

        // initialize number of all resource types
        for (int i = 0; i < resources.length; i++) {

            resources[i] = Integer.parseInt(scanner.next());

        }

        //keep track of the number of activites
        int numberOfActivities = 0;

        // initialize each process object
        while (scanner.hasNext()) {

            // store attributes of the current activity object
            String activityType = scanner.next();
            int processNumber = Integer.parseInt(scanner.next());
            int resourceType = Integer.parseInt(scanner.next());
            int info = Integer.parseInt(scanner.next());

            // create new activity object and add it to the current process'
            // activities
            Activity currentActivity = new Activity(activityType, processNumber, resourceType, info);

            if (currentActivity.activityType.equals("initiate")) {

                processes.get(processNumber - 1).claim[currentActivity.resourceType - 1] = currentActivity.info;

            }

            processes.get(processNumber - 1).activities.add(currentActivity);

            //increment the number of activites
            numberOfActivities++;

        }

        //check arbitrary limits
        if(numberOfActivities > 30) {

            System.out.println("Input file does not adhere to arbitrary limits.");
            System.exit(-1);

        }

        // return initialize array list
        return processes;

    }

    private static ArrayList<Process> optimisticManager(ArrayList<Process> processes) {

        // initialize array list of finished processes
        ArrayList<Process> finishedProcesses = new ArrayList<Process>();

        // initialize cycle
        int cycle = 0;

        // initialize index variable
        int i = 0;

        // store initial size of processes
        int processesSize = processes.size();

        // initialize wait queue for requests that cannot be processed at a
        // particular cycle
        ArrayList<Process> waitQueue = new ArrayList<Process>();

        // initialize buffer to hold resources released at cycle n to make them
        // available at cycle n + 1
        int[] resourceBuffer = new int[NUMBER_OF_RESOURCE_TYPES];

        // continue while all processes have not terminated or deadlock has not
        // occurred
        while (finishedProcesses.size() != processesSize) {

            // loop through all processes
            while (i < processes.size()) {

                if (processes.get(i).computing > 0) {

                    // decrement current process' remaining computing time
                    processes.get(i).computing--;

                    // increment i
                    i++;

                }

                else {

                    // store current activity
                    Activity currentActivity = processes.get(i).activities.get(0);

                    // process the current activity based on its type
                    switch (currentActivity.activityType) {

                        case "initiate":

                            // remove the current activity
                            processes.get(i).activities.remove(0);

                            break;

                        case "request":

                            // if we are not able to satisfy the current process'
                            // request...
                            if (resources[currentActivity.resourceType - 1] < currentActivity.info) {

                                // add current process to wait queue
                                waitQueue.add(processes.get(i));

                                // remove current process from list of processes
                                processes.remove(i);

                                // decrement i
                                i--;

                                break;

                            }

                            // we are able to satisfy the current process' request
                            else {

                                // redistribute resource
                                processes.get(i).resources[currentActivity.resourceType - 1] += currentActivity.info;
                                resources[currentActivity.resourceType - 1] -= currentActivity.info;

                                // remove the current activity
                                processes.get(i).activities.remove(0);

                                break;

                            }

                        case "release":

                            // place resource to be released into the resource
                            // buffer
                            resourceBuffer[currentActivity.resourceType - 1] += currentActivity.info;

                            // decrement the resource count of the resource that the
                            // current process is releasing
                            processes.get(i).resources[currentActivity.resourceType - 1] -= currentActivity.info;

                            // remove the current activity
                            processes.get(i).activities.remove(0);

                            break;

                        case "compute":

                            // set current process' computing time
                            processes.get(i).computing = currentActivity.info - 1;

                            // remove the current activity
                            processes.get(i).activities.remove(0);

                            break;

                        case "terminate":

                            // set current process' time taken to the current cycle
                            processes.get(i).timeTaken = cycle;

                            // update terminated processes count
                            finishedProcesses.add(processes.get(i));

                            // release all of current process' resources
                            for (int k = 0; k < processes.get(i).resources.length; k++) {

                                resourceBuffer[k] += processes.get(i).resources[k];
                                processes.get(i).resources[k] = 0;

                            }

                            // remove the current activity
                            processes.get(i).activities.remove(0);

                            // remove current process
                            processes.remove(i);

                            // decrement i
                            i--;

                            break;
                    }

                    // increment i
                    i++;

                }

            }

            // reset i to zero
            i = 0;

            // increment cycle
            cycle++;

            // increment wait time of all processes on wait queue
            incrementWaitTime(waitQueue);

            // add back resources that were released during the previous cycle
            for (int k = 0; k < resourceBuffer.length; k++) {

                resources[k] += resourceBuffer[k];
                resourceBuffer[k] = 0;

            }

            // check if any processes on the wait queue's requests can be
            // satisfied
            checkWaitQueue(waitQueue);

            // determine whether or not we are deadlocked
            boolean deadlocked = (finishedProcesses.size() != processesSize && processes.size() == 0) ? true : false;

            // if we are currently deadlocked...
            if (deadlocked) {

                // attempt to correct the current deadlock
                correctDeadlock(deadlocked, waitQueue, finishedProcesses, processesSize);

            }

        }

        // return the array list of finished processes
        return finishedProcesses;

    }

    private static void incrementWaitTime(ArrayList<Process> waitQueue) {

        // increment the wait time of all processes on the wait list by one
        for (int i = 0; i < waitQueue.size(); i++) {

            waitQueue.get(i).waitTime++;

        }

    }

    private static void checkWaitQueue(ArrayList<Process> waitQueue) {

        // initialize stack to hold all processes to be removed from the wait
        // queue
        Stack<Process> stack = new Stack<Process>();

        // iterate through all processes in wait queue checking if we can
        // satisfy each process' request
        for (int k = 0; k < waitQueue.size(); k++) {

            // store the desired index
            int currentIndex = waitQueue.get(k).activities.get(0).resourceType;

            // account for off by one error
            if (currentIndex > 0) {

                currentIndex--;

            }

            // if we can satisfy the current process' request
            if (resources[currentIndex] >= waitQueue.get(k).activities.get(0).info) {

                // remove current process from the wait queue and add it to the
                // stack
                stack.push(waitQueue.remove(k));

            }

        }

        // push all items from the stack back to the process list
        while (!stack.isEmpty()) {

            processes.add(0, stack.pop());

        }

    }

    private static void correctDeadlock(boolean deadlocked, ArrayList<Process> waitQueue,
                                        ArrayList<Process> finishedProcesses, int processesSize) {

        // while deadlocked continuously abort processes
        while (deadlocked) {

            // abort process with the lowest process number and release its
            // resources
            abortProcess(waitQueue, finishedProcesses);

            // now that a process has been aborted, check if any of the requests
            // on the wait queue can be satisfied
            checkWaitQueue(waitQueue);

            // reassign deadlock
            deadlocked = (finishedProcesses.size() != processesSize && processes.size() == 0) ? true : false;

        }

    }

    private static void abortProcess(ArrayList<Process> waitQueue, ArrayList<Process> finishedProcesses) {

        // keep track of minimum
        int min = 0;

        // iterate through processes finding the smallest process number
        for (int k = 0; k < waitQueue.size(); k++) {

            // reassign minimum index
            if (waitQueue.get(k).number < waitQueue.get(min).number) {

                min = k;

            }

        }

        // iterate through process with the lowest process number's resource
        // array
        for (int k = 0; k < waitQueue.get(min).resources.length; k++) {

            // redistribute resources
            resources[k] += waitQueue.get(min).resources[k];
            waitQueue.get(min).resources[k] = 0;

        }

        // mark current process as aborted
        waitQueue.get(min).aborted = true;

        // add aborted process to finished processes array list
        finishedProcesses.add(waitQueue.get(min));

        // remove current process from wait queue
        waitQueue.remove(min);

    }

    private static void printOptimisticManager(ArrayList<Process> optimisticProcesses) {

        // print header
        System.out.println(String.format("%17s", "FIFO"));

        // initialize total run time to zero
        int totalRunTime = 0;

        // initialize total wait time to zero
        int totalWaitTime = 0;

        // iterate through all processes
        for (int i = 0; i < optimisticProcesses.size(); i++) {

            // if the current process was aborted as a result of a deadlock...
            if (optimisticProcesses.get(i).aborted) {

                System.out.println("Task " + (i + 1) + "\t\taborted");

            }

            // if the current process successfully terminated
            else {

                // increment total time taken
                totalRunTime += optimisticProcesses.get(i).timeTaken;

                // increment total wait time
                totalWaitTime += optimisticProcesses.get(i).waitTime;

                // print output for current task
                System.out.format("Task " + (optimisticProcesses.get(i).number) + "\t\t%d\t%d\t%.0f%s",
                        optimisticProcesses.get(i).timeTaken, optimisticProcesses.get(i).waitTime,
                        ((float) optimisticProcesses.get(i).waitTime / (float) optimisticProcesses.get(i).timeTaken)
                                * 100,
                        "%");
                System.out.println();

            }

        }

        // print total runtime, total wait time, and overall percentage spent
        // waiting
        System.out.format("total \t\t%d\t%d\t%.0f%s", totalRunTime, totalWaitTime,
                ((float) totalWaitTime / (float) totalRunTime * 100), "%");

        // print line breaks
        System.out.println();
        System.out.println();

    }

}
