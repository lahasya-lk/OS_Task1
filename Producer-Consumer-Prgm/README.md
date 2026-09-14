Producer-Consumer Problem Using Java Threads

Overview:
This project demonstrates the concept of Producer-Consumer Problem using Java threads.
A Producer is producing items and putting them in a common buffer while a Consumer is taking items from the 
buffer. Synchronization is used to make sure that the Producer is waiting if the buffer is full and 
the Consumer is waiting if the buffer is empty.

Concepts used:
Java Multithreading
Thread Synchronization
synchronized
wait()
notifyAll()
Circular Buffer
sleep()
join()

Working:
The buffer size is 5 and the number of items is 10,
the Producer is producing items from 1 to 10 and the Consumer is consuming the produced items, if the buffer
is full the Producer waits and if the buffer is empty the Consumer waits, notifyAll() method is 
used to notify waiting threads that an item has been added or removed.

The Project Structure:
Producer-Consumer/
└── src/
└── project/
└── Prod_Cons_Demo.java

Conclusion:

This project successfully demonstrates the Producer-Consumer problem using Java threads.
The Producer and Consumer are using a circular buffer of a fixed size, and the synchronization is 
achieved through synchronized, wait(), and notifyAll().
This project helps understand the concepts of threads synchronization and communication that are widely used
in the Operating Systems course.
