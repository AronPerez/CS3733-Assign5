import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileRead implements Runnable {

    private String inputFile = "";

    public FileRead(String inputFile) {
        this.inputFile = inputFile;
    }

    @Override
    public void run() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(inputFile));
            String line = reader.readLine();
            while (line != null) {
                String[] lineSplit = line.split(" ");
                if ("proc".equals(lineSplit[0])) {
                    Assign5.numProcesses++;
                    Node n = new Node();
                    n.setPr(Integer.parseInt(lineSplit[1]));
                    int numBursts = Integer.parseInt(lineSplit[2]);
                    n.setNumCPUBurst((numBursts / 2) + 1);
                    n.setNumIOBurst(numBursts / 2);
                    int[] cpuBurst = new int[n.getNumCPUBurst()];
                    int[] ioBurst = new int[n.getNumIOBurst()];
                    int burstCounter = 1, cpuCounter = 0, ioCounter = 0;
                    for (int i = 3; i < lineSplit.length; i++) {
                        if (burstCounter % 2 == 0) {
                            // Even
                            ioBurst[ioCounter] = Integer.parseInt(lineSplit[i]);
                            ioCounter++;
                        } else {
                            // Odd
                            cpuBurst[cpuCounter] = Integer.parseInt(lineSplit[i]);
                            cpuCounter++;
                        }
                        burstCounter++;
                    }
                    n.setCpuBurst(cpuBurst);
                    n.setIoBurst(ioBurst);
                    n.setCpuIndex(0);
                    n.setIoIndex(0);
                    n.setTimeCreated(System.currentTimeMillis());
                    n.setTimeEnteredRQ(System.currentTimeMillis());
                    Assign5.readyQ.addWrapper(n);
                    Assign5.semCpu.release();
                } else if ("sleep".equals(lineSplit[0])) {
                    try {
                        Thread.sleep(Integer.parseInt(lineSplit[1]));
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                } else if ("stop".equals(lineSplit[0])) {
                    break;
                } else {
                    System.err.println("ERROR: Cannot Parse: " + line + "! Continuing...");
                }
                line = reader.readLine();
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println("ERROR: Cannot Find File: " + inputFile);
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            System.err.println("ERROR: Cannot Access File" + inputFile);
            ioe.printStackTrace();
        }
        Assign5.fileReadDone = true;
    }
}
