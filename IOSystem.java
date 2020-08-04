import java.util.concurrent.TimeUnit;

public class IOSystem implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            if (Assign5.readyQ.isEmpty() && Assign5.ioQ.isEmpty() && Assign5.fileReadDone) {
                break;
            }
            try {
                boolean response = Assign5.semIo.tryAcquire(1, TimeUnit.SECONDS);
                if (!response) {
                    break;
                }
                Assign5.ioBusy = true;
                boolean proceed = true;
                synchronized(Assign5.ioQ) { /* Also synchronized(obj){ } structure is OK to use for protecting critical sections in your methods).*/
                    if (Assign5.ioQ.isEmpty()) proceed = false;
                }
                if (proceed) {
                    Node n = Assign5.ioQ.removeFirstWrapper();
                    Thread.sleep(n.getIoBurst()[n.getIoIndex()]);
                    n.setIoIndex(n.getIoIndex() + 1);
                    Assign5.numIOJobs++;
                    Assign5.readyQ.addWrapper(n);
                    n.setTimeEnteredRQ(System.currentTimeMillis());
                }
                Assign5.ioBusy = false;
                Assign5.semCpu.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Assign5.ioDone = true;
    }
}
