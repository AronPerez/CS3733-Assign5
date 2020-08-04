public class Node {

    private int pr, numCPUBurst, numIOBurst, cpuIndex, ioIndex;
    private int[] cpuBurst, ioBurst;
    private long timeCreated, timeEnteredRQ, timeInRQ, turnAroundTime, waitingTime, processTime;
    private Node prev, next;

    public Node() {
        prev = null;
        next = null;
    }

    public int getPr() {
        return pr;
    }

    public void setPr(final int pr) {
        this.pr = pr;
    }

    public int getNumCPUBurst() {
        return numCPUBurst;
    }

    public void setNumCPUBurst(final int numCPUBurst) {
        this.numCPUBurst = numCPUBurst;
    }

    public int getNumIOBurst() {
        return numIOBurst;
    }

    public void setNumIOBurst(final int numIOBurst) {
        this.numIOBurst = numIOBurst;
    }

    public int getCpuIndex() {
        return cpuIndex;
    }

    public void setCpuIndex(final int cpuIndex) {
        this.cpuIndex = cpuIndex;
    }

    public int getIoIndex() {
        return ioIndex;
    }

    public void setIoIndex(final int ioIndex) {
        this.ioIndex = ioIndex;
    }

    public int[] getCpuBurst() {
        return cpuBurst;
    }

    public void setCpuBurst(final int[] cpuBurst) {
        this.cpuBurst = cpuBurst;
    }

    public int[] getIoBurst() {
        return ioBurst;
    }

    public void setIoBurst(final int[] ioBurst) {
        this.ioBurst = ioBurst;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(final long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public long getTimeEnteredRQ() {
        return timeEnteredRQ;
    }

    public void setTimeEnteredRQ(final long timeEnteredRQ) {
        this.timeEnteredRQ = timeEnteredRQ;
    }

    public long getTimeInRQ() {
        return timeInRQ;
    }

    public void setTimeInRQ(final long timeInRQ) {
        this.timeInRQ = timeInRQ;
    }

    public void setTurnAroundTime(final long turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public long getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(final long waitingTime) {
        this.waitingTime = waitingTime;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(final Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(final Node next) {
        this.next = next;
    }
}
