/**
 * <h1>TonerTechnician</h1> <code>extends Thread</code><br>
 * <description>
 *     TonerTechnician class mimics a technician in-charge of replacing the toner when low of a shared printer used by
 *     students. <br><br>
 *     Similar to the Student, it would be best practice to implement the {@link Runnable} here too, however, in par
 *     with the requirements, {@link Thread} class has been extended.
 * </description>
 * */
public class TonerTechnician extends Thread {

    private final String name;
    private final ThreadGroup group;
    private final Printer printer;

    /**
     * TonerTechnician core constructor creates an instance of the TonerTechnician class which models a toner replacing
     * technician
     *
     * @param name The name of the toner technician
     * @param group The thread group the technician belongs
     * @param printer the shared instance of the monitor - laserPrinter
     */
    public TonerTechnician(String name, ThreadGroup group, Printer printer) {
        super(group, name);
        this.name = name;
        this.group = group;
        this.printer = printer;
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) { // attempts to replace the cartridge thrice in intervals of random seconds
            ((ServicePrinter) printer).replaceTonerCartridge();
            if (i != 2) {
                try {
                    Thread.sleep((int) (Math.random() * (5000 - 1000) + 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // once all the replaces have concluded/ expired
        System.out.println("\u001B[36mREPLACE: \t\u001B[30m\u001B[46m["+ Thread.currentThread().getName() +
                "]: Concluded Replaces. Total Cartridges used: " + ((LaserPrinter) printer).getTonerReplaces() + "\u001B[0m");
    }
}
