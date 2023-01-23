/**
 * <h1>PaperTechnician</h1> <code>extends Thread</code><br>
 * <description>
 *     PaperTechnician class mimics a technician in-charge of refilling the paper when low of a shared printer used by
 *     students. <br><br>
 *     Similar to the Student, it would be best practice to implement the {@link Runnable} here too, however, in par
 *     with the requirements, {@link Thread} class has been extended.
 * </description>
 * */
public class PaperTechnician extends Thread {

    private final String name;
    private final ThreadGroup group;
    private final Printer printer;

    /**
     * PaperTechnician core constructor creates an instance of the PaperTechnician class which models a paper refilling
     * technician
     *
     * @param name The name of the paper technician
     * @param group The thread group the technician belongs
     * @param printer the shared instance of the monitor - laserPrinter
     */
    public PaperTechnician(String name, ThreadGroup group, Printer printer) {
        super(group, name);
        this.name = name;
        this.group = group;
        this.printer = printer;
    }

    /**
     * The override function of the Run method from the Thread class. You do NOT call this function directly to start
     * the thread which will just be treated as a function in the thread class and execute in the current thread, instead
     * of creating a new thread. This method will be automatically run after the thread has moved to its Runnable state.
     * The run method is similar to the main method, but for a thread.
     * */
    @Override
    public void run() {
        for (int i = 0; i < 3; i++) { // attempts to refill paper thrice
            ((ServicePrinter) printer).refillPaper();
            if (i != 2) { // each time =>  will be put to timed waiting for a random amount of seconds
                try {
                    Thread.sleep((int) (Math.random() * (5000 - 1000) + 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // once all the refillings are completed/ expired
        System.out.println("\u001B[35mREFILL: \t\u001B[30m\u001B[45m["+ Thread.currentThread().getName() +
                "]: Concluded Refillings. Total Paper Packs used: " + ((LaserPrinter) printer).getPaperRefills() + "\u001B[0m");
    }
}
