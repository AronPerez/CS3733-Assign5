import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Assign5 {
    // Global variables
    static String schedulingAlg = "";
    static int numProcesses = 0, numRQJobs = 0, numIOJobs = 0, totalCPURunningTime = 0, quantum = 0;
    static long totalWaitingTimeInReadyQ = 0, readyClock = 0, totalReadyWaitingTime = 0, readyTATime = 0, globalStart = 0, globalEnd = 0, totalGlobal = 0;
    static boolean fileReadDone = false, cpuDone = false, ioDone = false, cpuBusy = false, ioBusy = false;
    static final Semaphore semCpu = new Semaphore(0), semIo = new Semaphore(0);
    static final DoublyLinkedList readyQ = new DoublyLinkedList(), ioQ = new DoublyLinkedList();
    static List < Node > terminated = Collections.synchronizedList(new ArrayList < Node > ());
    static List < Long > readyProcessTime = Collections.synchronizedList(new ArrayList < Long > ());

    public static void main(String[] args) {
        // Validate the number of arguments
        if (args.length < 4) {
            System.err.println("ERROR: Not enough arguments supplied!");
            System.err.println("Usage: -alg [FIFO|SJF|PR|RR] [-quantum [integer(ms)]] -input [input_file_name.txt]");
            System.err.println("Please verify your arguments and try again.");
            System.exit(1);
        }

        // Local variables
        String inputFileName = "";

        // Parse command line arguments
        for (int i = 0; i < args.length; i++) {
            if ("-alg".equals(args[i])) {
                String tempAlg = args[i + 1];
                if ("".equals(tempAlg)) {
                    System.err.println("ERROR: Missing Algorithm Value!");
                    System.exit(2);
                } else if ("FIFO".equals(tempAlg) || "SJF".equals(tempAlg) || "PR".equals(tempAlg) || "RR".equals(tempAlg)) {
                    schedulingAlg = tempAlg;
                } else {
                    System.err.println("ERROR: Invalid Algorithm Value!");
                    System.err.println("Usage: -alg [FIFO|SJF|PR|RR]");
                    System.exit(2);
                }
            } else if ("-quantum".equals(args[i])) {
                int tempQuantum = Integer.parseInt(args[i + 1]);
                if (tempQuantum != 0) {
                    quantum = tempQuantum;
                } else {
                    System.err.println("ERROR: Invalid Quantum Value!");
                    System.exit(2);
                }
            } else if ("-input".equals(args[i])) {
                String tempFileName = args[i + 1];
                if ("".equals(tempFileName)) {
                    System.err.println("ERROR: Missing File Name!");
                    System.exit(2);
                } else {
                    inputFileName = tempFileName;
                }
            }
        }

        // Create the threads
        globalStart = System.currentTimeMillis();
        Thread thFR = new Thread(new FileRead(inputFileName));
        Thread thCPU = new Thread(new CPUScheduler());
        Thread thIO = new Thread(new IOSystem());
        thFR.start();
        thCPU.start();
        thIO.start();
        try {
            thFR.join();
            thCPU.join();
            thIO.join();
        } catch (InterruptedException ie) {
            System.err.println("ERROR: Cannot join threads FR, CPU, and/or IO!");
            System.err.println("Please try again.");
            System.exit(3);
        }
        globalEnd = System.currentTimeMillis();
        totalGlobal = globalEnd - globalStart;

        //Calculate Performance Metrics
        calculateTotalCPURunningTime();
        double cpuUtilization = (double) totalCPURunningTime / sumReadyProcessTime() * 100;

        // Print the output
        System.out.println("------------------------------------------------------");
        System.out.println("Input File Name : " + inputFileName);
        if (quantum > 0) {
            System.out.println("CPU Scheduling Alg : " + schedulingAlg + " (" + quantum + ")");
        } else {
            System.out.println("CPU Scheduling Alg : " + schedulingAlg);
        }
        System.out.println(String.format("CPU utilization : %.2f", cpuUtilization) + " %");
        System.out.println(String.format("Throughput : %.3f processes / ms", (double) numRQJobs / readyClock));
        System.out.println(String.format("Avg. Turnaround time : %.2f ms", (double) readyTATime / numRQJobs));
        System.out.println(String.format("Avg. Waiting time in R queue : %.1f ms", (double) totalWaitingTimeInReadyQ / numRQJobs));
    }

    private static void calculateTotalCPURunningTime() {
        for (Node n: terminated) {
            totalWaitingTimeInReadyQ += n.getTimeInRQ();
            totalCPURunningTime += calculateUsageForProcess(n.getCpuBurst());
        }
    }

    private static int calculateUsageForProcess(int[] arr) {
        int totalTime = 0;
        for (int i: arr) {
            totalTime += i;
        }
        return totalTime;
    }

    private static long sumReadyProcessTime() {
        long sum = 0;
        for (long l: readyProcessTime) {
            sum += l;
        }
        return sum;
    }
}
