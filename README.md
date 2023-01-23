# Printer-System Java Concurrency Implementation

> Built using Java 19.0.2 on IntelliJ IDEA 2022.3.1
> <br>By: [Nuvin Godakanda Arachchi](https://nuvinga.github.io) @ [GitHub](https://github.com/nuvinga)

This application was built as partial completion of my final year in the BEng (Hons) Software Engineering degree program at the 
University of Westminster

## Application Overview

This implementation mimics a shared printer, being used by students and technicians for printing and printer opertations using Java Threads and its
concurrency features. 

### Code Structure

The following core Java files were implemented.

1. **LaserPrinter.java** <br>
This Java class contains the implementation of a Monitor class. The monitor in multi-threaded applications is considered to be the
critical-section, aka. the shared resource. In this application, the printer identified to be the shared resource is implemented as the Monitor.
<br>Notice, each method signature of the implementation would've included the *"synchronized"* keyword, indicating of the concurrency 
*"locking mechanism"* which ensures ***mutually exclusive*** access to the printer for each user.

2. **Student.java** <br>
This Java class implements a thread, by extending the Thread class, mimicking the behavior of a student wanting to print a set of documents,
which is decided through a randomizer. <br>Extending the Thread class is not considered best practice when creating 
Thread functionalities when no Thread specializations are required, when the basic Thread behaviour is producible by simply implementing the
Runnable interface. However, the Thread has been extended here to keep in par with the specifications.

3. **PaperTechnician.java & TonerTechnician.java**<br>
These Java classes implements a thread again, mimicking the behavior of a Paper technician and a Toner cartdrige technician in charge of a printer. 
<br>As menitoned above, this class also only required the basic behavior of a Thread, however has been created by extending the Thread class.

### Indexed in Turn-It In Global Referencing Scheme

This project code should not be used for any academics related activity due to all code segments having been submitted to the
**Turn-It In global referencing platform**, where usage of this code may be caught for plagarism/ academic misconduct.

