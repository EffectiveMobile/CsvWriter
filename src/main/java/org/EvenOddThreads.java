package org;

public class EvenOddThreads {

    private static final int MAX = 10; // до какого числа выводим
    private int number = 1; // текущее число

    public static void main(String[] args) {
        EvenOddThreads printer = new EvenOddThreads();

        Thread oddThread = new Thread(() -> printer.printOdd(), "Odd");
        Thread evenThread = new Thread(() -> printer.printEven(), "Even");

        oddThread.start();
        evenThread.start();
    }

    public synchronized void printOdd() {
        while (number <= MAX) {
            // если число четное — ждем
            if (number % 2 == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            } else {
                System.out.println(Thread.currentThread().getName() + ": " + number);
                number++;
                notifyAll();
            }
        }
    }

    public synchronized void printEven() {
        while (number <= MAX) {
            // если число нечетное — ждем
            if (number % 2 != 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            } else {
                System.out.println(Thread.currentThread().getName() + ": " + number);
                number++;
                notifyAll();
            }
        }
    }
}
