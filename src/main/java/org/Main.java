package org;

public class Main {
    public static void main(String[] args) {

        RingBuffer<Integer> buffer = new RingBuffer<>(5);


        Thread producer = new Thread(() -> {
            int i = 1;
            try {
                while (true) {
                    System.out.println("Producing: " + i);
                    buffer.put(i++);
                    Thread.sleep(500); // имитация работы
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });


        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    int item = buffer.take();
                    System.out.println("Consuming: " + item);
                    Thread.sleep(1000); // имитация работы
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}