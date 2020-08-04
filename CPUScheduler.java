import java.util.concurrent.TimeUnit;

public class CPUScheduler implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            /* Needs to be mutuzed around since unsynced */
            if (Assign5.readyQ.isEmpty() && !Assign5.cpuBusy && Assign5.ioQ.isEmpty() && !Assign5.ioBusy && Assign5.fileReadDone) {
                break;
            }
            if ("FIFO".equals(Assign5.schedulingAlg)) {
                try {
                    boolean response = Assign5.semCpu.tryAcquire(1, TimeUnit.SECONDS);
                    if (!response) {
                        break;
                    }
                    Assign5.cpuBusy = true;
                    boolean proceed = true;
                    synchronized(Assign5.readyQ) { /* Also synchronized(obj){ } structure is OK to use for protecting critical sections in your methods).*/
                        if (Assign5.readyQ.isEmpty()) proceed = false;
                    }
                    if (proceed) {
                        Node n = Assign5.readyQ.removeFirstWrapper();
                        long processStart = System.currentTimeMillis();
                        n.setTimeInRQ(System.currentTimeMillis() - n.getTimeEnteredRQ());
                        int sleepTime = n.getCpuBurst()[n.getCpuIndex()];
                        Thread.sleep(sleepTime);
                        n.setWaitingTime(n.getWaitingTime() + Assign5.readyClock);
                        Assign5.totalReadyWaitingTime = Assign5.totalReadyWaitingTime + n.getWaitingTime();
                        Assign5.readyClock = Assign5.readyClock + sleepTime;
                        Assign5.readyTATime = Assign5.readyTATime + Assign5.readyClock;
                        Assign5.numRQJobs++;
                        n.setTimeEnteredRQ(0);
                        n.setCpuIndex(n.getCpuIndex() + 1);
                        Assign5.readyProcessTime.add(System.currentTimeMillis() - processStart);
                        if (n.getCpuIndex() >= n.getNumCPUBurst()) {
                            n.setTurnAroundTime(System.currentTimeMillis() - n.getTimeCreated());
                            Assign5.terminated.add(n);
                        } else {
                            Assign5.ioQ.addWrapper(n);
                        }
                    }
                    Assign5.cpuBusy = false;
                    Assign5.semIo.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if ("SJF".equals(Assign5.schedulingAlg)) {
                try {
                    boolean response = Assign5.semCpu.tryAcquire(1, TimeUnit.SECONDS);
                    if (!response) {
                        break;
                    }
                    Assign5.cpuBusy = true;
                    boolean proceed = true;
                    synchronized(Assign5.readyQ) { /* Also synchronized(obj){ } structure is OK to use for protecting critical sections in your methods).*/
                        if (Assign5.readyQ.isEmpty()) proceed = false;
                    }
                    if (proceed) {
                        Node n = Assign5.readyQ.removeMinCPUBurstWrapper();
                        long processStart = System.currentTimeMillis();
                        n.setTimeInRQ(System.currentTimeMillis() - n.getTimeEnteredRQ());
                        int sleepTime = n.getCpuBurst()[n.getCpuIndex()];
                        Thread.sleep(sleepTime);
                        n.setWaitingTime(n.getWaitingTime() + Assign5.readyClock);
                        Assign5.totalReadyWaitingTime = Assign5.totalReadyWaitingTime + n.getWaitingTime();
                        Assign5.readyClock = Assign5.readyClock + sleepTime;
                        Assign5.readyTATime = Assign5.readyTATime + Assign5.readyClock;
                        Assign5.numRQJobs++;
                        n.setTimeEnteredRQ(0);
                        n.setCpuIndex(n.getCpuIndex() + 1);
                        Assign5.readyProcessTime.add(System.currentTimeMillis() - processStart);
                        if (n.getCpuIndex() >= n.getNumCPUBurst()) {
                            n.setTurnAroundTime(System.currentTimeMillis() - n.getTimeCreated());
                            Assign5.terminated.add(n);
                        } else {
                            Assign5.ioQ.addWrapper(n);
                        }
                    }
                    Assign5.cpuBusy = false;
                    Assign5.semIo.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if ("PR".equals(Assign5.schedulingAlg)) {
                try {
                    boolean response = Assign5.semCpu.tryAcquire(1, TimeUnit.SECONDS);
                    if (!response) {
                        break;
                    }
                    Assign5.cpuBusy = true;
                    boolean proceed = true;
                    synchronized(Assign5.readyQ) { /* Also synchronized(obj){ } structure is OK to use for protecting critical sections in your methods).*/
                        if (Assign5.readyQ.isEmpty()) proceed = false;
                    }
                    if (proceed) {
                        Node n = Assign5.readyQ.removeHighestPRWrapper();
                        long processStart = System.currentTimeMillis();
                        n.setTimeInRQ(System.currentTimeMillis() - n.getTimeEnteredRQ());
                        int sleepTime = n.getCpuBurst()[n.getCpuIndex()];
                        Thread.sleep(sleepTime);
                        n.setWaitingTime(n.getWaitingTime() + Assign5.readyClock);
                        Assign5.totalReadyWaitingTime = Assign5.totalReadyWaitingTime + n.getWaitingTime();
                        Assign5.readyClock = Assign5.readyClock + sleepTime;
                        Assign5.readyTATime = Assign5.readyTATime + Assign5.readyClock;
                        Assign5.numRQJobs++;
                        n.setTimeEnteredRQ(0);
                        n.setCpuIndex(n.getCpuIndex() + 1);
                        Assign5.readyProcessTime.add(System.currentTimeMillis() - processStart);
                        if (n.getCpuIndex() >= n.getNumCPUBurst()) {
                            n.setTurnAroundTime(System.currentTimeMillis() - n.getTimeCreated());
                            Assign5.terminated.add(n);
                        } else {
                            Assign5.ioQ.addWrapper(n);
                        }
                    }
                    Assign5.cpuBusy = false;
                    Assign5.semIo.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if ("RR".equals(Assign5.schedulingAlg)) {
                try {
                    boolean response = Assign5.semCpu.tryAcquire(1, TimeUnit.SECONDS);
                    if (!response) {
                        break;
                    }
                    Assign5.cpuBusy = true;
                    boolean proceed = true;
                    synchronized(Assign5.readyQ) { /* Also synchronized(obj){ } structure is OK to use for protecting critical sections in your methods).*/
                        if (Assign5.readyQ.isEmpty()) proceed = false;
                    }
                    if (proceed) {
                        Node n = Assign5.readyQ.removeFirstWrapper();
                        long processStart = System.currentTimeMillis();
                        n.setTimeInRQ(System.currentTimeMillis() - n.getTimeEnteredRQ());
                        if (Assign5.quantum < n.getCpuBurst()[n.getCpuIndex()]) {
                            int[] temp = n.getCpuBurst();
                            temp[n.getCpuIndex()] = temp[n.getCpuIndex()] - Assign5.quantum;
                            n.setCpuBurst(temp);
                        }
                        int sleepTime = n.getCpuBurst()[n.getCpuIndex()];
                        Thread.sleep(sleepTime);
                        n.setWaitingTime(n.getWaitingTime() + Assign5.readyClock);
                        Assign5.totalReadyWaitingTime = Assign5.totalReadyWaitingTime + n.getWaitingTime();
                        Assign5.readyClock = Assign5.readyClock + sleepTime;
                        Assign5.readyTATime = Assign5.readyTATime + Assign5.readyClock;
                        Assign5.numRQJobs++;
                        n.setTimeEnteredRQ(0);
                        n.setCpuIndex(n.getCpuIndex() + 1);
                        Assign5.readyProcessTime.add(System.currentTimeMillis() - processStart);
                        if (n.getCpuIndex() >= n.getNumCPUBurst()) {
                            n.setTurnAroundTime(System.currentTimeMillis() - n.getTimeCreated());
                            Assign5.terminated.add(n);
                        } else {
                            Assign5.ioQ.addWrapper(n);
                        }
                    }
                    Assign5.cpuBusy = false;
                    Assign5.semIo.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Assign5.cpuDone = true;
    }
}
