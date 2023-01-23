/**
 * <h1>Student</h1> <code>extends Thread</code> <br>
 * <description>
 *     The student class mimics a student needing to use the printer to print out {@link Document}s. The
 *     {@link LaserPrinter}, being the critical section will be accessed through the student by acquiring the
 *     synchronization lock before being able to perform the print operation. <br><br>
 *     <b>NOTE:</b> This implementation of student thread is done by <code>Extending the {@link Thread}</code> class,
 *     which is one of the two ways of implementing a thread in java; the other being by <code>implementing the
 *     {@link Runnable}</code> interface. However, since the student class has no need of producing a custom/ specialized
 *     behaviour of a thread, implementing the runnable would have been the best practise & approach. The sole reason
 *     for extending the thread class here is due to the coursework specification requiring the extension for the
 *     thread classes.
 * </description>
 * */
public class Student extends Thread {

    private final String name;
    private final ThreadGroup group;
    private final Printer printer;

    /**
     * Student core constructor creates an instance of the Student class which models a student
     *
     * @param name The name of the laser printer
     * @param group The thread group the student belongs
     * @param printer the shared instance of the monitor - laserPrinter
     */
    public Student(String name, ThreadGroup group, Printer printer) {
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
        int totalPageCount = 0;
        int PAGE_COUNT_LOWER_BOUND = 1;
        int PAGE_COUNT_UPPER_BOUND = 10;

        // creates 5 documents with a random number of pages defined by a upper and lower bound.
        for (int i = 0; i < 5; i++) {
            /*
            * NOTE: if the upper bound is increased over 10, the application maybe get stuck in a deadlock
            * situation where the toner cartridge maybe be over 10, thus making replacing the toner illegal, however,
            * a student may require a higher toner level to print the document.
            * Eg: Current toner level is 35; document to print is 50 pages => this will lead to a deadlock as the
            * toner technician is waiting for the toner to drop lower than 10 and the student is waiting for the
            * toner to be replaced, thus no party making progress.
            * */
            int randomPageCount = (int) (Math.random() * (PAGE_COUNT_UPPER_BOUND - PAGE_COUNT_LOWER_BOUND) + PAGE_COUNT_LOWER_BOUND);
            Document document = new Document(this.name + "_id_" + i,
                    this.name + "_doc." + i, randomPageCount);
            printer.printDocument(document);
            totalPageCount += randomPageCount;
            if (i != 4) { // will go to sleep in between each print (timed waiting/ sleep state)
                try {
                    Thread.sleep((int) (Math.random() * (4000 - 1000) + 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // once all the documents are printed
        System.out.println("\u001B[34mPRINT: \t\t\u001B[30m\u001B[44m["+ Thread.currentThread().getName() + "]: Printing Completed Sucessfully. Total Page Count: " + totalPageCount + "\u001B[0m");
    }


}
