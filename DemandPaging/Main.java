
import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * Created by kdn251 on 11/28/16.
 */
public class Main {

    //initialize RAM to null
    static Page[] RAM = null;
    static HashMap<Integer, Process> map = null;
    static int QUANTUM = 3;
    static Scanner scanner = null;

    public static void main(String args[]) throws IOException {

        //store command line values
        int machineSize = Integer.parseInt(args[0]);
        int pageSize = Integer.parseInt(args[1]);
        int processSize = Integer.parseInt(args[2]);
        int jobMix = Integer.parseInt(args[3]);
        int references = Integer.parseInt(args[4]);
        String replacementAlgorithm = args[5];

        //initialize hash map to map integers to process objects
        map = new HashMap<Integer, Process>();

        //initialize scanner to read random numbers file

        //attempt to reinitialize scanner to read from random-number file
        try {

            scanner = new Scanner(new File("random-numbers.txt"));

        }

        //catch all exceptions...
        catch(Exception exception) {

            System.out.println("Please ensure that the random-number.txt file is within the same directory as main");
            System.exit(-1);

        }

        //initialize processes and place them into the hash map
        if(jobMix == 1) {

            for(int i = 1; i <= 1; i++) {

                map.put(i, new Process(i, 1, 0, 0, references));

            }

        }

        else if(jobMix == 2) {

            for(int i = 1; i <= 4; i++) {

                map.put(i, new Process(i, 1, 0, 0, references));

            }

        }

        else if(jobMix == 3) {

            for(int i = 1; i <= 4; i++) {

                map.put(i, new Process(i, 0, 0, 0, references));

            }

        }

        else {

            map.put(1, new Process(1, 0.75, 0.25, 0, references));
            map.put(2, new Process(2, 0.75, 0, 0.25, references));
            map.put(3, new Process(3, 0.75, 0.125, 0.125, references));
            map.put(4, new Process(4, 0.5, 0.125, 0.125, references));

        }

        //initialize RAM array to store pages
        RAM = new Page[machineSize / pageSize];

        //fill RAM with smallest possible value
        Arrays.fill(RAM, null);

        //echo intput to standar output
        echo(machineSize, pageSize, processSize, jobMix, references, replacementAlgorithm);

        //simulate demand paging
        paging(machineSize, pageSize, processSize, jobMix, references, replacementAlgorithm);

    }

    public static void echo(int machineSize, int pageSize, int processSize, int jobMix, int references, String replacementAlgorithm) {

        System.out.println("The machine size is " + machineSize);
        System.out.println("The page size is " + pageSize);
        System.out.println("The process size is " + processSize);
        System.out.println("The job mix number is " + jobMix);
        System.out.println("The number of references per page is " + references);
        System.out.println("The replacement algorithm is " + replacementAlgorithm);
        System.out.println();

    }

    public static void paging(int machineSize, int pageSize, int processSize, int jobMix, int references, String replacementAlgorithm) {

        //initialize queue for LRU replacement algorithm
        Queue<Page> pageQueue = new LinkedList<Page>();

        //initialize stack for FIFO replacement algorithm
        Stack<Page> pageStack = new Stack<Page>();

        int time = 1;

        while(time <= (map.size() * references)) {

            Iterator it = map.entrySet().iterator();

            while(it.hasNext()) {

                Map.Entry pair = (Map.Entry)it.next();
                Process currentProcess = (Process)pair.getValue();

                for(int i = 0; i < QUANTUM && currentProcess.numberOfReferences > 0; i++) {

                    int word = 0;

                    if(currentProcess.firstReference) {

                        //System.out.println("PROCESSSIZE" + processSize);
                        word = (111 * currentProcess.number) % processSize;
                        currentProcess.nextWord = word;
                        currentProcess.firstReference = false;

                    }

                    else {

                        word = (currentProcess.nextWord + processSize) % processSize;

                    }

                        int pageNumber = word / pageSize;

                        Page currentPage = new Page(pageNumber, currentProcess.number);

                        int frameIndex = contains(RAM, currentPage);

                        //if the page table contains the current page...
                        if(frameIndex >= 0) {

                            //System.out.println(currentProcess.number + " references word " + word + " at time " + time + " Frame index: " + frameIndex);

                            //if replacement algorithm is LRU...
                            if(replacementAlgorithm.equalsIgnoreCase("LRU")) {

                                Iterator<Page> iterator = pageQueue.iterator();

                                //check if the queue already contains the current page, if it does, remove it
                                while (iterator.hasNext()) {

                                    Page page = iterator.next();

                                    if (currentPage.equals(page)) {

                                        iterator.remove();

                                    }

                                }

                                //add current page back to queue so that it is not the LRU
                                pageQueue.add(currentPage);

                            }

                        }

                        else {

                            int possiblyEvictedFrame = add(RAM, currentPage, pageQueue, pageStack, replacementAlgorithm, time);

                            //UNCOMMENT FOR DEBUGGING
                            //System.out.println(currentProcess.number + " references word " + word +  " at time " + time + ": Evicted frame: " + possiblyEvictedFrame);

                        }


                        //determine next word based on a, b, c
                        int nextRandom = scanner.nextInt();

                        //UNCOMMENT FOR DEBUGGING
                        //  System.out.println("Random: " + nextRandom);

                        double y = nextRandom / (Integer.MAX_VALUE + 1d);

                        if(y < currentProcess.a) {

                            currentProcess.nextWord = (currentProcess.nextWord + 1) % processSize;

                        }

                        else if(y < currentProcess.a + currentProcess.b) {

                            currentProcess.nextWord = (currentProcess.nextWord - 5) % processSize;

                        }

                        else if(y < currentProcess.a + currentProcess.b + currentProcess.c) {

                            currentProcess.nextWord = (currentProcess.nextWord + 4) % processSize;

                        }

                        else {

                            int nextRandomInt = scanner.nextInt();
                            currentProcess.nextWord = nextRandomInt % processSize;

                        }

                    //decrement number of references remaining for the current process
                    currentProcess.numberOfReferences--;

                    //increment time
                    time++;

                }

            }

        }

        //initialize values for final calculations
        int totalPageFaults = 0;
        float totalEvictions = 0;
        float averageResidencyTime = 0;

        //iterate through hash map and increment values for final calculations
        for(Map.Entry<Integer, Process> entry : map.entrySet()) {

            totalPageFaults += entry.getValue().numberOfPageFaults;
            totalEvictions += entry.getValue().numberOfEvictions;
            averageResidencyTime += entry.getValue().residencyTime;


            //print results
            if(entry.getValue().numberOfEvictions == 0) {

                System.out.println("Process " + entry.getValue().number + " had " + entry.getValue().numberOfPageFaults + " faults. With no evictions, the average residence is undefined.");

            }

            //print results
            else {

                System.out.println("Process " + entry.getValue().number + " had " + entry.getValue().numberOfPageFaults + " faults and " + (entry.getValue().residencyTime / entry.getValue().numberOfEvictions) + " average residency");

            }

        }

        //print results
        if(totalEvictions == 0) {

            System.out.printf("\nThe total number of faults is %d. With no evictions, the overage average residency is undefined\n", totalPageFaults);

        }

        //print results
        else {

            averageResidencyTime = averageResidencyTime / totalEvictions;

            System.out.printf("\nThe total number of faults is %d and the overage average residency is %f\n", totalPageFaults, averageResidencyTime);

        }

    }

    public static int contains(Page[] RAM, Page currentPage) {

        //iterate through RAM searching for current page
        for(int i = 0; i < RAM.length; i++) {

            if(RAM[i] == null) continue;
            //if the page is found
            if(RAM[i].processNumber == currentPage.processNumber && RAM[i].pageNumber == currentPage.pageNumber) {

                currentPage.loadTime = RAM[i].loadTime;

                return i;

            }

        }

        map.get(currentPage.processNumber).numberOfPageFaults++;

        //page was not found
        return -1;

    }

    public static int add(Page[] RAM, Page currentPage, Queue pageQueue, Stack<Page> pageStack, String replacementAlgorithm, int time) {

        //iterate through RAM looking for a free frame
        for(int i = RAM.length - 1; i >= 0; i--) {

            //if the current space in RAM is available
            if(RAM[i] == null) {

                RAM[i] = currentPage;

                if(replacementAlgorithm.equalsIgnoreCase("LRU")) {

                    pageQueue.add(currentPage);

                }

                if(replacementAlgorithm.equalsIgnoreCase("LIFO")) {

                    pageStack.add(currentPage);

                }

                currentPage.loadTime = time;

                return i;

            }

        }

        return evict(RAM, currentPage, pageQueue, pageStack, replacementAlgorithm, time);

    }

    public static int evict(Page[] RAM, Page currentPage, Queue<Page> pageQueue, Stack<Page> pageStack, String replacementAlgorithm, int time) {

        //check if the replacement algorithm is LRU
        if(replacementAlgorithm.equalsIgnoreCase("LRU")) {

            //find the page to remove from RAM
            Page pageToRemove = pageQueue.poll();

            //iterate through RAM to find the page to remove
            for(int i = 0; i < RAM.length; i++) {

                //place current page in the victim's index
                if(RAM[i].processNumber == pageToRemove.processNumber && RAM[i].pageNumber == pageToRemove.pageNumber) {

                    //System.out.println("process number: " + pageToRemove.processNumber);
                    map.get(pageToRemove.processNumber).numberOfEvictions++;

                    RAM[i] = currentPage;

                    currentPage.loadTime = time;

                    map.get(pageToRemove.processNumber).residencyTime += (time - pageToRemove.loadTime);

                    pageQueue.add(currentPage);

                    return i;

                }

            }

            //error
            return -1;

        }

        //check if the replacement algorithm is LIFO
        else if(replacementAlgorithm.equalsIgnoreCase("LIFO")) {

            Page pageToRemove = pageStack.pop();

            for(int i = 0; i < RAM.length; i++) {

                if(RAM[i].processNumber == pageToRemove.processNumber && RAM[i].pageNumber == pageToRemove.pageNumber) {

                    map.get(pageToRemove.processNumber).numberOfEvictions++;

                    RAM[i] = currentPage;

                    currentPage.loadTime = time;

                    map.get(pageToRemove.processNumber).residencyTime += (time - pageToRemove.loadTime);

                    pageStack.add(currentPage);

                    return i;

                }

            }

            //error
            return -1;

        }

        //check if the replacement algorithm is Random
        else if(replacementAlgorithm.equalsIgnoreCase("Random")) {

            int randomVictim = scanner.nextInt() % RAM.length;

            Page pageToRemove = RAM[randomVictim];

            map.get(pageToRemove.processNumber).numberOfEvictions++;
            map.get(pageToRemove.processNumber).residencyTime += (time - pageToRemove.loadTime);

            RAM[randomVictim] = currentPage;

            currentPage.loadTime = time;

            return randomVictim;

        }

        //otherwise, return -1 (error)
        return -1;

    }


}
