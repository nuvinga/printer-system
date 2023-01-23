/**
 * <h1>LaserPrinter</h1>
 * <description>
 *     This class mimics the behaviour of a physical shared-printer. The model includes a paper tray, a toner
 *     cartridge and a functionality where students are able to print documents through the printer and printer
 *     technicians will be able to refill and replace the paper tray and the toner. <br><br>
 *     The LaserPrinter class is a monitor, which handles the critical section of a java multi-threaded application
 *     where multiple processes try to access the same shared resource which may result in data corruption and
 *     interference (more like a train wreck). This class delivers Mutual Exclusivity to the shared printer by
 *     synchronizing the methods and implementing a lock functionality to access the resources.
 * </description>
 * */
public class LaserPrinter implements ServicePrinter {

    private final String name;
    private int paperLevel;
    private int tonerLevel;
    private int countDocumentsPrinted;
    private final ThreadGroup studentGroup;
    private int tonerReplaces;
    private int paperRefills;
    private final String RESET_COLOR = "\u001B[0m";
    private final String SUCCESS_COLOR = "\u001B[32m";

    /**
     * LaserPrinter core constructor creates an instance of the LaserPrinter class which models a physical
     * shared printer.
     *
     * @param name         The name of the laser printer
     * @param paperLevel   The initial paper level of the printer
     * @param tonerLevel   The initial toner level of the printer
     * @param studentGroup The thread group the users of the printers belong
     */
    public LaserPrinter(String name, int paperLevel, int tonerLevel, ThreadGroup studentGroup) {
        this.name = name;
        this.paperLevel = paperLevel;
        this.tonerLevel = tonerLevel;
        this.studentGroup = studentGroup;
        // setting the internal variables to defaults
        this.countDocumentsPrinted = 0;
        this.tonerReplaces = 0;
        this.paperRefills = 0;
    }

    /**
     * Models students printing documents using a shared printer. Printing will only be allowed given that
     * adequate toner and paper levels are available matching size of the document.
     *
     * @param document instance of the {@link Document} class with data of the document
     */
    @Override
    public synchronized void printDocument(Document document) {
        String COLOR = "\u001B[34m";
        String ERROR_COLOR = "\u001B[31m";
        String documentKey = "PRINT: \t\t[" + Thread.currentThread().getName() + "][" + document.getDocumentName() + "]: ";

        while (this.paperLevel < document.getNumberOfPages() || this.tonerLevel < document.getNumberOfPages()) {
            // both toner and paper not available
            if (this.paperLevel < document.getNumberOfPages() && this.tonerLevel < document.getNumberOfPages())
                System.out.println(COLOR + documentKey + ERROR_COLOR + "Adequate Paper and Toner not available" + RESET_COLOR);

            // if the amount of paper is not enough
            if (this.paperLevel < document.getNumberOfPages())
                System.out.println(COLOR + documentKey + ERROR_COLOR + "No adequate Paper Level. Available: " +
                        this.paperLevel + "; Required: " + document.getNumberOfPages() + RESET_COLOR);

            // if the amount of toner is not enough
            if (this.tonerLevel < document.getNumberOfPages())
                System.out.println(COLOR + documentKey + ERROR_COLOR + "No adequate Cartridge Level. Available: " +
                        this.tonerLevel + "; Required: " + document.getNumberOfPages() + RESET_COLOR);

            try {
                // if resources are not available, the student move to waiting state till notified
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // if all resources are available, student will print the document and notify all waiting threads
        this.countDocumentsPrinted++;
        this.paperLevel -= document.getNumberOfPages();
        this.tonerLevel -= document.getNumberOfPages();
        System.out.println(COLOR + documentKey + SUCCESS_COLOR + "Print Completed" + RESET_COLOR);
        notifyAll();
    }

    /**
     * Models the paper technician attempting to refill the paper tray in the shared Printer. The paper can be
     * added only if the paper level falls below the boundary: {@link #Full_Paper_Tray} - {@link #SheetsPerPack}.
     * Then the technician can acquire the lock to the printer and reload the paper tray
     * */
    @Override
    public synchronized void refillPaper() {
        String COLOR = "\u001B[35m";
        String printKey = "REFILL: \t[" + Thread.currentThread().getName() + "]: ";

        while (this.paperLevel > (Full_Paper_Tray - SheetsPerPack)) {
            System.out.println(COLOR + printKey + "Paper tray already Full. Students Active? " + (studentGroup.activeCount() > 1) + RESET_COLOR);
            // if the paper tray is over 200 papers, the technician will go to TIMED waiting state
            try {
                wait(5000);
                // if all the students have finished printing, the paper count will not decrease and the technician
                // can go into a livelock. to escape this scenario, technician will check if the students group is
                // still active.
                if (studentGroup.activeCount() < 1) return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // if paper count falls below 200 technician can refill paper and notify all waiting threads
        System.out.println(COLOR + printKey + SUCCESS_COLOR + "Re-Filling paper! New paper pack added" + RESET_COLOR);
        this.paperLevel += SheetsPerPack;
        this.paperRefills++;
        notifyAll();
    }

    /**
     * Models the toner technician attempting to replace the toner cartridge in the shared Printer. The toner can be
     * replaced only if the toner level falls below the boundary: {@link #Minimum_Toner_Level}.
     * Then the technician can acquire the lock to the printer and replace the toner cartridge
     * */
    @Override
    public synchronized void replaceTonerCartridge() {
        String COLOR = "\u001B[36m";
        String printKey = "REPLACE: \t[" + Thread.currentThread().getName() + "]: ";

        while (this.tonerLevel >= Minimum_Toner_Level) {
            System.out.println(COLOR + printKey + "Toner still Available. Students Active? " + (studentGroup.activeCount() > 1) + RESET_COLOR);
            try {
                // if the toner is over 10 the technician will go into timed waiting
                wait(5000);
                if (studentGroup.activeCount() < 1) return; // checks if students are still active
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // if toner falls below 10 the technician can replace the toner and notify all waiting threads
        System.out.println(COLOR + printKey + SUCCESS_COLOR + "Re-Placing Toner cartridge! New toner added" + RESET_COLOR);
        this.tonerLevel = Full_Toner_Level;
        this.tonerReplaces++;
        notifyAll();
    }

    /**
     * This instantiatable method will print a string representation of the
     * current state of the laser-printer.
     *
     * @return string
     */
    @Override
    public String toString() {
        return "LaserPrinter [\n\tPrinterKey: \t'" + name + ", \n\tPaper Level: \t" + paperLevel + ", \n\tToner Level: \t" + tonerLevel + ", \n\tSum of Prints: \t" + countDocumentsPrinted + "\n]";
    }

    /**
     * Returns the count of toner replacing that took place during the run
     *
     * @return int
     */
    public synchronized int getTonerReplaces() {
        return tonerReplaces;
    }

    /**
     * Returns the count of paper refills that took place during the run
     *
     * @return int
     */
    public synchronized int getPaperRefills() {
        return paperRefills;
    }
}
