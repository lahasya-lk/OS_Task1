package project;

public class Prod_Cons_Demo {

public static void main(String[] args) {
int bSize = 5;
int nmbrOfItems = 10;

SharedBuffer buff = new SharedBuffer(bSize);

Producer prod = new Producer(buff, nmbrOfItems);
Consumer cons = new Consumer(buff, nmbrOfItems);

prod.start();
cons.start();

try {
prod.join();
cons.join();
} catch (InterruptedException e) {
System.out.println("Main thread was interrupted.");
}

System.out.println("\nProducer and Consumer have finished.");
}
}

class SharedBuffer {
private int[] buffer;
private int capacity;

private int count = 0;
private int in = 0;
private int out = 0;

public SharedBuffer(int capacity) {
this.capacity = capacity;
buffer = new int[capacity];
}

public synchronized void addItem(int item) throws InterruptedException {

while (count == capacity) {
System.out.println("Buffer is full. Producer is waiting...");
wait();
}

buffer[in] = item;
in = (in + 1) % capacity;
count++;

System.out.println("Produced: " + item +
" | Items present in a buffer: " + count);

notifyAll();
}

public synchronized int removeItem() throws InterruptedException {

while (count == 0) {
System.out.println("Buffer is empty. Consumer is waiting...");
wait();
}

int item = buffer[out];
out = (out + 1) % capacity;
count--;

System.out.println("Consumed: " + item +
" | Items present in a buffer: " + count);

notifyAll();

return item;
}
}

class Producer extends Thread {
private SharedBuffer buff;
private int nmbrOfItems;

public Producer(SharedBuffer buffer, int numberOfItems) {
this.buff = buffer;
this.nmbrOfItems = numberOfItems;
}

@Override
public void run() {
try {
for (int i = 1; i <= nmbrOfItems; i++) {

int item = i;

buff.addItem(item);

Thread.sleep(200);
}
} catch (InterruptedException e) {
System.out.println("Producer thread was interrupted.");
}
}
}

class Consumer extends Thread {
private SharedBuffer buff;
private int nmbrOfItems;

public Consumer(SharedBuffer buffer, int numberOfItems) {
this.buff = buffer;
this.nmbrOfItems = numberOfItems;
}

@Override
public void run() {
try {
for (int i = 1; i <= nmbrOfItems; i++) {

buff.removeItem();

Thread.sleep(400);
}
} catch (InterruptedException e) {
System.out.println("Consumer thread was interrupted.");
}
}
}
