package org;

public class Main {
    public static void main(String[] args) {

        RingBuffer<Integer> buffer = new RingBuffer<>(5);


        Thread producer = new Thread(() -> {
            int i = 1;
            try {
                while (i < 10) {
                    System.out.println("Producing: " + i);
                    buffer.put(i++);
                    Thread.sleep(500); // имитация работы
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        });


        Thread consumer = new Thread(() -> {
            try {
                int a = 0;
                while (a++ < 10) {
                    int item = buffer.take();
                    System.out.println("Consuming: " + item);
                    Thread.sleep(1000); // имитация работы
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        });

        producer.start();
        consumer.start();
    }
}