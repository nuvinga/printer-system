import java.util.ArrayList;

/**
 * <h1>PrintingSystem</h1>
 * <description>
 *     The class with the main method of the printing system. The printingSystem creates a single shared instance of
 *     the LaserPrinter identified as the critical section, along with four instances of students with documents for
 *     print, and 2 technicians, one paper technician and another the toner technician.
 * </description>
 * */
public class PrintingSystem {

    public static void main(String[] args) {
        ArrayList<Thread> studentThreads = new ArrayList<>();

        ThreadGroup studentGroup = new ThreadGroup(Thread.currentThread().getThreadGroup(), "Students");
        ThreadGroup technicianGroup = new ThreadGroup(Thread.currentThread().getThreadGroup(), "Technicians");

        // strategically setting the paper and toner levels so both technicians will have time to perform
        Printer printer = new LaserPrinter("LP:w1761350",0, 20, studentGroup);

        /*
         * Java has 2 methods of creating Java threads.
         *  1. Implementing the Runnable interface
         *  2. Extending the Thread class
         * Of these two methods, when creating threads that does not require specialization to the behavior of the
         * threads, the best practise is to implement the Runnable interface rather than extending the Thread Class.
         * However, in this implementation, the developer has extended the Thread class since the coursework
         * specification prefers to do so.
         * */
        for (int i = 0; i < 4; i++) {
            studentThreads.add(new Student("student-" + i, studentGroup, printer));
            // if the student class was an implementation of the Runnable interface:
            // studentThreads.add(new Thread(studentGroup, new Student("student-" + i, studentGroup, printer), "student-" + i));
        }
        Thread paperTech = new PaperTechnician("PaperTechnician", technicianGroup, printer);
        // the single laser printer instance is shared in all the students and technicians
        Thread tonerTech = new TonerTechnician("TonerTechnician", technicianGroup, printer);

        /*
        * As explained in the student class, only the Start method can move a thread from New to Runnable, effectively
        * starting/ running the thread. If the Run method was called, the method would be treated similar to a regular
        * method and perform the functionality on the thread it was called on, instead of creating a new one.
        * */
        studentThreads.forEach((Thread::start));
        paperTech.start();
        tonerTech.start();

        // waits for all the threads to move to its terminated state
        try {
            studentThreads.forEach((thread -> {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
            paperTech.join();
            tonerTech.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // finally, prints the printer summary
        System.out.println("\n\u001B[42m\u001B[30m\nAll Processes Ended \n\n\u001B[0m");
        System.out.println("\u001B[33mPrinting Printer Summary \n\u001B[0m");

        System.out.println(printer);

    }

}
